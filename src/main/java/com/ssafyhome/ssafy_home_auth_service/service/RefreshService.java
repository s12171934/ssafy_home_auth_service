package com.ssafyhome.ssafy_home_auth_service.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface RefreshService {

  ResponseEntity<?> reissue(String refreshToken, HttpServletResponse response);
  void addRefreshTokenList(String refreshToken, String username);
  String checkRefreshToken(String refreshToken);
  void setTokens(String userId, String userEmail, HttpServletResponse response);
  String getRefreshToken(HttpServletRequest request);
}
