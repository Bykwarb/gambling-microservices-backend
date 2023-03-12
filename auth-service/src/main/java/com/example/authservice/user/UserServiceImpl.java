package com.example.authservice.user;

import com.example.authservice.utils.UserDto;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeoutException;

@Service
@ComponentScan("com.example.authservice.user")
@Transactional
@Slf4j
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> getByUsrEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    @CircuitBreaker(name = "getUserByEmail", fallbackMethod = "failUser")
    @Bulkhead(name= "bulkAuth", fallbackMethod= "failUser")

    public User getUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.findUserByEmail(email);
        if (optionalUser.isPresent()){
         return optionalUser.get();
        }else {
            throw new UsernameNotFoundException("Username not found");
        }
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setRole(Role.USER);
        user.setAccountEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        userRepository.save(user);
    }
    private User failUser(String email, Throwable t){
        User user = new User();
        user.setAccountEnabled(false);
        user.setAccountNonExpired(false);
        user.setAccountNonLocked(false);
        return user;
    }
}
