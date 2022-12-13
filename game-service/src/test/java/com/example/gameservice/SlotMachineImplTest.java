package com.example.gameservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SlotMachineImplTest {
    @Autowired
    SlotMachine slotMachine;
    @Test
    void play() {
       int count = 0;
       Session session = new Session();
       session.setLines(3);
       session.setCredits(1000);
       session.setBetId(1);
       session.setBetterId(1);
       session.setBetValue(5);
       for (int i = 0; i < 1000; i++){
           session = slotMachine.play(session);
           System.out.println(session.toString());
       }
    }
}