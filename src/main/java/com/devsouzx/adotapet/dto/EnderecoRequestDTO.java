package com.devsouzx.adotapet.dto;

public record EnderecoRequestDTO(
        String logradouro,
        String cep,
        String numero,
        String bairro,
        String cidade,
        String estado
) {
}
