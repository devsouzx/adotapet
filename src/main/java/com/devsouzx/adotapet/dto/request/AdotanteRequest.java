package com.devsouzx.adotapet.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record AdotanteRequest(
    @NotBlank
    String nome,
    @NotBlank
    @Email
    String email,
    String telefone,
    @JsonAlias("data_nascimento")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dataNascimento
) {
}
