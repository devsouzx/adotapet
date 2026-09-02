package com.devsouzx.adotapet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

@AllArgsConstructor
@Builder
public class UserResetPasswordResponse {
    String email;
    String resetPasswordCode;

    @Override
    public String toString() {
        return "{\"email\": \"" + email + "\", \"resetPasswordCode\": \"" + resetPasswordCode + "\"}";
    }
}
