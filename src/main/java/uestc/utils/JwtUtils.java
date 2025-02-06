package uestc.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import uestc.entity.User;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 */
@Data
@ConfigurationProperties(prefix = "jwt")
@Component
public class JwtUtils {

    private String secret;      // 密钥
    private Long expiration;    // 过期时间

    /**
     * 获取签名密钥
     */
    private SecretKey getSigningKey() {
        // 使用重复填充的方式达到所需长度
        byte[] keyBytes = new byte[32]; // 256 bits
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < keyBytes.length; i++) {
            keyBytes[i] = secretBytes[i % secretBytes.length];
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 从数据声明生成Token令牌
     */
    private String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + expiration);
        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .expiration(expirationDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 从令牌中获取数据声明
     */
    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * 从令牌中获取用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return (claims != null) ? claims.getSubject() : null;
    }

    /**
     * 生成令牌
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(1);
        claims.put(Claims.SUBJECT, userDetails.getUsername());
        return generateToken(claims);
    }

    /**
     * 刷新令牌
     */
    public String refreshToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                return null;
            }
            claims.put(Claims.ISSUED_AT, new Date());
            return generateToken(claims);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断令牌是否过期
     */
    public Boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                return true;
            }
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 验证令牌
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        if (!(userDetails instanceof User)) {
            return false;
        }

        String username = getUsernameFromToken(token);
        return username != null &&
                username.equals(userDetails.getUsername()) &&
                !isTokenExpired(token);
    }
}