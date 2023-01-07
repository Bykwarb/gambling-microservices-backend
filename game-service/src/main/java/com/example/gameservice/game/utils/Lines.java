package com.example.gameservice.game.utils;

import lombok.Getter;

@Getter
public enum Lines {
    First(8, 1),
    Second(8, 2),
    Third(8, 3),
    RightDiagonal(8, 4),
    LeftDiagonal(8, 5);
    private final double coefficient;
    private final int indexNumber;
    Lines(double coefficient, int indexNumber) {
        this.coefficient = coefficient;
        this.indexNumber = indexNumber;
    }

}
