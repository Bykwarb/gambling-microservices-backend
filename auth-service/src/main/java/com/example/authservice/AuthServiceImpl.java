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

import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<?> auth(String email, String password) {
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
        Map<String, String> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("token", token);
        return ResponseEntity.ok(response);
    }


}
