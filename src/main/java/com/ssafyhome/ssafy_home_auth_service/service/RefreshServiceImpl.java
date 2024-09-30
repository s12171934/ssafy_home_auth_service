package com.ssafyhome.ssafy_home_auth_service.service;

import com.ssafyhome.ssafy_home_auth_service.entity.RefreshEntity;
import com.ssafyhome.ssafy_home_auth_service.repository.RefreshRepository;
import com.ssafyhome.ssafy_home_auth_service.util.CookieUtil;
import com.ssafyhome.ssafy_home_auth_service.util.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RefreshServiceImpl implements RefreshService {

  private final JWTUtil jwtUtil;
  private final CookieUtil cookieUtil;
  private final RefreshRepository refreshRepository;
  private final long TEN_MINUTES = 10 * 60 * 1000L;
  private final long ONE_DAY = 24 * 60 * 60 * 1000L;

  public RefreshServiceImpl(
      JWTUtil jwtUtil,
      CookieUtil cookieUtil,
      RefreshRepository refreshRepository
  ) {

    this.jwtUtil = jwtUtil;
    this.cookieUtil = cookieUtil;
    this.refreshRepository = refreshRepository;
  }

  @Override
  public ResponseEntity<?> reissue(String refreshToken, HttpServletResponse response) {

    String refreshTokenError = checkRefreshToken(refreshToken);
    if (refreshTokenError != null) {
      return new ResponseEntity<>(refreshTokenError, HttpStatus.BAD_REQUEST);
    }

    String userId = jwtUtil.getKey(refreshToken, "userId");
    String userEmail = jwtUtil.getKey(refreshToken, "userEmail");
    setTokens(userId, userEmail, response);

    return new ResponseEntity<>("refresh token reissue", HttpStatus.CREATED);
  }

  @Override
  public void addRefreshTokenList(String refreshToken, String userId) {

    refreshRepository.save(new RefreshEntity(refreshToken, userId));
  }

  @Override
  public String checkRefreshToken(String refreshToken) {

    if (refreshToken.equals("no_refresh_token")) {
      return "refresh token not found";
    }

    if (!jwtUtil.getKey(refreshToken,"category").equals("refresh") ||
        !refreshRepository.existsById(refreshToken) ||
        jwtUtil.isExpired(refreshToken)
    ) {
      return "invalid refresh token";
    }

    return null;
  }

  @Override
  public void setTokens(String userId, String userEmail, HttpServletResponse response) {

    String newAccessToken = jwtUtil.createJWT(
        "access",
        userId,
        userEmail,
        TEN_MINUTES
    );
    String newRefreshToken = jwtUtil.createJWT(
        "refresh",
        userId,
        userEmail,
        ONE_DAY
    );

    addRefreshTokenList(newRefreshToken, userId);

    response.setHeader("Authorization", newAccessToken);
    response.addCookie(cookieUtil.createCookie("refresh", newRefreshToken));
  }

  @Override
  public String getRefreshToken(HttpServletRequest request) {

    Cookie[] cookies = request.getCookies();
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals("refresh")) return cookie.getValue();
    }

    return null;
  }
}
