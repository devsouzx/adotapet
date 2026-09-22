package com.devsouzx.adotapet.dto.response;

import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AbrigoInfoResponse(
        String nome,
        String email,
        String telefone,
        String cnpj,
        String horarioFuncionamento,
        String descricao,
        String fotoUrl,
        boolean ativo,
        LocalDateTime dataCadastro,
        EnderecoResponse endereco
) {
}
