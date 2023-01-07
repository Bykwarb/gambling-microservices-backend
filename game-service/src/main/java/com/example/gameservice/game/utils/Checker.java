package com.example.gameservice.game.utils;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface Checker {
    public Map<Lines, Symbols> check(Symbols[][] symbols);
}
