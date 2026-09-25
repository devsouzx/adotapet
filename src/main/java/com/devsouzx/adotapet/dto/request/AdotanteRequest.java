package com.devsouzx.adotapet.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record AdotanteRequest(
    String nome,
    String email,
    String telefone,
    @JsonAlias("data_nascimento")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dataNascimento
) {
}
