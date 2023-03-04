package com.example.gameservice.game;

import com.example.gameservice.game.utils.Symbols;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SpinnerImpl implements Spinner{
    private static final int GRID_SIZE = 3;
    private static final int SYMBOLS_PER_ROW = 3;
    private static final int SYMBOLS_PER_COL = 3;
    private static final int SYMBOLS_COUNT = 63;
    private static final int SYMBOLS_PER_SPIN = SYMBOLS_PER_ROW * SYMBOLS_PER_COL;

    @Override
    public Symbols[][] spin() {
        final List<Symbols> SYMBOLS_POOL = Arrays.asList(
                Symbols.SEVEN, Symbols.LEMON, Symbols.BELLS, Symbols.BAR, Symbols.BAR, Symbols.CHERRY, Symbols.CHERRY
        );
        final List<Symbols> symbolsList = new ArrayList<>();
        for (int i = 0; i < SYMBOLS_PER_SPIN; i++){
            symbolsList.addAll(SYMBOLS_POOL);
        }
        Collections.shuffle(symbolsList);
        Symbols[][] result = new Symbols[GRID_SIZE][GRID_SIZE];
        Random random = new Random();
        for (int i = 0; i < GRID_SIZE; i++){
            for (int j = 0; j < GRID_SIZE; j++){
                result[i][j] = symbolsList.get(random.nextInt(SYMBOLS_COUNT));
            }
        }
        return result;
    }
}
