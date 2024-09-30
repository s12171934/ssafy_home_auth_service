package com.ssafyhome.ssafy_home_auth_service.mapper;

import com.ssafyhome.ssafy_home_auth_service.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
  UserEntity getUserByIdAndEmail(String userId, String userEmail);

  UserEntity getUserById(String username);
}
