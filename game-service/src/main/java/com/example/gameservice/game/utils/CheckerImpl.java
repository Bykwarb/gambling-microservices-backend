package com.example.gameservice.game.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
@Component
public class CheckerImpl implements Checker {

    @Override
    public Map<Lines, Symbols> check(Symbols[][] symbols) {
        Map<Lines, Symbols> result = new HashMap<>();
        checkRightDiagonal(symbols, result);
        checkLeftDiagonal(symbols, result);
        checkFirstLine(symbols, result);
        checkSecondLine(symbols, result);
        checkThirdLine(symbols, result);
        return result;
    }

    private Map<Lines, Symbols> checkFirstLine(Symbols[][] symbols, Map<Lines, Symbols> result){
        if (symbols[0][0] == symbols[0][1] && symbols[0][0] == symbols[0][2]) {
            result.put(Lines.First, symbols[0][0]);
        }
        return result;
    }
    private Map<Lines, Symbols> checkSecondLine(Symbols[][] symbols, Map<Lines, Symbols> result){
        if (symbols[1][0] == symbols[1][1] && symbols[1][0] == symbols[1][2]){
            result.put(Lines.Second, symbols[1][0]);
        }
        return result;
    }
    private Map<Lines, Symbols> checkThirdLine(Symbols[][] symbols, Map<Lines, Symbols> result){
        if (symbols[2][0] == symbols[2][1] && symbols[2][0] == symbols[2][2]){
            result.put(Lines.Third, symbols[2][0]);
        }
        return result;
    }
    private Map<Lines, Symbols> checkRightDiagonal(Symbols[][] symbols, Map<Lines, Symbols> result){
        if(symbols[0][0] == symbols[1][1] && symbols[0][0] == symbols[2][2]){
            result.put(Lines.RightDiagonal, symbols[0][0]);
        }
        return result;
    }
    private Map<Lines, Symbols> checkLeftDiagonal(Symbols[][] symbols, Map<Lines, Symbols> result){
        if (symbols[0][2] == symbols[1][1] && symbols[0][2] == symbols[2][0]){
            result.put(Lines.LeftDiagonal, symbols[0][2]);
        }
        return result;
    }
}
