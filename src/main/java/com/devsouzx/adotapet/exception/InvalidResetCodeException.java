package com.devsouzx.adotapet.exception;

import org.springframework.http.HttpStatus;

public class InvalidResetCodeException extends ApiException {
    public InvalidResetCodeException() {
        super(
                HttpStatus.BAD_REQUEST,
                "INVALID_OR_EXPIRED_RESET_CODE",
                "Código de redefinição inválido ou expirado"
        );
    }
}
