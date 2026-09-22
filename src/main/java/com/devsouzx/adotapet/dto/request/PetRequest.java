package com.devsouzx.adotapet.dto.request;

import com.devsouzx.adotapet.domain.pet.PortePet;
import com.devsouzx.adotapet.domain.pet.SexoPet;
import com.devsouzx.adotapet.domain.pet.StatusPet;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
