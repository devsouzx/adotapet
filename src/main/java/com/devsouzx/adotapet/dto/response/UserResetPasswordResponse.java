package com.devsouzx.adotapet.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserResetPasswordResponse(
    UUID abrigoId,
    String email,
    String resetPasswordCode
) {}
