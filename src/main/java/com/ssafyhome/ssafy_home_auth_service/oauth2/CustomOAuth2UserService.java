package com.ssafyhome.ssafy_home_auth_service.oauth2;

import com.ssafyhome.ssafy_home_auth_service.entity.UserEntity;
import com.ssafyhome.ssafy_home_auth_service.mapper.UserMapper;
import com.ssafyhome.ssafy_home_auth_service.oauth2.response.GoogleResponse;
import com.ssafyhome.ssafy_home_auth_service.oauth2.response.KakaoResponse;
import com.ssafyhome.ssafy_home_auth_service.oauth2.response.NaverResponse;
import com.ssafyhome.ssafy_home_auth_service.oauth2.response.OAuth2Response;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserMapper userMapper;

	public CustomOAuth2UserService(UserMapper userMapper) {

		this.userMapper = userMapper;
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		OAuth2User oAuth2User = super.loadUser(userRequest);

		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		OAuth2Response oAuth2Response = null;
		switch (registrationId) {
			case "google" :
				oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
				break;
			case "naver" :
				oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
				break;
			case "kakao" :
				oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
				break;
			default:
				return null;
		}

		String userId = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
		UserEntity existUser = userMapper.getUserById(userId);

		if (existUser == null) {

			UserEntity userEntity = new UserEntity();
			userEntity.setUserId(userId);
			userEntity.setUserEmail(oAuth2Response.getEmail());

			userMapper.save(userEntity);
			return new CustomOAuth2User(userEntity);
		}
		else {
			existUser.setUserEmail(oAuth2Response.getEmail());

			userMapper.update(existUser);
			return new CustomOAuth2User(existUser);
		}
	}
}
