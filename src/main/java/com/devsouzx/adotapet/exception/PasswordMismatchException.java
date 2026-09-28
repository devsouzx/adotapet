package com.devsouzx.adotapet.exception;

import org.springframework.http.HttpStatus;

public class PasswordMismatchException extends ApiException {
    public PasswordMismatchException() {
        super(HttpStatus.BAD_REQUEST, "PASSWORD_MISMATCH", "As senhas não coincidem");
    }
}
