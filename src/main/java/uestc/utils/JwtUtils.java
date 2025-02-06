package uestc.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import uestc.entity.User;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
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
        try {
            // 使用 SHA-256 处理密钥
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(secret.getBytes(StandardCharsets.UTF_8));
            return Keys.hmacShaKeyFor(hash);
        } catch (NoSuchAlgorithmException e) {
            // 降级处理：如果SHA-256不可用，使用简单填充
            return Keys.hmacShaKeyFor(
                    Arrays.copyOf(secret.getBytes(StandardCharsets.UTF_8), 32)
            );
        }
    }

    /**
     * 从数据声明生成Token令牌
     */
    private String generateToken(Map<String, Object> claims) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 从令牌中获取数据声明
     */
    public Claims getClaimsFromToken(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }

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
        return claims != null ? claims.getSubject() : null;
    }

    /**
     * 生成令牌
     */
    public String generateToken(UserDetails userDetails) {
        if (userDetails == null || userDetails.getUsername() == null) {
            return null;
        }

        Map<String, Object> claims = new HashMap<>(2);
        claims.put(Claims.SUBJECT, userDetails.getUsername());
        claims.put("created", new Date());
        return generateToken(claims);
    }

    /**
     * 刷新令牌
     */
    public String refreshToken(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }

        Claims claims = getClaimsFromToken(token);
        if (claims == null || isTokenExpired(token)) {
            return null;
        }

        // 创建新的claims，只保留必要信息
        Map<String, Object> refreshedClaims = new HashMap<>(2);
        refreshedClaims.put(Claims.SUBJECT, claims.getSubject());
        refreshedClaims.put("created", new Date());

        return generateToken(refreshedClaims);
    }

    /**
     * 判断令牌是否过期
     */
    public Boolean isTokenExpired(String token) {
        if (token == null || token.isEmpty()) {
            return true;
        }

        try {
            Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                return true;
            }
            Date expiration = claims.getExpiration();
            return expiration == null || expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 验证令牌
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        if (token == null || token.isEmpty() || !(userDetails instanceof User)) {
            return false;
        }

        try {
            String username = getUsernameFromToken(token);
            return username != null &&
                    username.equals(userDetails.getUsername()) &&
                    !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}