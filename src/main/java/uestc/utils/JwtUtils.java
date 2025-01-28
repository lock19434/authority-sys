package uestc.utils;

import io.jsonwebtoken.*;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import uestc.entity.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *  JWT工具类
 */
@Data
@ConfigurationProperties(prefix = "jwt")
@Component
public class JwtUtils {

    private String secret;      // 密钥
    private Long expiration;    // 过期时间

    /**
     *  从数据声明生成Token令牌
     */
    private String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + this.expiration);
        return Jwts
                .builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.ES512, this.secret)
                .compact();
    }

    /**
     * 从令牌中获取数据声明
     */
    public Claims getClaimsFormToken(String token) {
        Claims claims;
        try {
            claims = Jwts
                    .parser()
                    .setSigningKey(this.secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }

        return claims;
    }

    public String getUsernameFromToken(String token) {
        String username = null;
        try {
            Claims claims = getClaimsFormToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return username;
    }

    /**
     *  生成令牌
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(2);
        claims.put(Claims.SUBJECT, userDetails.getUsername());
        claims.put(Claims.ISSUED_AT, new Date());
        return generateToken(claims);
    }

    /**
     * 刷新令牌
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFormToken(token);
            claims.put(Claims.ISSUED_AT, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return refreshedToken;
    }

    /**
     * 判断令牌是否过期
     */
    public Boolean isTokenExpired(String token) {
        Claims claims = getClaimsFormToken(token);
        Date expiration = claims.getExpiration();
        return  expiration.before(new Date());
    }

    /**
     * 验证令牌
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        User user = (User) userDetails;
        String username = getUsernameFromToken(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

}
