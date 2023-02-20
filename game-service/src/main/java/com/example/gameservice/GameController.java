package com.example.gameservice;

import com.example.gameservice.events.SendGameToGameHistoryEvent;
import com.example.gameservice.game.entity.Result;
import com.example.gameservice.game.entity.Session;
import com.example.userservice.utils.ClientContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/game/slot-machine")
public class GameController {

    private final GameService gameService;
    private Logger logger = LoggerFactory.getLogger(GameService.class);

    private final ApplicationEventPublisher publisher;

    public GameController(GameService gameService, ApplicationEventPublisher publisher) {
        this.gameService = gameService;
        this.publisher = publisher;
    }

    @PostMapping("/play")
    public ResponseEntity<Result> play(@RequestBody Session session){
        session.setGameId(UUID.randomUUID().toString());
        logger.debug("Play. Game-id {}. Correlation-id: {}.", session.getGameId(), ClientContextHolder.getContext().getCorrelationId());
        Result result = gameService.checkYourLuck(session);
        logger.debug("Result. Game-id {}. Correlation-id: {}. Result: {}.", session.getGameId(), ClientContextHolder.getContext().getCorrelationId(), result);
        publisher.publishEvent(new SendGameToGameHistoryEvent(result, session));
        return ResponseEntity.ok(result);
    }

}
