package com.example.gameservice.game.utils;

import com.example.gameservice.game.Result;
import com.example.gameservice.game.Session;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Paymaster {

    public Result calculateResult(Map<Lines, Symbols> r, Session session){
        Result result = new Result();
        double sum = 0;
        if (!r.isEmpty()){
            for (Map.Entry<Lines, Symbols> entry : r.entrySet()) {
                Lines k = entry.getKey();
                Symbols v = entry.getValue();
                if (k.getIndexNumber() <= session.getLines()){
                    sum += ((session.getBetValue() + v.getCoefficient()) * k.getCoefficient());
                }
            }
        }
        if (sum != 0){
            result.setResult(sum);
            result.setStatus(Result.Status.Win);
            result.setSession(session);
        }else {
            sum -= session.getBetValue();
            result.setResult(sum);
            result.setStatus(Result.Status.Lose);
            result.setSession(session);
        }
        return result;
    }
}
