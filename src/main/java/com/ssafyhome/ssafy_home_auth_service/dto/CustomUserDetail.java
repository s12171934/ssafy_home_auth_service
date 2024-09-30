package com.ssafyhome.ssafy_home_auth_service.dto;

import com.ssafyhome.ssafy_home_auth_service.entity.UserEntity;
import com.ssafyhome.ssafy_home_auth_service.mapper.UserMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetail implements UserDetails {

  private final UserEntity userEntity;

  public CustomUserDetail(UserEntity userEntity) {

    this.userEntity = userEntity;
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
  public String getPassword() {
    return userEntity.getUserPw();
  }

  @Override
  public String getUsername() {
    return userEntity.getUserId();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
