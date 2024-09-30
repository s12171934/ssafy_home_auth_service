package com.ssafyhome.ssafy_home_auth_service.service;

import com.ssafyhome.ssafy_home_auth_service.dto.Passport;
import com.ssafyhome.ssafy_home_auth_service.entity.UserEntity;
import com.ssafyhome.ssafy_home_auth_service.mapper.UserMapper;
import com.ssafyhome.ssafy_home_auth_service.util.JWTUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PassportServiceImpl implements PassportService {

  private JWTUtil jwtUtil;
  private UserMapper userMapper;

  public PassportServiceImpl(JWTUtil jwtUtil) {

    this.jwtUtil = jwtUtil;
  }

  @Override
  public ResponseEntity<?> createPassport(String accessToken, HttpServletResponse response) {

    String userId = jwtUtil.getKey(accessToken, "userId");
    String userEmail = jwtUtil.getKey(accessToken, "userEmail");

    UserEntity userEntity = userMapper.getUserByIdAndEmail(userId, userEmail);
    if (userEntity == null) return new ResponseEntity<>("invalid access token", HttpStatus.UNAUTHORIZED);

    Passport passport = new Passport(
        userEntity.getUserId(),
        "user"
    );

    return new ResponseEntity<>(passport, HttpStatus.OK);
  }
}
