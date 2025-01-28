package uestc.config.springSecurity.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import uestc.entity.User;
import uestc.utils.JwtUtils;
import uestc.utils.LoginResult;
import uestc.utils.ResultCode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 登录认证成功处理器（适配 jjwt 0.12.5）
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        User user = (User) authentication.getPrincipal();

        // 生成 Token
        String token = jwtUtils.generateToken(user);

        // 获取过期时间
        long expireTime = System.currentTimeMillis() + jwtUtils.getExpiration();

        // 构造返回结果
        LoginResult loginResult = new LoginResult(
                user.getId(),
                ResultCode.SUCCESS,
                token,
                expireTime
        );

        String jsonResult = JSON.toJSONString(
                loginResult,
                SerializerFeature.DisableCircularReferenceDetect
        );

        try (ServletOutputStream outputStream = response.getOutputStream()) {
            outputStream.write(jsonResult.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        }
    }

}