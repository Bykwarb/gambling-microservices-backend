package com.example.gameservice;

import com.example.gameservice.game.Spinner;
import com.example.gameservice.game.utils.Symbols;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@SpringBootTest
public class SpinnerTest {

    @Autowired
    private Spinner spinner;

    @Test
    void spin_ReturnsValidGrid() {
        Symbols[][] grid = spinner.spin();
        assertNotNull(grid);
        assertEquals(3, grid.length);
        assertEquals(3, grid[0].length);
        assertEquals(3, grid[1].length);
        assertEquals(3, grid[2].length);
    }

    @Test
    void spin_ReturnsRandomGrids() {
        Symbols[][] grid1 = spinner.spin();
        Symbols[][] grid2 = spinner.spin();
        assertFalse(Arrays.deepEquals(grid1, grid2));
    }

    @Test
    void spin_ReturnsGridWithValidSymbols() {
        Symbols[][] grid = spinner.spin();
        Set<Symbols> allowedSymbols = new HashSet<>(Arrays.asList(
                Symbols.SEVEN, Symbols.LEMON, Symbols.BELLS, Symbols.BAR, Symbols.CHERRY
        ));
        for (Symbols[] row : grid) {
            for (Symbols symbol : row) {
                assertTrue(allowedSymbols.contains(symbol));
            }
        }
    }
}
