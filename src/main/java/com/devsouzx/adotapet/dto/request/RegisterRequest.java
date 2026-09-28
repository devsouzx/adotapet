package com.devsouzx.adotapet.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
        String nome,
        @NotBlank
        @Email
        String email,
        @NotBlank
        String senha,
        @NotBlank
        String repetirSenha,
        String telefone,
        String cnpj,
        String horarioFuncionamento,
        String descricao,
        String fotoUrl,
        @NotNull
        @Valid EnderecoRequest endereco
) {
}
