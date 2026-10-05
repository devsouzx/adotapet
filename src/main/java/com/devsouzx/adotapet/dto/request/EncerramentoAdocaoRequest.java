package com.devsouzx.adotapet.dto.request;

import java.time.LocalDate;

public record EncerramentoAdocaoRequest(
        LocalDate dataEncerramento,
        String motivoEncerramento
) {
}
