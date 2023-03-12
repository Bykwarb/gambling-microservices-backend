package com.example.gameservice;

import com.example.gameservice.game.utils.Checker;
import com.example.gameservice.game.utils.Lines;
import com.example.gameservice.game.utils.Symbols;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class CheckerTest {
    Symbols[][] grid = {
            { Symbols.CHERRY, Symbols.SEVEN, Symbols.LEMON },
            { Symbols.BAR, Symbols.CHERRY, Symbols.CHERRY },
            { Symbols.SEVEN, Symbols.BELLS, Symbols.CHERRY}
    };

    @Autowired
    private Checker checker;
    @Test
    public void defaultCheckTest(){
        Map<Lines, Symbols> result = checker.check(grid);
        assertEquals(result.get(Lines.RightDiagonal), Symbols.CHERRY);
    }

}
