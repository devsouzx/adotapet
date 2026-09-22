package com.devsouzx.adotapet.dto.response;

import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.domain.pet.PortePet;
import com.devsouzx.adotapet.domain.pet.SexoPet;
import com.devsouzx.adotapet.domain.pet.StatusPet;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PetResponse(
        UUID id,
        String nome,
        String especie,
        String raca,
        String descricao,
        Integer idadeEstimadaMeses,
        BigDecimal peso,
        String fotoUrl,
        LocalDateTime dataCadastro,
        StatusPet status,
        SexoPet sexo,
        PortePet porte,
        Abrigo abrigo
) {
}
