package com.example.gameservice;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Random;

@Component
public class SpinnerImpl implements Spinner{
    @Override
    public Symbols[][] spin() {
        Symbols[] symbols = Symbols.values();
        Symbols[][] result = new Symbols[3][3];
        for (int i = 0; i < 3; i++){
            Random random = new Random();
            for (int j = 0; j < 3; j++){
                result[i][j] = symbols[random.nextInt(5)];
            }
        }
        return result;
    }
}
