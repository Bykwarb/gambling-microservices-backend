package com.example.gameservice.game.utils;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
public enum Symbols {
    CHERRY(0),
    BAR(1),
    BELLS(2),
    LEMON(3),
    SEVEN(4);
    private final double coefficient;

    Symbols(double coefficient) {
        this.coefficient = coefficient;
    }


}
