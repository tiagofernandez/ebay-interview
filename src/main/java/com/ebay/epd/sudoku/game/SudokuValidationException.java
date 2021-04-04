package com.ebay.epd.sudoku.game;

import java.util.LinkedList;
import java.util.List;

public class SudokuValidationException extends Exception {
    private static final long serialVersionUID = 2796067722006700351L;

    private final LinkedList<InvalidFieldError> errors;

    public SudokuValidationException(List<InvalidFieldError> errors) {
        this.errors = new LinkedList<>(errors);
    }

    public List<InvalidFieldError> getErrors() {
        return errors;
    }
}
