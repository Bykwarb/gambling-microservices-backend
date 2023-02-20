package com.example.gameservice.game.utils;

import com.example.gameservice.game.entity.Result;
import com.example.gameservice.game.entity.Session;
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
        }else {
            result.setResult(sum);
            result.setStatus(Result.Status.Lose);
        }
        return result;
    }
}
