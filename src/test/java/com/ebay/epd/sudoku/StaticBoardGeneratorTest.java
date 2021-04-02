package com.ebay.epd.sudoku;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import com.ebay.epd.sudoku.game.Board;
import com.ebay.epd.sudoku.game.BoardLogic;
import org.junit.Before;
import org.junit.Test;

public class StaticBoardGeneratorTest {
    private BoardLogic g;

    @Before
    public void setup() {
        g = new BoardLogic();
    }

    @Test
    public void shouldGenerateBoard() {
        Board b = g.generateBoard();
        assertNotNull(b);
    }

    @Test
    public void shouldBe9x9() {
        Board b = g.generateBoard();
        Integer[][] fields = b.getFields();
        assertThat(fields.length, is(9));
        for (int i = 0; i < fields.length; i++) {
            Integer[] row = fields[i];
            assertThat(row.length, is(9));
        }
    }

    @Test
    public void shouldHaveContent() {
        Board b = g.generateBoard();
        Integer[][] fields = b.getFields();
        assertNotNull(fields);

        boolean hasContent = false;
        for (int i = 0; i < fields.length; ++i) for (int j = 0; j < fields[i].length; ++j) hasContent =
            hasContent || (fields[i][j] != null);
        assertTrue(hasContent);
    }
}
