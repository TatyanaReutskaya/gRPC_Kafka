package com.example.userserviceapp.controller;

import com.example.userserviceapp.dto.UserDto;
import com.example.userserviceapp.entity.User;
import com.example.userserviceapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/healthCheck")
    @ResponseStatus(HttpStatus.OK)
    public void healthCheck() {
    }
    @GetMapping
    public List<UserDto> getAllUsers(){
        return userService.getAllUsers();
    }
    @PostMapping("/create")
    public void createUser(@RequestBody UserDto userDto){
        userService.save(userDto);
    }
}
