package com.example.userservice.exception;


public class UserNotFoundedException extends Exception{
    public UserNotFoundedException(String message) {
        super(message);
    }
}
