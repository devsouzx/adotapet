package com.devsouzx.adotapet.dto;

import lombok.Builder;

@Builder
public record EnderecoResponse(
        String logradouro,
        String cep,
        String numero,
        String bairro,
        String cidade,
        String estado
) {
}
