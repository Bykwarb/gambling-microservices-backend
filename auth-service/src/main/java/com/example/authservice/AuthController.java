package com.example.authservice;

import com.example.authservice.user.UserService;
import com.example.authservice.utils.AuthenticationRequestDto;
import com.example.authservice.utils.UserDto;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/auth-service/")
public class AuthController {
    @Autowired
    private AuthService authService;
    @GetMapping("/auth")
    public ResponseEntity<?> authenticate(@RequestParam("email") String email, @RequestParam("password") String password){
         return authService.auth(email, password);
    }
    @ExceptionHandler({CallNotPermittedException.class})
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public void handleCallNotPermittedException() {
    }

}
