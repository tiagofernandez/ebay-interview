package com.ebay.epd.sudoku.game;

import java.io.Serializable;

public class InvalidFieldError implements Serializable {
    private static final long serialVersionUID = -7355861616444502131L;

    private int x;
    private int y;

    public InvalidFieldError(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
