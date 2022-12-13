package com.example.gameservice;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Session {
    private int betId;
    private double betValue;
    private int lines;
    private double credits;
    private long betterId;
}
