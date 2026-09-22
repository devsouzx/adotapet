package com.devsouzx.adotapet.dto.response;

import lombok.Builder;

@Builder
public record UserResetPasswordResponse(
    String email,
    String resetPasswordCode
) {
    @Override
    public String toString() {
        return "{\"email\": \"" + email + "\", \"resetPasswordCode\": \"" + resetPasswordCode + "\"}";
    }
}
