package com.ssafyhome.ssafy_home_auth_service.controller;

import com.ssafyhome.ssafy_home_auth_service.service.PassportService;
import com.ssafyhome.ssafy_home_auth_service.service.RefreshService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final PassportService passportService;
  private final RefreshService refreshService;

  public AuthController(
      PassportService passportService,
      RefreshService refreshService
  ) {

    this.passportService = passportService;
    this.refreshService = refreshService;
  }

  @GetMapping("/passport")
  public ResponseEntity<?> getPassport(
      @RequestHeader("Authorization")
      String accessToken,
      HttpServletResponse response
  ) {

    return passportService.createPassport(accessToken, response);
  }

  @PostMapping("/reissue")
  public ResponseEntity<?> reissue(
      @CookieValue(value = "refreshToken", defaultValue = "no_refresh_token")
      String refreshToken,
      HttpServletResponse response) {

    return refreshService.reissue(refreshToken, response);
  }
}
