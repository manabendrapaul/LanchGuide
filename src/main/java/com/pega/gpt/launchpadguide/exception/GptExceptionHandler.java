package com.pega.gpt.launchpadguide.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GptExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, String>> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        logger.error(exc.getMessage(), exc);
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(Map.of("Message", "File too large!"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> illegalArguementException(IllegalArgumentException ex) {
        logger.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(Map.of("Message", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> generic(Exception ex) {
        logger.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(Map.of("Message", "Unknown Error"));
    }

}