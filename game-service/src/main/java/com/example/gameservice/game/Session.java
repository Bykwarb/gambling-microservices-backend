package com.example.gameservice.game;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Session {
    private int gameId;
    private double betValue;
    private int lines;
    private double credits;
    private long betterId;
}
