package com.example.userserviceapp.mapper;

import com.example.userserviceapp.dto.UserDto;
import com.example.userserviceapp.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User userDtoToUser(UserDto userDto);
    UserDto userToUserDto(User user);
}
