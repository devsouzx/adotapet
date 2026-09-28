package com.devsouzx.adotapet.exception;

import org.springframework.http.HttpStatus;

public class ShelterNotFoundException extends ApiException {
    public ShelterNotFoundException() {
        super(HttpStatus.NOT_FOUND, "SHELTER_NOT_FOUND", "Abrigo não encontrado");
    }
}
