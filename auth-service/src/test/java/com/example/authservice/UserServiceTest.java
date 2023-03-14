package com.example.authservice;

import com.example.authservice.user.*;
import com.example.authservice.utils.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class UserServiceTest {
    @Mock
    public UserRepository userRepository;

    @Mock
    public PasswordEncoder passwordEncoder;

    public UserService userService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, passwordEncoder);
        User user = new User();
        user.setUsername("Test");
        user.setAccountEnabled(true);
        user.setRole(Role.USER);
        user.setAccountNonLocked(true);
        user.setPassword("testPass");
        user.setAccountNonExpired(true);
        user.setCreatedDate(LocalDateTime.now());
        user.setEmail("test@gmail.com");
        when(userRepository.findUserByEmail("test@gmail.com")).thenReturn(Optional.of(user));
    }

    @Test
    public void getUserByEmail_successful(){
       User user = userService.getUserByEmail("test@gmail.com");
       assertNotNull(user);
       assertEquals("Test", user.getUsername());
       verify(userRepository, times(1)).findUserByEmail("test@gmail.com");
    }

    @Test
    public void getUserByEmail_butUserNotExist(){
        when(userRepository.findUserByEmail("test@gmail.com")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, ()->{
            User user = userService.getUserByEmail("test@gmail.com");
        });
    }

    @Test
    public void saveUser(){
        UserDto userDto = new UserDto();
        userDto.setUsername("test");
        userService.saveUser(userDto);
        verify(userRepository, times(1)).save(notNull());
    }
}
