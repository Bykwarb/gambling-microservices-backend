package com.example.userservice;

import com.example.userservice.entities.UserDTO;
import com.example.userservice.entities.UserEntity;
import com.example.userservice.exception.UserAlreadyExistException;
import com.example.userservice.exception.UserNotFoundedException;

public interface UserService {
    void createUser(UserDTO user) throws UserAlreadyExistException;
    void deleteUser(Long id);
    UserEntity getUserByUserName(String username);
    UserEntity getUserById(Long id) throws UserNotFoundedException;
}
