package com.example.gameservice;

import lombok.Getter;

@Getter
public enum Lines {
    One(8),
    Two(8),
    Three(8),
    RightDiagonal(8),
    LeftDiagonal(8);
    private final double coefficient;
    Lines(double coefficient) {
        this.coefficient = coefficient;
    }

}
