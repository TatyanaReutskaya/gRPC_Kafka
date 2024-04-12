package com.example.userserviceapp.service;

import com.example.userserviceapp.dto.UserDto;
import com.example.userserviceapp.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User save(UserDto userDto);
    List<UserDto> getAllUsers();
}
