package uestc.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uestc.config.redis.RedisService;
import uestc.utils.JwtUtils;
import uestc.utils.Result;
import uestc.vo.TokenVo;

import java.util.Date;

@RestController
@RequestMapping("/sysUser")
public class SysUserController {

    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private RedisService redisService;

    /**
     *  刷新Token
     */
    @PostMapping("/refreshToken")
    public Result<TokenVo> refreshToken(HttpServletRequest request) {
        // 从header中获取token
        String token = request.getHeader("token");
        // 从参数中获取token
        if (ObjectUtils.isEmpty(token)) {
            token = request.getParameter("token");
        }
        // 获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // 保存新的token
        String newToken = "";
        // 验证 token 是否有效
        if (jwtUtils.validateToken(token, userDetails)) {
            // 如果 token 有效，刷新 token
            newToken = jwtUtils.refreshToken(token);
        }
        // 获取新的 token 的过期时间
        Claims claims = jwtUtils.getClaimsFromToken(newToken);
        Date expiration = claims.getExpiration();

        // 删除旧的token
        redisService.del("token_" + token);

        // 设置新的token
        String newTokenKey = "token_" + newToken;
        redisService.set(newTokenKey, newToken, jwtUtils.getExpiration()/1000);
        // 创建tokenVo对象
        TokenVo tokenVo = new TokenVo(expiration.getTime(), newToken);
        return Result.ok(tokenVo).message("token刷新成功！");

    }

}
