package com.devsouzx.adotapet.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record AdocaoRequest(
        @NotNull UUID petId,
        @NotNull UUID adotanteId,
        LocalDate dataAdocao,
        String observacoes
) {
}
