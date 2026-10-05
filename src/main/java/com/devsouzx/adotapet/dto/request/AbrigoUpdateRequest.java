package com.devsouzx.adotapet.dto.request;

import jakarta.validation.constraints.Email;

public record AbrigoUpdateRequest(
        String nome,
        @Email
        String email,
        String telefone,
        String cnpj,
        String horarioFuncionamento,
        String descricao,
        String fotoUrl
) {
}
