package com.devsouzx.adotapet.exception;

import java.time.Instant;
import java.util.Map;

public record ApiErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String code,
        String message,
        String path,
        Map<String, String> details
) {
    public static ApiErrorResponse of(
            int status,
            String error,
            String code,
            String message,
            String path,
            Map<String, String> details
    ) {
        return new ApiErrorResponse(Instant.now(), status, error, code, message, path, details);
    }
}
