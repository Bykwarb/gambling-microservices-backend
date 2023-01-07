package com.example.gameservice;

import com.example.gameservice.game.Result;
import com.example.gameservice.game.Session;
import com.example.gameservice.game.SlotMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    @Autowired
    private SlotMachine slotMachine;

    public Result checkYourLuck(Session session){
        if (session.getCredits() < session.getBetValue()){
            Result result = new Result();
            result.setStatus(Result.Status.NotEnoughMoney);
            result.setSession(session);
            return result;
        }
        session.setCredits(session.getCredits() - session.getBetValue());
        return slotMachine.play(session);
    }
}
