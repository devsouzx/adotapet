package com.devsouzx.adotapet.dto.response;

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
