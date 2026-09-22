package com.devsouzx.adotapet.dto.request;

public record AbrigoUpdateRequest(
        String nome,
        String email,
        String telefone,
        String cnpj,
        String horarioFuncionamento,
        String descricao,
        String fotoUrl
) {
}
