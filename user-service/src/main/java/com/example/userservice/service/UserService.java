package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.UserEntity;

import java.util.Iterator;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUserByUserId(String userId);
    UserDto getUserDetailsByEmail(String email);

    /*전체 사용자 목록 반환*/
    Iterable<UserEntity> getUsersAll();
}
