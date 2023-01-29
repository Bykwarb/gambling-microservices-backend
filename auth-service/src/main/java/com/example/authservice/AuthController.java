package com.example.authservice;

import com.example.authservice.security.SecurityUser;
import com.example.authservice.security.jwt.JwtTokenProvider;
import com.example.authservice.user.UserService;
import com.example.authservice.utils.AuthenticationRequestDto;
import com.example.authservice.utils.UserRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/auth-service/")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;
    @GetMapping("/auth")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDto request){
         return authService.auth(request.getEmail(), request.getPassword());
    }
    @GetMapping("/verify")
    public ResponseEntity<UsernamePasswordAuthenticationToken> verifyToken(@RequestParam("token") String token){
        return ResponseEntity.ok(authService.verify(token));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRequestDto userRequestDto){
        userService.saveUser(userRequestDto);
        return ResponseEntity.ok("");
    }


}
