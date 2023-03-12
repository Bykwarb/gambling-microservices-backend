package com.example.gameservice;

import com.example.gameservice.game.SlotMachine;
import com.example.gameservice.game.entity.Result;
import com.example.gameservice.game.entity.Session;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
public class SlotMachineTest {

    @Autowired
    private SlotMachine slotMachine;

    @Test
    public void correctWork(){
        Session session = new Session();
        session.setLines(5);
        session.setBetValue(5);
        session.setBetterUserName("Test");
        Result result = slotMachine.play(session);
        assertNotNull(result);
        assertEquals(3, result.getSymbols().length);
        assertEquals(3, result.getSymbols()[0].length);
        assertEquals(3, result.getSymbols()[1].length);
        assertEquals(3, result.getSymbols()[2].length);
    }
}
