package com.ssafyhome.ssafy_home_auth_service.oauth2;

import com.ssafyhome.ssafy_home_auth_service.service.RefreshService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

	@Value("${front-end.url}")
	private String viewURL;
	private final RefreshService refreshService;

	public CustomSuccessHandler(RefreshService refreshService) {

		this.refreshService = refreshService;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

		CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
		String userId = customOAuth2User.getName();
		String userEmail = customOAuth2User.getEmail();
		refreshService.setTokens(userId, userEmail, response);

		response.sendRedirect(viewURL);
	}
}
