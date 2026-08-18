package com.devsouzx.adotapet.dto;

public record RegisterRequestDTO(
        String nome,
        String email,
        String senha,
        String repetirSenha,
        String telefone,
        String cnpj,
        String horarioFuncionamento,
        String descricao,
        String fotoUrl,
        EnderecoRequestDTO endereco
) {
}
