package com.devsouzx.adotapet.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserResetPasswordRequest(
    @NotBlank
    @NotNull
    String newPassword,
    @NotBlank
    @NotNull
    String confirmPassword
) {
}
