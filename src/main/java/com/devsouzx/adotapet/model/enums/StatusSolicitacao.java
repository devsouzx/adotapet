package com.devsouzx.adotapet.model.enums;

public enum StatusSolicitacao {
    PENDENTE("Pendente"),
    APROVADA("Aprovada"),
    RECUSADA("Recusada"),
    CANCELADA_PELO_ADOTANTE("Cancelada pelo Adotante");

    private String descricao;

    StatusSolicitacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}