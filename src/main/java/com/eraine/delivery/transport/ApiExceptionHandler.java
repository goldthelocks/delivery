package com.eraine.delivery.transport;

import com.eraine.delivery.model.ErrorResponse;
import com.eraine.delivery.model.ParcelRejectedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ParcelRejectedException.class)
    public ResponseEntity<ErrorResponse> handleParcelRejectedException() {
        return ResponseEntity.badRequest().body(new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Parcel exceeds weight limit.",
                LocalDateTime.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("Exception thrown:", ex);
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Sorry, something broke.",
                        LocalDateTime.now()));
    }
}
