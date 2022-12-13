package com.example.gameservice;

import java.util.Map;

public interface SlotMachine {
    Session play(Session session);
    Map<Lines, Symbols> calculateTotals(Symbols[][] symbols);
}
