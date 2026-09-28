package com.devsouzx.adotapet.exception;

import org.springframework.http.HttpStatus;

public class InvalidAdoptionDateException extends ApiException {
    public InvalidAdoptionDateException() {
        super(HttpStatus.BAD_REQUEST, "INVALID_ADOPTION_DATE", "A data da adoção deve ser hoje ou anterior");
    }

    public InvalidAdoptionDateException(String message) {
        super(HttpStatus.BAD_REQUEST, "INVALID_ADOPTION_DATE", message);
    }
}
