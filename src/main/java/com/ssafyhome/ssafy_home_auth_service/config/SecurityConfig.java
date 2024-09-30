package com.ssafyhome.ssafy_home_auth_service.config;

import com.ssafyhome.ssafy_home_auth_service.filter.CustomLoginFilter;
import com.ssafyhome.ssafy_home_auth_service.filter.CustomLogoutFilter;
import com.ssafyhome.ssafy_home_auth_service.mapper.UserMapper;
import com.ssafyhome.ssafy_home_auth_service.oauth2.CustomOAuth2UserService;
import com.ssafyhome.ssafy_home_auth_service.oauth2.CustomSuccessHandler;
import com.ssafyhome.ssafy_home_auth_service.repository.RefreshRepository;
import com.ssafyhome.ssafy_home_auth_service.service.RefreshService;
import com.ssafyhome.ssafy_home_auth_service.util.CookieUtil;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final AuthenticationConfiguration authenticationConfiguration;
	private final RefreshRepository refreshRepository;
	private final RefreshService refreshService;
	private final UserMapper userMapper;
	private final CookieUtil cookieUtil;

	public SecurityConfig(
			AuthenticationConfiguration authenticationConfiguration,
			RefreshRepository refreshRepository,
			RefreshService refreshService,
			UserMapper userMapper,
			CookieUtil cookieUtil
	) {

		this.authenticationConfiguration = authenticationConfiguration;
		this.refreshRepository = refreshRepository;
		this.refreshService = refreshService;
		this.userMapper = userMapper;
		this.cookieUtil = cookieUtil;
	}

	@SneakyThrows
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {

		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}

	@SneakyThrows
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService, CustomSuccessHandler customSuccessHandler) {

		http.csrf((auth) -> auth.disable());
		http.formLogin((auth) -> auth.disable());
		http.httpBasic((auth) -> auth.disable());

		CustomLoginFilter customLoginFilter = new CustomLoginFilter(
				authenticationManager(authenticationConfiguration),
				refreshService,
				userMapper
		);
		customLoginFilter.setFilterProcessesUrl("/auth/login");
		http.addFilterAt(customLoginFilter, UsernamePasswordAuthenticationFilter.class);

		CustomLogoutFilter customLogoutFilter = new CustomLogoutFilter(
				refreshRepository,
				refreshService,
				cookieUtil
		);
		http.addFilterBefore(customLogoutFilter, LogoutFilter.class);

		http.oauth2Login((oauth2) -> oauth2
				.userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
						.userService(customOAuth2UserService))
				.successHandler(customSuccessHandler)
		);

		http.sessionManagement((session) -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		);

		return http.build();
	}
}
