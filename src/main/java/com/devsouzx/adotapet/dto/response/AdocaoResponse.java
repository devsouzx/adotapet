package com.devsouzx.adotapet.dto.response;

import com.devsouzx.adotapet.domain.adocao.StatusAdocao;

import java.time.LocalDate;
import java.util.UUID;

public record AdocaoResponse(
        UUID id,
        LocalDate dataAdocao,
        StatusAdocao status,
        String observacoes,
        LocalDate dataEncerramento,
        String motivoEncerramento,
        UUID abrigoId,
        PetAdocaoResponse pet,
        AdotanteResponse adotante
) {
}
