package com.example.userservice.kafka;

import com.example.userservice.entities.GameDTO;
import com.example.userservice.entities.UserEntity;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.entities.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class GameEventHandler {
    private Logger logger = LoggerFactory.getLogger(GameEventHandler.class);

    private final UserRepository userRepository;

    public GameEventHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @KafkaListener(topics = "game-message")
    public void GameHistoryMessageReceive(@Payload GameDTO gameDTO, @Header(KafkaHeaders.RECEIVED_KEY) String key){
        Optional<UserEntity> optionalUser = userRepository.findById(gameDTO.getUserId());
        if (optionalUser.isEmpty()){
            logger.debug("Received message from game-service. User not found. User-id: {}. Correlation-id: {}. Game-id: {}.", gameDTO.getUserId(), key, gameDTO.getGameId());
        }else {
            logger.debug("Received message from game-service. Game: {}. Correlation-id: {}.", gameDTO, key);
            UserEntity user = optionalUser.get();
            Game game = new Game();
            game.setGameId(gameDTO.getGameId());
            game.setResult(gameDTO.getResult());
            game.setBet(gameDTO.getBet());
            game.setStatus(gameDTO.getStatus());
            game.setUserId(user.getUserId());
            game.setDate(gameDTO.getLocalDateTime());
            user.getHistory().add(game);
            userRepository.save(user);
        }
    }
}
