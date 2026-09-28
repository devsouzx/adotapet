package com.devsouzx.adotapet.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record PetRequest(
        @NotBlank
        String nome,
        @NotBlank
        String especie,
        @NotBlank
        String raca,
        String descricao,
        @PositiveOrZero
        Integer idadeEstimadaMeses,
        @Positive
        BigDecimal peso,
        String fotoUrl,
        @NotBlank
        String status,
        @NotBlank
        String sexo,
        @NotBlank
        String porte
) {
}
