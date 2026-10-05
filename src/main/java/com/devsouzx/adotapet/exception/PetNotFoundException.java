package com.devsouzx.adotapet.exception;

import org.springframework.http.HttpStatus;

public class PetNotFoundException extends ApiException {
    public PetNotFoundException() {
        super(HttpStatus.NOT_FOUND, "PET_NOT_FOUND", "Pet não encontrado");
    }
}
