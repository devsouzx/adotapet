package com.devsouzx.adotapet.exception;

import org.springframework.http.HttpStatus;

public class InvalidPaginationException extends ApiException {
    public InvalidPaginationException() {
        super(
                HttpStatus.BAD_REQUEST,
                "INVALID_PAGINATION",
                "page deve ser >= 0 e size deve estar entre 1 e 100"
        );
    }

    public InvalidPaginationException(String message) {
        super(HttpStatus.BAD_REQUEST, "INVALID_PAGINATION", message);
    }
}
