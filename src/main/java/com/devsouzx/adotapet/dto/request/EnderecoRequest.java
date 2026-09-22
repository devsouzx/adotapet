package com.devsouzx.adotapet.dto.request;

public record EnderecoRequest(
        String logradouro,
        String cep,
        String numero,
        String bairro,
        String cidade,
        String estado
) {
}
