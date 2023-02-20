package com.example.authservice;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {
    ResponseEntity<String> auth(String email, String password);
    UsernamePasswordAuthenticationToken verify(String token);
}
