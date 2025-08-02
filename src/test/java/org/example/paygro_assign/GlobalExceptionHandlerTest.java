package org.example.paygro_assign;

import org.example.paygro_assign.exceptions.GlobalExceptionHandler;
import org.example.paygro_assign.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {
    @Test
    void handleNotFound_returnsNotFoundStatus() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        NotFoundException ex = new NotFoundException("missing!");
        ResponseEntity<Map<String, String>> res = handler.handleNotFound(ex);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        assertEquals("missing!", res.getBody().get("error"));
    }


}
