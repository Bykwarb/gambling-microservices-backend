package com.example.userservice;

import com.example.userservice.event.SendRequestToCreateWalletEvent;
import com.example.userservice.exception.UserAlreadyExistException;
import com.example.userservice.exception.UserNotFoundedException;
import com.example.userservice.event.SendUserToAuthServiceEvent;
import com.example.userservice.utils.ClientContextHolder;
import com.example.userservice.entities.UserDTO;
import com.example.userservice.entities.UserEntity;
import com.example.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("UserService")
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final ApplicationEventPublisher publisher;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, ApplicationEventPublisher publisher) {
        this.userRepository = userRepository;
        this.publisher = publisher;
    }


    @Override
    public void createUser(UserDTO userDTO) throws UserAlreadyExistException {
        if (userRepository.existsByName(userDTO.getUsername())) {
            throw new UserAlreadyExistException("User already exists");
        }
        UserEntity user = new UserEntity();
        user.setName(userDTO.getUsername());
        userRepository.save(user);
        publisher.publishEvent(new SendUserToAuthServiceEvent(userDTO));
        publisher.publishEvent(new SendRequestToCreateWalletEvent(userDTO.getUsername()));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserEntity getUserByUserName(String username) {
        return userRepository.getUserEntityByName(username).orElse(null);
    }

    @Override
    public UserEntity getUserById(Long id) throws UserNotFoundedException {
        return userRepository.findById(id).orElseThrow(() -> {
            log.debug("User not found. Correlation-id: {}. User id: {}", ClientContextHolder.getContext().getCorrelationId(), id);
            return new UserNotFoundedException("User not found");
        });
    }



}
