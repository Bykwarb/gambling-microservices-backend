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
public class GameEventListener implements ApplicationListener<GameEvent> {
    @Autowired
    private KafkaTemplate<String, GameDTO> kafkaTemplate;
    private Logger logger = LoggerFactory.getLogger(GameEventListener.class);
    @Override
    public void onApplicationEvent(GameEvent event) {
        GameDTO gameDTO = new GameDTO();
        Result result = event.getResult();
        Session session = event.getSession();
        gameDTO.setGameId(session.getGameId());
        gameDTO.setBet(session.getBetValue());
        gameDTO.setUserName(session.getBetterUserName());
        gameDTO.setStatus(result.getStatus());
        gameDTO.setResult(result.getResult());
        gameDTO.setLocalDateTime(LocalDateTime.now());
        logger.debug("Time: {}", gameDTO.getLocalDateTime());
        logger.debug("Sending message to Kafka. GameDTO: {}. Correlation-id: {}", gameDTO, ClientContextHolder.getContext().getCorrelationId());
        kafkaTemplate.send(event.getTopic(), ClientContextHolder.getContext().getCorrelationId(), gameDTO);
    }
}
