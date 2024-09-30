package com.ssafyhome.ssafy_home_auth_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@AllArgsConstructor
@RedisHash(value = "refresh", timeToLive = 24 * 60 * 60)
public class RefreshEntity {

  @Id
  private String refreshToken;
  private String username;
}
