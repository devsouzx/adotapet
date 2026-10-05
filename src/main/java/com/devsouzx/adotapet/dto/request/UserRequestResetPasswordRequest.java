package com.devsouzx.adotapet.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserRequestResetPasswordRequest(
    @Email
    @NotBlank
    String email
) {
}
