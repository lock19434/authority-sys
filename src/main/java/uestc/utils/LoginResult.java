package uestc.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  返回Token信息类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResult {

    private Long id;            // 用户编号
    private int code;           // 状态码
    private String token;       // token令牌
    private Long expireTime;    // token过期时间


}
