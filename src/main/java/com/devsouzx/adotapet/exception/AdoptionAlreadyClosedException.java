package com.devsouzx.adotapet.exception;

import org.springframework.http.HttpStatus;

public class AdoptionAlreadyClosedException extends ApiException {
    public AdoptionAlreadyClosedException() {
        super(HttpStatus.CONFLICT, "ADOPTION_ALREADY_CLOSED", "A adoção já está encerrada");
    }
}
