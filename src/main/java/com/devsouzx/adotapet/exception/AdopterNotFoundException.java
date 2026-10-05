package com.devsouzx.adotapet.exception;

import org.springframework.http.HttpStatus;

public class AdopterNotFoundException extends ApiException {
    public AdopterNotFoundException() {
        super(HttpStatus.NOT_FOUND, "ADOPTER_NOT_FOUND", "Adotante não encontrado");
    }
}
