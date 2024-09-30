package com.ssafyhome.ssafy_home_auth_service.filter;

import com.ssafyhome.ssafy_home_auth_service.mapper.UserMapper;
import com.ssafyhome.ssafy_home_auth_service.service.RefreshService;
import com.ssafyhome.ssafy_home_auth_service.util.CookieUtil;
import com.ssafyhome.ssafy_home_auth_service.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final RefreshService refreshService;
  private final UserMapper userMapper;

  public CustomLoginFilter(
      AuthenticationManager authenticationManager,
      RefreshService refreshService,
      UserMapper userMapper
  ) {

    this.authenticationManager = authenticationManager;
    this.refreshService = refreshService;
    this.userMapper = userMapper;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

    String userId = obtainUsername(request);
    String password = obtainPassword(request);

    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(userId, password);

    return authenticationManager.authenticate(authenticationToken);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

    String userId = authResult.getName();
    String userEmail = userMapper.getUserById(userId).getUserEmail();
    refreshService.setTokens(userId, userEmail, response);
    response.setStatus(HttpStatus.OK.value());
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
  }
}
