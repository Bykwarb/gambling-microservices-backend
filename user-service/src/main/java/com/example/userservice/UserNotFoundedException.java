package com.example.userservice;


public class UserNotFoundedException extends Exception{
    public UserNotFoundedException(String message) {
        super(message);
    }
}
