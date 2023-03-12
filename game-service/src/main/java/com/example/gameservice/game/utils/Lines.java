package com.example.gameservice.game.utils;

import lombok.Getter;

@Getter
public enum Lines {
    First(1),
    Second(2),
    Third(3),
    RightDiagonal(4),
    LeftDiagonal(5);
    private final int indexNumber;
    Lines( int indexNumber) {
        this.indexNumber = indexNumber;
    }

}
