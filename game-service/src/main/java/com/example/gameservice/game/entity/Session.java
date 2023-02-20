package com.example.gameservice.game.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Session {
    private String gameId;
    private double betValue;
    private int lines;
    private double credits;
    private String betterUserName;
}
