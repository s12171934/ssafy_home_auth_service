package com.ssafyhome.ssafy_home_auth_service.filter;

import com.ssafyhome.ssafy_home_auth_service.repository.RefreshRepository;
import com.ssafyhome.ssafy_home_auth_service.service.RefreshService;
import com.ssafyhome.ssafy_home_auth_service.util.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class CustomLogoutFilter extends GenericFilterBean {

  private final RefreshRepository refreshRepository;
  private final RefreshService refreshService;
  private final CookieUtil cookieUtil;

  public CustomLogoutFilter(
      RefreshRepository refreshRepository,
      RefreshService refreshService,
      CookieUtil cookieUtil
  ) {

    this.refreshRepository = refreshRepository;
    this.refreshService = refreshService;
    this.cookieUtil = cookieUtil;
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
  }

  public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

    String requestURI = request.getRequestURI();
    String requestMethod = request.getMethod();
    if(!requestURI.startsWith("/auth/logout") || !requestMethod.equals("post")) {
      filterChain.doFilter(request, response);
      return;
    }

    String refreshToken = refreshService.getRefreshToken(request);
    if (refreshService.checkRefreshToken(refreshToken) != null) {
      response.setStatus(HttpStatus.BAD_REQUEST.value());
      return;
    }

    refreshRepository.deleteById(refreshToken);
    response.addCookie(cookieUtil.deleteCookie("refresh"));
    response.setStatus(HttpStatus.OK.value());
  }
}
