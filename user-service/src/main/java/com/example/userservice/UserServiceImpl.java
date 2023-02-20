package com.example.userservice;

import com.example.userservice.event.SendRequestToCreateWalletEvent;
import com.example.userservice.exception.UserAlreadyExistException;
import com.example.userservice.exception.UserNotFoundedException;
import com.example.userservice.event.SendUserToAuthServiceEvent;
import com.example.userservice.utils.ClientContextHolder;
import com.example.userservice.entities.UserDTO;
import com.example.userservice.entities.UserEntity;
import com.example.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("UserService")
public class UserServiceImpl implements UserService{
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Override
    public void createUser(UserDTO userDTO) throws UserAlreadyExistException {
        try {
            UserEntity user = new UserEntity();
            user.setName(userDTO.getUsername());
            userRepository.save(user);
        }catch (Exception e){
            throw new UserAlreadyExistException("User already exist");
        }
        logger.debug("Publish event, userDto {}", userDTO);
        publisher.publishEvent(new SendUserToAuthServiceEvent(userDTO));
        logger.debug("Publish event, userName {}", userDTO);
        publisher.publishEvent(new SendRequestToCreateWalletEvent(userDTO.getUsername()));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserEntity getUserByUserName(String username) {
        return userRepository.getUserEntityByName(username).get();
    }

    @Override
    public UserEntity getUserById(Long id) throws UserNotFoundedException {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()){
            logger.debug("User not found. Correlation-id: {}. User id: {}", ClientContextHolder.getContext().getCorrelationId(), id);
            throw new UserNotFoundedException("User not found");
        }
        return optionalUser.get();
    }



}
