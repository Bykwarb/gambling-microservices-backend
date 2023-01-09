package com.example.gameservice;

import com.example.gameservice.game.Result;
import com.example.gameservice.game.Session;
import com.example.gameservice.game.SlotMachine;
import com.example.gameservice.utils.GameContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    @Autowired
    private SlotMachine slotMachine;
    private Logger logger = LoggerFactory.getLogger(GameService.class);
    public Result checkYourLuck(Session session){
        if (session.getCredits() < session.getBetValue()){
            Result result = new Result();
            result.setStatus(Result.Status.NotEnoughMoney);
            result.setSession(session);
            logger.debug("Not enough money. Correlation-id: {}.", GameContextHolder.getContext().getCorrelationId());
            return result;
        }
        if (session.getLines() > 5 || session.getLines() < 1){
            Result result = new Result();
            result.setStatus(Result.Status.IncorrectLinesNumber);
            result.setSession(session);
            logger.debug("Incorrect lines number. Correlation-id: {}.", GameContextHolder.getContext().getCorrelationId());
            return result;
        }
        session.setCredits(session.getCredits() - session.getBetValue());
        return slotMachine.play(session);
    }
}
