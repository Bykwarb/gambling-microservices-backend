package com.example.gameservice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Result {
    private Session session;
    private double result;
    private Status status;

    public enum Status{
        Win,
        Lose
    }
}
