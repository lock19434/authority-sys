package uestc.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenVo {
    private Long expireTime;    // 过期时间
    private String token;       // token

}
