package com.example.authservice.user;

import com.example.authservice.utils.UserDto;

import java.util.Optional;

public interface UserService {
    Optional<User> getByUsrEmail(String email);
    User getUserByEmail(String email);
    void saveUser(UserDto userDto);
}
