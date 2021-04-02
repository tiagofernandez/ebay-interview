package com.ebay.epd.sudoku;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import com.ebay.epd.sudoku.game.Board;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MultiValueMap;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
//@WebAppConfiguration
public class BoardTests {
    @Autowired
    private TestRestTemplate restTemplate;

    MultiValueMap<String, String> headers = new HttpHeaders() {

        {
            add("Content-Type", "application/json");
            add("Accept", "application/json");
        }
    };

    @Test
    public void shouldReturnOkOnBoard() throws Exception {
        ResponseEntity<Board> entity = restTemplate.getForEntity("/board", Board.class);
        assertThat(entity.getStatusCodeValue(), is(200));
    }

    @Test
    public void shouldErrorOnEmptyBoardFields() throws Exception {
        Board b = new Board();
        HttpEntity<String> entity = new HttpEntity<String>(toJson(b), headers);
        ResponseEntity<String> response = restTemplate.exchange(
            "/board/validate",
            HttpMethod.PUT,
            entity,
            String.class
        );

        assertThat(response.getStatusCodeValue(), is(400));
    }

    @Test
    public void shouldErrorOnInvalidInput() throws Exception {
        Board b = new Board();
        Integer[][] fields = createTestBoard();
        fields[0][0] = 10;
        b.setFields(fields);

        HttpEntity<String> entity = new HttpEntity<String>(toJson(b), headers);
        ResponseEntity<String> response = restTemplate.exchange(
            "/board/validate",
            HttpMethod.PUT,
            entity,
            String.class
        );

        assertThat(response.getStatusCodeValue(), is(400));
    }

    @Test
    public void shouldErrorOnInvalidLetters() throws Exception {
        String fieldDescription = "fields:[['a'],[],[],[],[],[],[],[],[]]";

        HttpEntity<String> entity = new HttpEntity<String>(fieldDescription, headers);
        ResponseEntity<String> response = restTemplate.exchange(
            "/board/validate",
            HttpMethod.PUT,
            entity,
            String.class
        );
        assertThat(response.getStatusCodeValue(), is(400));
    }

    @Test
    public void shouldErrorOnInvalidBoardRow() throws Exception {
        Board b = new Board();
        Integer[][] fields = createTestBoard();
        fields[0][0] = 1;
        fields[0][1] = 1;
        b.setFields(fields);

        HttpEntity<String> entity = new HttpEntity<String>(toJson(b), headers);
        ResponseEntity<String> response = restTemplate.exchange(
            "/board/validate",
            HttpMethod.PUT,
            entity,
            String.class
        );

        assertThat(response.getStatusCodeValue(), is(400));
    }

    @Test
    public void shouldErrorOnInvalidBoardColumn() throws Exception {
        Board b = new Board();
        Integer[][] fields = createTestBoard();
        fields[0][0] = 1;
        fields[1][0] = 1;
        b.setFields(fields);

        HttpEntity<String> entity = new HttpEntity<String>(toJson(b), headers);
        ResponseEntity<String> response = restTemplate.exchange(
            "/board/validate",
            HttpMethod.PUT,
            entity,
            String.class
        );

        assertThat(response.getStatusCodeValue(), is(400));
    }

    @Test
    public void shouldErrorOnInvalidBoardCell() throws Exception {
        Board b = new Board();
        Integer[][] fields = createTestBoard();
        fields[0][0] = 1;
        fields[1][1] = 1;
        b.setFields(fields);

        HttpEntity<String> entity = new HttpEntity<String>(toJson(b), headers);
        ResponseEntity<String> response = restTemplate.exchange(
            "/board/validate",
            HttpMethod.PUT,
            entity,
            String.class
        );

        assertThat(response.getStatusCodeValue(), is(400));
    }

    @Test
    public void shouldDescribeErrorOnInvalidBoard() throws Exception {
        Board b = new Board();
        b.setId("abc");
        Integer[][] fields = createTestBoard();
        fields[0][0] = 1;
        fields[1][1] = 1;
        b.setFields(fields);

        HttpEntity<String> entity = new HttpEntity<>(toJson(b), headers);
        ResponseEntity<String> response = restTemplate.exchange(
            "/board/validate",
            HttpMethod.PUT,
            entity,
            String.class
        );

        assertThat(response.getStatusCodeValue(), is(400));
        assertThat(JsonPath.compile("$.errors[0].x").read(response.getBody()), is(0));
        assertThat(JsonPath.compile("$.errors[0].y").read(response.getBody()), is(0));
        assertThat(JsonPath.compile("$.errors[1].x").read(response.getBody()), is(1));
        assertThat(JsonPath.compile("$.errors[1].y").read(response.getBody()), is(1));
    }

    @Test
    public void shouldSuccessOnValidBoard() throws Exception {
        Board b = new Board();
        b.setId("abc");
        Integer[][] fields = createTestBoard();
        b.setFields(fields);

        HttpEntity<String> entity = new HttpEntity<>(toJson(b), headers);
        ResponseEntity<String> response = restTemplate.exchange(
            "/board/validate",
            HttpMethod.PUT,
            entity,
            String.class
        );

        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(JsonPath.compile("$.state").read(response.getBody()), is("VALID"));
    }

    @Test
    public void shouldMaintainBoardState() throws Exception {
        ResponseEntity<Board> initial = restTemplate.getForEntity("/board", Board.class);
        Board board = initial.getBody();
        Integer[][] fields = board.getFields();
        assertThat(fields[0][3], is(nullValue()));
        fields[0][3] = 1;
        board.setFields(fields);
        HttpEntity<String> toValidate = new HttpEntity<>(toJson(board), headers);
        restTemplate.exchange("/board/validate", HttpMethod.PUT, toValidate, String.class);
        ResponseEntity<Board> updated = restTemplate.getForEntity("/board/" + board.getId(), Board.class);
        board = updated.getBody();
        assertThat(board.getFields()[0][3], is(1));
        ResponseEntity<Board> newGame = restTemplate.getForEntity("/board", Board.class);
        assertThat(newGame.getBody().getFields()[0][3], is(nullValue()));
    }

    private String toJson(Board b) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(b);
    }

    public Integer[][] createTestBoard() {
        Integer[][] fields = new Integer[9][];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new Integer[9];
        }
        return fields;
    }
}
