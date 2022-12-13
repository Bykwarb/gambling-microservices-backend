package com.example.gameservice;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SpinnerImplTest {
    SpinnerImpl spinner = new SpinnerImpl();
    SlotMachineImpl slotMachine = new SlotMachineImpl();
    @Test
    void spin() {
        int counter = 0;
        for (int r = 0; r < 10000; r++){
            Symbols[][] symbols = spinner.spin();
            if (!slotMachine.calculateTotals(symbols).isEmpty()){
                counter++;
            }
        }
        System.out.println(counter);
    }
}