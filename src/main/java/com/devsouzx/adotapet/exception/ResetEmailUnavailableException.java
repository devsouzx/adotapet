package com.devsouzx.adotapet.exception;

import org.springframework.http.HttpStatus;

public class ResetEmailUnavailableException extends ApiException {
    public ResetEmailUnavailableException(Throwable cause) {
        super(
                HttpStatus.SERVICE_UNAVAILABLE,
                "RESET_EMAIL_UNAVAILABLE",
                "Não foi possível enviar o e-mail de redefinição no momento",
                cause
        );
    }
}
