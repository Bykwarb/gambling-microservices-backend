package com.example.userservice;

public interface UserService {
    void createUser(UserDTO user);
    void deleteUser(Long id);
    UserEntity getUserById(Long id) throws UserNotFoundedException;
}
