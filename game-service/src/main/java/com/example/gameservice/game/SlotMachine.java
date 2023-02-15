package com.example.gameservice.game;

import com.example.gameservice.game.entity.Result;
import com.example.gameservice.game.entity.Session;

public interface SlotMachine {
    Result play(Session session);
}
