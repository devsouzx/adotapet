package com.devsouzx.adotapet.exception;

import org.springframework.http.HttpStatus;

public class PetUpdateForbiddenException extends ApiException {
    public PetUpdateForbiddenException() {
        super(
                HttpStatus.FORBIDDEN,
                "PET_UPDATE_FORBIDDEN",
                "Você não tem permissão para atualizar este pet"
        );
    }
}
