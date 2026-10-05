package com.devsouzx.adotapet.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record AdocaoUpdateRequest(
        @NotNull UUID adotanteId,
        @NotNull LocalDate dataAdocao,
        String observacoes
) {
}
