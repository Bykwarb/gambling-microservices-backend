package com.example.gameservice.events;

import com.example.gameservice.game.entity.GameDTO;
import com.example.gameservice.game.entity.Result;
import com.example.gameservice.game.entity.Session;
import com.example.userservice.utils.ClientContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class GameHistoryListener implements ApplicationListener<SendGameToGameHistoryEvent> {
    @Autowired
    private KafkaTemplate<String, GameDTO> kafkaTemplate;
    private String topicName = "game-message";
    private Logger logger = LoggerFactory.getLogger(GameHistoryListener.class);
    @Override
    public void onApplicationEvent(SendGameToGameHistoryEvent event) {
        GameDTO gameDTO = new GameDTO();
        Result result = event.getResult();
        Session session = event.getSession();
        gameDTO.setGameId(session.getGameId());
        gameDTO.setBet(session.getBetValue());
        gameDTO.setUserId(session.getBetterId());
        gameDTO.setStatus(result.getStatus());
        gameDTO.setResult(result.getResult());
        gameDTO.setLocalDateTime(LocalDateTime.now());
        logger.debug("Time: {}", gameDTO.getLocalDateTime());
        logger.debug("Sending message to Kafka. GameDTO: {}. Correlation-id: {}", gameDTO, ClientContextHolder.getContext().getCorrelationId());
        kafkaTemplate.send(topicName, ClientContextHolder.getContext().getCorrelationId(), gameDTO);
    }
}
