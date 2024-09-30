package com.ssafyhome.ssafy_home_auth_service.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface PassportService {

  ResponseEntity<?> createPassport(String accessToken, HttpServletResponse response);
}
