package com.example.gameservice.game;

import com.example.gameservice.game.entity.Result;
import com.example.gameservice.game.entity.Session;
import com.example.gameservice.game.utils.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SlotMachineImpl implements SlotMachine {

    private final Spinner spinner;
    private final Checker checker;
    private final Paymaster paymaster;

    public SlotMachineImpl(Spinner spinner, Checker checker, Paymaster paymaster) {
        this.spinner = spinner;
        this.checker = checker;
        this.paymaster = paymaster;
    }

    @Override
    public Result play(Session session) {
       Symbols[][] symbols = spinner.spin();
       Map<Lines, Symbols> winLines = checker.check(symbols);
       Result result = paymaster.calculateResult(winLines,session);
       result.setSymbols(symbols);
       result.setWinLines(winLines);
       return result;
    }

}
