package com.example.authservice;

import com.example.authservice.security.jwt.JwtTokenProvider;
import com.example.authservice.user.User;
import com.example.authservice.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService{
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    @Qualifier("AuthUserService")
    private UserService userService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Override
    public ResponseEntity<String> auth(String email, String password) {
        log.debug("Before manager.auth");
        manager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        log.debug("After manager.auth");
        User user = userService.getUserByEmail(email);
        String token = jwtTokenProvider.createToken(user);
        return ResponseEntity.ok(token);
    }

    @Override
    public UsernamePasswordAuthenticationToken verify(String token) {
        return jwtTokenProvider.getAuthentication(token);
    }
}
