package com.jk.sns.exception;

import static com.jk.sns.exception.ErrorCode.DATABASE_ERROR;

import com.jk.sns.controller.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(SimpleSnsApplicationException.class)
    public ResponseEntity<?> errorHandler(SimpleSnsApplicationException e) {
        return ResponseEntity.status(e.getErrorCode().getStatus())
            .body(Response.error(e.getErrorCode().name(), e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> databaseErrorHandler(IllegalArgumentException e) {
        return ResponseEntity.status(DATABASE_ERROR.getStatus())
            .body(Response.error(DATABASE_ERROR.name(),
                String.format("%s. %s", DATABASE_ERROR.getMessage() + e.getMessage())));
    }
}

