package com.example.authservice.user;

import com.example.authservice.AuthController;
import com.example.authservice.utils.UserRequestDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getByUsrEmail(String email);
    User getUserByEmail(String email);
    void saveUser(UserRequestDto userRequestDto);
}
