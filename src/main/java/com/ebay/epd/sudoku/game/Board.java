package com.ebay.epd.sudoku.game;

import javax.validation.constraints.NotNull;

public class Board {
    @NotNull
    private String id;

    @NotNull
    private Integer[][] fields;

    private String dealsLink;
    private BoardState state = BoardState.VALID;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer[][] getFields() {
        return this.cloneGrid(fields);
    }

    public void setFields(Integer[][] fields) {
        this.fields = this.cloneGrid(fields);
    }

    public void setState(BoardState state) {
        this.state = state;
    }

    public BoardState getState() {
        return state;
    }

    public String getDealsLink() {
        return dealsLink;
    }

    public void setDealsLink(String dealsLink) {
        this.dealsLink = dealsLink;
    }

    private Integer[][] cloneGrid(Integer[][] grid) {
        if (grid == null) {
            return null;
        }
        Integer[][] clone = new Integer[grid.length][];
        for (int i = 0; i < grid.length; ++i) {
            clone[i] = new Integer[grid[i].length];
            System.arraycopy(grid[i], 0, clone[i], 0, clone[i].length);
        }
        return clone;
    }
}
