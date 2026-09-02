package com.devsouzx.adotapet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResetPasswordRequest {
    @NotBlank
    @NotNull
    private String newPassword;
    @NotBlank
    @NotNull
    private String confirmPassword;
}
