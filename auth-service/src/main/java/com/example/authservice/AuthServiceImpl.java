package com.example.authservice;

import com.example.authservice.security.jwt.JwtTokenProvider;
import com.example.authservice.user.User;
import com.example.authservice.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService{
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Override
    public ResponseEntity<String> auth(String email, String password) {
        log.info("Auth user with credential: {}, {}", email, password);
        try {
            manager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (BadCredentialsException e) {
            log.debug("Failed to authenticate user with email {}", email);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to authenticate user");
        } catch (Exception e) {
            log.debug("Unexpected error during authentication for user with email {}", email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error during authentication");
        }
        User user = userService.getUserByEmail(email);
        String token = jwtTokenProvider.createToken(user);
        return ResponseEntity.ok(token);
    }


    @Override
    public UsernamePasswordAuthenticationToken verify(String token) {
        try {
            return jwtTokenProvider.getAuthentication(token);
        } catch (Exception e) {
            log.error("Failed to verify token {}", token, e);
            return null;
        }
    }
}
