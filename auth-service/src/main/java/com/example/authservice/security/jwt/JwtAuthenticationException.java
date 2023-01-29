package com.example.authservice.security.jwt;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class JwtAuthenticationException extends Exception {
    private HttpStatus httpStatus;
    public JwtAuthenticationException(String message) {
        super(message);
    }
    public JwtAuthenticationException(String msg, HttpStatus httpStatus) {
        super(msg);
        this.httpStatus = httpStatus;
    }
}
