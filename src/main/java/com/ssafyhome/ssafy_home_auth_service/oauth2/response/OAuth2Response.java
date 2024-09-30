package com.ssafyhome.ssafy_home_auth_service.oauth2.response;

public interface OAuth2Response {

	String getProvider();
	String getProviderId();
	String getEmail();
	String getName();
}
