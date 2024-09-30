package com.ssafyhome.ssafy_home_auth_service.filter;

import com.ssafyhome.ssafy_home_auth_service.repository.RefreshRepository;
import com.ssafyhome.ssafy_home_auth_service.service.RefreshService;
import com.ssafyhome.ssafy_home_auth_service.util.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class CustomLogoutFilter extends GenericFilterBean {

  private final RefreshService refreshService;
  private final RefreshRepository refreshRepository;
  private final CookieUtil cookieUtil;

  public CustomLogoutFilter(
      RefreshService refreshService,
      RefreshRepository refreshRepository,
      CookieUtil cookieUtil
  ) {

    this.refreshService = refreshService;
    this.refreshRepository = refreshRepository;
    this.cookieUtil = cookieUtil;
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
  }

  public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

    String refreshToken = refreshService.getRefreshToken(request);
    if (refreshService.checkRefreshToken(refreshToken) != null) {
      response.setStatus(HttpStatus.BAD_REQUEST.value());
      return;
    }



  }
}
