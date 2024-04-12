package com.example.userserviceapp.service.impl;

import com.example.userserviceapp.dto.UserDto;
import com.example.userserviceapp.entity.User;
import com.example.userserviceapp.entity.enums.UserStatus;
import com.example.userserviceapp.mapper.UserMapper;
import com.example.userserviceapp.repository.UserRepository;
import com.example.userserviceapp.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final int MONTH_WITHOUT_ACTIVE = 6;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public User save(UserDto userDto) {
        User user = userMapper.userDtoToUser(userDto);
        user.setUserStatus(UserStatus.ACTIVE);
        user.setExpirationDate(LocalDateTime.now().plusMonths(MONTH_WITHOUT_ACTIVE));
        userRepository.save(user);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::userToUserDto).toList();
    }
}
