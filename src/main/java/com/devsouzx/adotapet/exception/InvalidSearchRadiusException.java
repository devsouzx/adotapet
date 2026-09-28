package com.devsouzx.adotapet.exception;

import org.springframework.http.HttpStatus;

public class InvalidSearchRadiusException extends ApiException {
    public InvalidSearchRadiusException() {
        super(
                HttpStatus.BAD_REQUEST,
                "INVALID_PAGINATION_OR_RADIUS",
                "Raio deve ser positivo, page >= 0 e size deve estar entre 1 e 100"
        );
    }
}
