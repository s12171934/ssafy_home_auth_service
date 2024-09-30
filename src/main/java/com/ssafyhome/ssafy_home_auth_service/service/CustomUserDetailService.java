package com.ssafyhome.ssafy_home_auth_service.service;

import com.ssafyhome.ssafy_home_auth_service.dto.CustomUserDetail;
import com.ssafyhome.ssafy_home_auth_service.entity.UserEntity;
import com.ssafyhome.ssafy_home_auth_service.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailService implements UserDetailsService {

  private final UserMapper userMapper;

  public CustomUserDetailService(UserMapper userMapper) {

    this.userMapper = userMapper;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    UserEntity userEntity = userMapper.getUserById(username);
    if (userEntity == null) return  null;
    return new CustomUserDetail(userEntity);
  }
}
