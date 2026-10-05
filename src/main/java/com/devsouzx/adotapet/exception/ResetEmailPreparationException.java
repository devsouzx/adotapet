package com.devsouzx.adotapet.exception;

import org.springframework.http.HttpStatus;

public class ResetEmailPreparationException extends ApiException {
    public ResetEmailPreparationException(Throwable cause) {
        super(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "RESET_EMAIL_PREPARATION_FAILED",
                "Não foi possível preparar o e-mail de redefinição",
                cause
        );
    }
}
