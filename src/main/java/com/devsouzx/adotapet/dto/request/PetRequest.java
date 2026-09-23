package com.devsouzx.adotapet.dto.request;

import java.math.BigDecimal;

public record PetRequest(
        String nome,
        String especie,
        String raca,
        String descricao,
        Integer idadeEstimadaMeses,
        BigDecimal peso,
        String fotoUrl,
        String status,
        String sexo,
        String porte
) {
}
