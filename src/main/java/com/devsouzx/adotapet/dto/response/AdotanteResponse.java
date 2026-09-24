package com.devsouzx.adotapet.dto.response;

import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record AdotanteResponse(
        UUID id,
        String nome,
        String telefone,
        String email,
        LocalDate dataNascimento
) {
}
