package com.example.authservice.kafka;

import com.example.authservice.user.Role;
import com.example.authservice.user.User;
import com.example.authservice.user.UserRepository;
import com.example.authservice.utils.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class UserKafkaHandler {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @KafkaListener(topics = "user-message")
    public void GameHistoryMessageReceive(@Payload UserDto userDto, @Header(KafkaHeaders.RECEIVED_KEY) String key){
        log.debug("Received message from user-service. User: {}. Correlation-id: {}.", userDto, key);
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(Role.USER);
        user.setAccountEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        userRepository.save(user);
    }
}
