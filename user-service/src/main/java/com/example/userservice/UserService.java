package com.example.userservice;

import com.example.userservice.entities.UserDTO;
import com.example.userservice.entities.UserEntity;

public interface UserService {
    void createUser(UserDTO user);
    void deleteUser(Long id);
    UserEntity getUserById(Long id) throws UserNotFoundedException;
}
