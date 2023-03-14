package com.example.authservice.user;

import com.example.authservice.utils.UserDto;

import java.util.Optional;

public interface UserService {
    User getUserByEmail(String email);
    void saveUser(UserDto userDto);
}
