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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uestc.config.redis.RedisService;
import uestc.entity.Permission;
import uestc.entity.User;
import uestc.entity.UserInfo;
import uestc.utils.JwtUtils;
import uestc.utils.MenuTree;
import uestc.utils.Result;
import uestc.vo.RouterVo;
import uestc.vo.TokenVo;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @GetMapping("/getInfo")
    public Result<UserInfo> getUserInfo() {
        // 从spring security中获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ObjectUtils.isEmpty(authentication)) {
            return Result.<UserInfo>error().message("用户查询失败！");
        }
        // 获取用户信息
        User user = (User) authentication.getPrincipal();
        // 获取权限列表
        List<Permission> permissionList = user.getPermissionList();
        // 获取角色权限编码字段
        Object[] roles = permissionList.stream()
                .filter(Objects::nonNull)        // 过滤掉 null 元素
                .map(Permission::getCode)        // 获取权限编码
                .toArray();
        // 创建用户信息对象
        UserInfo userInfo = new UserInfo(user.getId(), user.getNickName(), user.getAvatar(), null, roles);
        return Result.ok(userInfo).message("用户信息查询成功！");
    }

    @GetMapping("/getMenuList")
    public Result<List<RouterVo>> getMenuList() {
        // 从spring security中获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ObjectUtils.isEmpty(authentication)) {
            return Result.<List<RouterVo>>error().message("用户查询失败！");
        }
        // 获取用户信息
        User user = (User) authentication.getPrincipal();
        // 获取用户权限
        List<Permission> permissionList = user.getPermissionList();
        // 筛选目录和菜单
        List<Permission> collect = permissionList.stream()
                .filter(item -> item!=null && item.getType() != 2)
                .toList();
        // 生成路由数据
        List<RouterVo> routerVoList = MenuTree.makeRouter(collect, 0L);
        // 返回数据
        return Result.ok(routerVoList).message("菜单数据获取成功！");
    }

}
