package com.example.userservice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user-service/")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<Response> createUser(@RequestBody UserDTO userDTO){
        userService.createUser(userDTO);
        return ResponseEntity.ok(new Response("User successfully created"));
    }


    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Response> deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);
        return ResponseEntity.ok(new Response("User successfully deleted"));
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<UserEntity> getUser(@PathVariable Long userId) throws UserNotFoundedException {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @ExceptionHandler(value = {UserNotFoundedException.class})
    public ResponseEntity<Response> userNotFoundedExceptionHandler(){
        return new ResponseEntity<>(new Response("User not found"), HttpStatus.NOT_FOUND);
    }

    @AllArgsConstructor
    @Setter
    @Getter
    private class Response{
        private String message;
    }
}
