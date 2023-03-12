package com.example.userservice;

import com.example.userservice.entities.UserDTO;
import com.example.userservice.entities.UserEntity;
import com.example.userservice.exception.UserAlreadyExistException;
import com.example.userservice.exception.UserNotFoundedException;
import com.example.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;


public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, eventPublisher);
        UserEntity user = new UserEntity();
        user.setUserId(1l);
        user.setName("testuser");
        when(userRepository.getUserEntityByName("testuser")).thenReturn(Optional.of(user));
        when(userRepository.findById(1l)).thenReturn(Optional.of(user));
    }

    @Test
    public void testCreateUser() throws UserAlreadyExistException {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        when(userRepository.existsByName(userDTO.getUsername())).thenReturn(false);
        userService.createUser(userDTO);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void testCreateUserWithExistingUser() throws UserAlreadyExistException {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        when(userRepository.existsByName(userDTO.getUsername())).thenReturn(true);
        assertThrows(UserAlreadyExistException.class, () -> userService.createUser(userDTO));
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void createUser_shouldPublishEvents() throws UserAlreadyExistException {
        String username = "testuser";
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        when(userRepository.existsByName(username)).thenReturn(false);
        userService.createUser(userDTO);
        verify(eventPublisher, times(2)).publishEvent(any());
    }

    @Test
    public void deleteUser(){
        userService.deleteUser(1l);
        verify(userRepository, times(1)).deleteById(1l);
    }

    @Test
    public void getUserByUsername() throws UserNotFoundedException {
        String username = "testuser";
        UserEntity user = userService.getUserByUserName(username);
        Long id = 1l;
        verify(userRepository, times(1)).getUserEntityByName(username);
        assertEquals(id, user.getUserId());
    }

    @Test
    public void getUserById() throws UserNotFoundedException {
        String username = "testuser";
        Long id = 1l;
        UserEntity user = userService.getUserById(id);
        verify(userRepository, times(1)).findById(1l);
        assertEquals(username, user.getName());
    }

    @Test
    public void getUserByUsernameButUserDoesntExist(){
        String username = "testuser1";
        assertThrows(UserNotFoundedException.class, () -> {
            userService.getUserByUserName(username);
        });
        verify(userRepository, times(1)).getUserEntityByName(username);
    }
    @Test
    public void getUserByIdButUserDoesntExist(){
        Long userId = 2l;
        assertThrows(UserNotFoundedException.class, () -> {
            userService.getUserById(userId);
        });
        verify(userRepository, times(1)).findById(userId);
    }
}
