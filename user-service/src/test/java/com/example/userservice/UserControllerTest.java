package com.example.userservice;

import com.example.userservice.entities.UserDTO;
import com.example.userservice.entities.UserEntity;
import com.example.userservice.exception.UserAlreadyExistException;
import com.example.userservice.exception.UserNotFoundedException;
import com.example.userservice.utils.ClientContextHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    private final String username = "testuser";
    private final String url = "/v1/user-service/";

    private final String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCeWt3YXJiIiwiZW1haWwiOiJuZWJ5a3dhcmJAZ21haWwuY29tIiwicm9sZSI6IlVTRVIiLCJrZXkiOiIkMmEkMTAkSVNEalNNdkIvQThhcGxBUFRqR1FlLmZ4N1RYSnloNWU2cUVLTi5GSk40bTdVMC56dGJ4SXkiLCJpYXQiOjE2Nzg1NjQyMzcsImV4cCI6MTY3ODY1MDYzN30.Hv8nr3Zu1ZlG9W0PipCAyGN4dkulu57WzCCmioIVSu0";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUser_shouldReturnOk() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        doNothing().when(userService).createUser(any(UserDTO.class));
        mockMvc.perform(MockMvcRequestBuilders.post(url + "create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"User successfully created\"}"));
        verify(userService, times(1)).createUser(any(UserDTO.class));
    }

    @Test
    void createUser_shouldReturnConflict() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);

        doThrow(new UserAlreadyExistException("User already exists"))
                .when(userService).createUser(any(UserDTO.class));

        mockMvc.perform(MockMvcRequestBuilders.post(url + "create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"User already exist\"}"));

        verify(userService, times(1)).createUser(any(UserDTO.class));
    }

    @Test
    void deleteUser_shouldReturnOk() throws Exception {
        Long userId = 1L;
        doNothing().when(userService).deleteUser(anyLong());
        mockMvc.perform(MockMvcRequestBuilders.delete(url + "delete/id/{userId}", userId).header("Authorization", jwt))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"User successfully deleted\"}"));

        verify(userService, times(1)).deleteUser(anyLong());
    }

    @Test
    void getUserById_shouldReturnOk() throws Exception {
        Long userId = 1L;

        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);

        when(userService.getUserById(anyLong())).thenReturn(userEntity);

        mockMvc.perform(MockMvcRequestBuilders.get(url + "get/id/{userId}", userId).header("Authorization", jwt))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(userId));

        verify(userService, times(1)).getUserById(anyLong());
    }

    @Test
    void getUserById_shouldReturnNotFound() throws Exception {
        Long userId = 1L;

        when(userService.getUserById(anyLong())).thenThrow(new UserNotFoundedException("User not found"));

        mockMvc.perform(MockMvcRequestBuilders.get(url + "get/id/{userId}", userId).header("Authorization", jwt))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"User not found\"}"));

        verify(userService, times(1)).getUserById(anyLong());
    }

    @Test
    void getUserByName_shouldReturnOk() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(username);
        when(userService.getUserByUserName(anyString())).thenReturn(userEntity);

        mockMvc.perform(MockMvcRequestBuilders.get(url + "get/username/{username}", username).header("Authorization", jwt))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(username));

        verify(userService, times(1)).getUserByUserName(anyString());
    }

    @Test
    void getUserByName_shouldReturnNotFound() throws Exception {
        when(userService.getUserByUserName(anyString())).thenThrow(new UserNotFoundedException("User not found"));

        mockMvc.perform(MockMvcRequestBuilders.get(url + "get/username/{username}", username).header("Authorization", jwt))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"User not found\"}"));

        verify(userService, times(1)).getUserByUserName(anyString());
    }
}
