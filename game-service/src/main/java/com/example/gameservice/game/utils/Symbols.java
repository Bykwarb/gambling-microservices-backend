package com.example.gameservice.game.utils;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
public enum Symbols {
    CHERRY(1),
    BAR(2),
    BELLS(3),
    LEMON(4),
    SEVEN(5);
    private final double coefficient;

    Symbols(double coefficient) {
        this.coefficient = coefficient;
    }


}
