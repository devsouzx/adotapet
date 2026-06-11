package com.devsouzx.adotapet.model.enums;

public enum Especie {
    CACHORRO("Cachorro"),
    GATO("Gato"),
    OUTRO("Outro");

    private String descricao;

    Especie(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static Especie fromString(String text) {
        for (Especie e : Especie.values()) {
            if (e.descricao.equalsIgnoreCase(text) || e.name().equalsIgnoreCase(text)) {
                return e;
            }
        }
        return OUTRO;
    }
}