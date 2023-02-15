package com.example.userservice;

import com.example.userservice.exception.UserAlreadyExistException;
import com.example.userservice.exception.UserNotFoundedException;
import com.example.userservice.utils.ClientContextHolder;
import com.example.userservice.entities.UserDTO;
import com.example.userservice.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user-service/")
public class UserController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Qualifier("UserService")
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<Response> createUser(@RequestBody UserDTO userDTO) throws UserAlreadyExistException {
        userService.createUser(userDTO);
        logger.debug("Create user. Correlation-id: {}", ClientContextHolder.getContext().getCorrelationId());
        return ResponseEntity.ok(new Response("User successfully created"));
    }


    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Response> deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);
        logger.debug("Delete user. Correlation-id: {}", ClientContextHolder.getContext().getCorrelationId());
        return ResponseEntity.ok(new Response("User successfully deleted"));
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<UserEntity> getUser(@PathVariable Long userId) throws UserNotFoundedException {
        logger.debug("Get user. Correlation-id: {}. User id: {}", ClientContextHolder.getContext().getCorrelationId(), userId);
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @ExceptionHandler(value = {UserNotFoundedException.class})
    public ResponseEntity<Response> userNotFoundedExceptionHandler(){
        return new ResponseEntity<>(new Response("User not found"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = UserAlreadyExistException.class)
    public ResponseEntity<Response> userExistHandler(){
        return new ResponseEntity<>(new Response("User already exist"), HttpStatus.FOUND);
    }

    @AllArgsConstructor
    @Setter
    @Getter
    private class Response{
        private String message;
    }
}
