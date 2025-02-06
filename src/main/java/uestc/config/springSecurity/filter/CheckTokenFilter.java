package uestc.config.springSecurity.filter;

import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.http.SecurityHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import uestc.config.redis.RedisService;
import uestc.config.springSecurity.CustomerUserDetailService;
import uestc.config.springSecurity.exception.CustomerAuthenticationException;
import uestc.config.springSecurity.handler.LoginFailureHandler;
import uestc.utils.JwtUtils;

import java.io.IOException;

@Data
@Component
public class CheckTokenFilter extends OncePerRequestFilter {

    @Value("${request.login.url}")
    private String loginUrl;

    @Resource
    private RedisService redisService;
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private CustomerUserDetailService customerUserDetailService;
    @Resource
    private LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 获取当前请求的URL地址
            String url = request.getRequestURI();
            // 判断当前请求是否是登录请求，否则需要验证Token
            if (! url.equals(loginUrl)) {
                // 进行Token认证
                this.validateToken(request);
            }
        } catch (AuthenticationException e) {
            loginFailureHandler.onAuthenticationFailure(request, response, e);
        }
        // 否则登录请求直接放行
        doFilter(request, response, filterChain);
    }

    // 进行Token认证
    private void validateToken(HttpServletRequest request) throws CustomerAuthenticationException {
        // 从header中获取token信息
        String token = request.getHeader("token");
        if (ObjectUtils.isEmpty(token)) {
            // 从参数中获取
            token = request.getParameter("token");
        }
        // 如果请求头或者请求参数中都没有token信息，则抛出异常
        if (ObjectUtils.isEmpty(token)) {
            // 抛出异常
            throw new CustomerAuthenticationException("token不存在！");
        }
        // 判断redis中是否存在token信息
        String tokenKey = "token_" + token;
        String redisToken = redisService.get(tokenKey);
        // 如果redis中token为空，则token失效
        if (ObjectUtils.isEmpty(redisToken)) {
            throw new CustomerAuthenticationException("token失效！");
        }
        // 如果token与redis的token不一致，则token验证失效。
        if (!token.equals(redisToken)) {
            throw new CustomerAuthenticationException("token验证失败！");
        }
        // 如果token存在，从token中解析出用户名
        String username = jwtUtils.getUsernameFromToken(token);
        if (ObjectUtils.isEmpty(username)) {
            throw new CustomerAuthenticationException("token解析失败！ ");
        }
        // 获取用户信息
        UserDetails userDetails = customerUserDetailService.loadUserByUsername(username);
        if (ObjectUtils.isEmpty(userDetails)) {
            throw new CustomerAuthenticationException("token验证失败！");
        }
        // 创建用户身份认证对象
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        // 设置请求信息
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // 将验证的信息交给spring security上下文
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
