package com.ssafyhome.ssafy_home_auth_service.util;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

  private static final int ONE_DAY = 24 * 60 * 60;
  private static final int DELETE = 0;

  public Cookie createCookie(String key, String value) {

    Cookie cookie = new Cookie(key, value);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setMaxAge(ONE_DAY);
    return cookie;
  }

  public Cookie deleteCookie(String key) {

    Cookie cookie = new Cookie(key, null);
    cookie.setPath("/");
    cookie.setMaxAge(DELETE);
    return cookie;
  }
}
