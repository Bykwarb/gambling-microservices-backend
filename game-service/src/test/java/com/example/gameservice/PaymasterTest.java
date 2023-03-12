package com.example.gameservice;

import com.example.gameservice.game.entity.Result;
import com.example.gameservice.game.entity.Session;
import com.example.gameservice.game.utils.Lines;
import com.example.gameservice.game.utils.Paymaster;
import com.example.gameservice.game.utils.Symbols;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;


@SpringBootTest
public class PaymasterTest {
    @Autowired
    private Paymaster paymaster;
    @Test
    public void defaultCalculate(){
        Map<Lines, Symbols> resultMap = new HashMap<>();
        resultMap.put(Lines.First, Symbols.CHERRY);
        Session session = new Session();
        session.setBetValue(5.0);
        session.setLines(1);
        Result result = paymaster.calculateResult(resultMap, session);
        assertEquals(result.getResult(), 75.0, 0);
        assertEquals(result.getStatus(), Result.Status.Win);
    }

    @Test
    public void calculateWinLineButNumberOfLinesInSessionLessThanLinesNumber(){
        Map<Lines, Symbols> resultMap = new HashMap<>();
        resultMap.put(Lines.Third, Symbols.SEVEN);
        Session session = new Session();
        session.setBetValue(5.0);
        session.setLines(1);
        Result result = paymaster.calculateResult(resultMap, session);
        assertEquals(result.getResult(), 0, 0);
        assertEquals(result.getStatus(), Result.Status.Lose);
    }

}
