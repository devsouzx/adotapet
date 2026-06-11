package com.devsouzx.adotapet.model.enums;

public enum StatusPet {
    DISPONIVEL("Disponível"),
    ADOTADO("Adotado");

    private String descricao;

    StatusPet(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}