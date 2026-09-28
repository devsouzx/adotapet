package com.devsouzx.adotapet.exception;

import org.springframework.http.HttpStatus;

public class PetDeletionForbiddenException extends ApiException {
    public PetDeletionForbiddenException() {
        super(
                HttpStatus.FORBIDDEN,
                "PET_DELETE_FORBIDDEN",
                "Você não tem permissão para remover este pet"
        );
    }
}
