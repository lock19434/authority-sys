package uestc.config.springSecurity.handler;

import com.alibaba.fastjson.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import uestc.config.springSecurity.exception.CustomerAuthenticationException;
import uestc.utils.Result;
import uestc.utils.ResultCode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 *  登录认证失败处理器类
 */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 设置响应的编码格式
        response.setContentType("application/json;charset=utf-8");
        // 定义变量，保存异常类型
        String message = null;
        // 判断异常类型
        if (exception instanceof AccountExpiredException) {
            message = "账户过期，登录失效！";
        } else if (exception instanceof BadCredentialsException) {
            message = "用户名或密码错误！";
        } else if (exception instanceof DisabledException) {
            message = "账户被禁用，登录失效！";
        } else if (exception instanceof CredentialsExpiredException) {
            message = "密码过期，登录失效！";
        } else if (exception instanceof LockedException) {
            message = "账户被锁，登录失效！";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            message = "账户不存在！！";
        } else if (exception instanceof CustomerAuthenticationException) {
            message = "token不存在！！";
        } else {
            message = "登录失败！";
        }
        String result = JSON.toJSONString(Result.error().message(message).code(ResultCode.ERROR));
        // 获取输出流
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(result.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
