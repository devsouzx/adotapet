package com.devsouzx.adotapet.exception;

import org.springframework.http.HttpStatus;

public class InvalidCoordinatesException extends ApiException {
    public InvalidCoordinatesException() {
        super(
                HttpStatus.BAD_REQUEST,
                "INVALID_COORDINATES",
                "Latitude ou longitude fora do intervalo permitido"
        );
    }
}
