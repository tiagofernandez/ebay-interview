package com.ebay.epd.sudoku.game;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SudokuService {
    @Autowired
    private BoardLogic boardLogic;

    private final Map<String, Board> boards = new HashMap<>();

    public Board getNewBoard() {
        Board newBoard = boardLogic.generateBoard();
        boards.put(newBoard.getId(), newBoard);
        return newBoard;
    }

    public Board getBoard(String id) {
        return boards.get(id);
    }

    public void validateBoard(Board b) throws SudokuValidationException {
        BoardState state = boardLogic.isValid(b);
        b.setState(state);
        if (state == BoardState.COMPLETED) {
            b.setDealsLink("https://www.ebay.co.uk/deals");
        }
        boards.put(b.getId(), b);
    }
}
