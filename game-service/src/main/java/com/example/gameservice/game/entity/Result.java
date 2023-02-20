package com.example.gameservice.game.entity;

import com.example.gameservice.game.utils.Lines;
import com.example.gameservice.game.utils.Symbols;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class Result {
    private double result;
    private Status status;
    private Symbols[][] symbols;
    private Map<Lines, Symbols> winLines;

    public enum Status{
        Win,
        Lose,
        NotEnoughMoney,
        IncorrectLinesNumber,
        ServiceUnavailable
    }
}
