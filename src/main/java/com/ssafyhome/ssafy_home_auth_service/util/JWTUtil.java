package com.ssafyhome.ssafy_home_auth_service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

  private SecretKey secretKey;

  public JWTUtil(
      @Value("${jwt.secret}")
      String secret
  ) {
    this.secretKey = new SecretKeySpec(
        secret.getBytes(StandardCharsets.UTF_8),
        Jwts.SIG.HS256.key().build().getAlgorithm()
    );
  }

  public Claims claimsFromToken(String token) {

    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public String getKey(String token, String key) {

    return claimsFromToken(token)
        .get(key, String.class);
  }

  public Boolean isExpired(String token) {

    try {
      Date expiration = claimsFromToken(token).getExpiration();
      return expiration.before(new Date());
    } catch (ExpiredJwtException e) {
      return true;
    }
  }

  public String createJWT(String category, String username, String email, Long expiration) {

    return Jwts.builder()
        .claim("category", category)
        .claim("username", username)
        .claim("email", email)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(secretKey)
        .compact();
  }
}
