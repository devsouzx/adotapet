package com.devsouzx.adotapet.exception;

import org.springframework.http.HttpStatus;

public class AdoptionNotFoundException extends ApiException {
    public AdoptionNotFoundException() {
        super(HttpStatus.NOT_FOUND, "ADOPTION_NOT_FOUND", "Adoção não encontrada");
    }
}
