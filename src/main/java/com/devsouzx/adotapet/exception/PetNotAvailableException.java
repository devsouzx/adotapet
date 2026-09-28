package com.devsouzx.adotapet.exception;

import org.springframework.http.HttpStatus;

public class PetNotAvailableException extends ApiException {
    public PetNotAvailableException() {
        super(HttpStatus.CONFLICT, "PET_NOT_AVAILABLE", "O pet não está disponível para adoção");
    }
}
