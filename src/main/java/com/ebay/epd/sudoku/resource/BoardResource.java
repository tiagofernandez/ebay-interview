package com.ebay.epd.sudoku.resource;

import com.ebay.epd.sudoku.game.Board;
import com.ebay.epd.sudoku.game.SudokuService;
import com.ebay.epd.sudoku.game.SudokuValidationException;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/board")
@Produces("application/json")
@Consumes("application/json")
public class BoardResource {
    @Autowired
    private SudokuService service;

    @GET
    public Board getBoard() {
        return service.getNewBoard();
    }

    @GET
    @Path("/{id}")
    public Board getBoard(@PathParam("id") String id) {
        return service.getBoard(id);
    }

    @PUT
    @Path("/validate")
    public Response validateBoard(@Valid Board b) throws SudokuValidationException {
        service.validateBoard(b);
        return Response.ok(b).build();
    }
}
