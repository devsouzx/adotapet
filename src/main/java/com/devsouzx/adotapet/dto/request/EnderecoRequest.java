package com.devsouzx.adotapet.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

public record EnderecoRequest(
        String logradouro,
        String cep,
        String numero,
        String bairro,
        String cidade,
        String estado,
        @DecimalMin("-90.0")
        @DecimalMax("90.0")
        BigDecimal latitude,
        @DecimalMin("-180.0")
        @DecimalMax("180.0")
        BigDecimal longitude
) {
}
