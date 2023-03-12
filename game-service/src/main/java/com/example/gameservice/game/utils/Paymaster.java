package com.example.gameservice.game.utils;

import com.example.gameservice.game.entity.Result;
import com.example.gameservice.game.entity.Session;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Paymaster {

    public Result calculateResult(Map<Lines, Symbols> resultMap, Session session) {
        Result result = new Result();
        double resultValue = 0;
        if (!resultMap.keySet().isEmpty()) {
            for (Map.Entry<Lines, Symbols> entry : resultMap.entrySet()) {
                Lines line = entry.getKey();
                Symbols symbol = entry.getValue();
                if (line.getIndexNumber() <= session.getLines()) {
                    resultValue += (session.getBetValue() * symbol.getCoefficient() * 15);
                }
            }
        }
        // Decrease resultValue if lines > 1 for 20% per lines up to 80%
        if (session.getLines() > 1 && session.getLines() <= 5) {
            resultValue *= (6 - session.getLines()) / 5.0;
        }
        result.setResult(resultValue);
        result.setStatus(resultValue != 0 ? Result.Status.Win : Result.Status.Lose);
        return result;
    }
}
