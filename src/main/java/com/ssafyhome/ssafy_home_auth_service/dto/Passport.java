package com.ssafyhome.ssafy_home_auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Passport {

  private String userId;
  private String role;
}
