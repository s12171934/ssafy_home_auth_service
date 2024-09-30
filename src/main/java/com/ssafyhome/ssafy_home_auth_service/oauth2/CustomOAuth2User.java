package com.ssafyhome.ssafy_home_auth_service.oauth2;

import com.ssafyhome.ssafy_home_auth_service.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

	private final UserEntity userEntity;

	public CustomOAuth2User(UserEntity userEntity){

		this.userEntity = userEntity;
	}

	@Override
	public Map<String, Object> getAttributes() {

		return null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Collection<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return "user";
			}
		});

		return authorities;
	}

	@Override
	public String getName() {

		return userEntity.getUserId();
	}

	public String getEmail() {

		return userEntity.getUserEmail();
	}
}
