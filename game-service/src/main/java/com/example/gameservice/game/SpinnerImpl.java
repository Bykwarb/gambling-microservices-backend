package com.example.gameservice.game;

import com.example.gameservice.game.utils.Symbols;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SpinnerImpl implements Spinner{
    
    @Override
    public Symbols[][] spin() {
        List<Symbols> symbolsList = new ArrayList<>();
        for (int i = 0; i < 9; i++){
            symbolsList.add(Symbols.SEVEN);
            symbolsList.add(Symbols.LEMON);
            symbolsList.add(Symbols.BELLS);
            symbolsList.add(Symbols.BAR);
            symbolsList.add(Symbols.BAR);
            symbolsList.add(Symbols.CHERRY);
            symbolsList.add(Symbols.CHERRY);
        }
        Collections.shuffle(symbolsList);
        Symbols[][] result = new Symbols[3][3];
        for (int i = 0; i < 3; i++){
            Random random = new Random();
            for (int j = 0; j < 3; j++){
                result[i][j] = symbolsList.get(random.nextInt(63));
            }
        }
        return result;
    }
}
