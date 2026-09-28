package com.devsouzx.adotapet.exception;

import org.springframework.http.HttpStatus;

public class InvalidClosureDateException extends ApiException {
    public InvalidClosureDateException() {
        super(HttpStatus.BAD_REQUEST, "INVALID_CLOSURE_DATE", "Data de encerramento inválida");
    }
}
