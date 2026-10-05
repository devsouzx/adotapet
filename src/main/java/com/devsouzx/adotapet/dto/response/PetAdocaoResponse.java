package com.devsouzx.adotapet.dto.response;

import java.util.UUID;

public record PetAdocaoResponse(
        UUID id,
        String nome
) {
}
