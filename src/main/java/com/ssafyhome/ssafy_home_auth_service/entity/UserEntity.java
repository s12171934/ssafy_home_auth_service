package com.ssafyhome.ssafy_home_auth_service.entity;

import lombok.Data;

@Data
public class UserEntity {

  private int userSeq;
  private String userId;
  private String userPw;
  private String userEmail;
}
