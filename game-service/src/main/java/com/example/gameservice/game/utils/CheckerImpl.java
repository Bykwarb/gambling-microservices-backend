package com.example.gameservice.game.utils;

import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class CheckerImpl implements Checker {

    @Override
    public Map<Lines, Symbols> check(Symbols[][] symbols) {
        Map<Lines, Symbols> result = new EnumMap<>(Lines.class);
        checkLine(symbols, result, Lines.First, 0);
        checkLine(symbols, result, Lines.Second, 1);
        checkLine(symbols, result, Lines.Third, 2);
        checkDiagonal(symbols, result, Lines.RightDiagonal, true);
        checkDiagonal(symbols, result, Lines.LeftDiagonal, false);
        return result;
    }

    private void checkLine(Symbols[][] symbols, Map<Lines, Symbols> result, Lines line, int row) {
        if (symbols[row][0] == symbols[row][1] && symbols[row][0] == symbols[row][2]) {
            result.put(line, symbols[row][0]);
        }
    }

    private void checkDiagonal(Symbols[][] symbols, Map<Lines, Symbols> result, Lines diagonal, boolean isRight) {
        if ((isRight && symbols[0][0] == symbols[1][1] && symbols[0][0] == symbols[2][2])
                || (!isRight && symbols[0][2] == symbols[1][1] && symbols[0][2] == symbols[2][0])) {
            result.put(diagonal, symbols[1][1]);
        }
    }
}
