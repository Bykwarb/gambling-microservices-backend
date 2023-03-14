package com.example.authservice;

import com.example.authservice.security.jwt.JwtTokenProvider;
import com.example.authservice.user.User;
import com.example.authservice.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
public class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager manager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private AuthService authService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        authService = new AuthServiceImpl(manager, userService, jwtTokenProvider);
    }

    @Test
    public void successfullAuth(){
        String email = "test@gmail.com";
        String password = "testPass";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setUsername("Test");
        Authentication authentication = new TestingAuthenticationToken(email, password, "ROLE_USER");
        when(manager.authenticate(new UsernamePasswordAuthenticationToken(email, password))).thenReturn(authentication);
        when(userService.getUserByEmail(email)).thenReturn(user);
        ResponseEntity<?> response = authService.auth(email, password);
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().toString(), "{email=test@gmail.com, username=Test, token=null}");
    }

    @Test
    public void unsuccessfulAuth(){
        String email = "test@gmail.com";
        String password = "testPass";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setUsername("Test");

        when(manager.authenticate(new UsernamePasswordAuthenticationToken(email, password))).thenThrow(BadCredentialsException.class);
        ResponseEntity<?> response = authService.auth(email, password);
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
        assertEquals(response.getBody().toString(), "Failed to authenticate user");
    }
}
