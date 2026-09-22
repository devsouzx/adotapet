package com.devsouzx.adotapet.dto.request;

public record RegisterRequest(
        String nome,
        String email,
        String senha,
        String repetirSenha,
        String telefone,
        String cnpj,
        String horarioFuncionamento,
        String descricao,
        String fotoUrl,
        EnderecoRequest endereco
) {
}
