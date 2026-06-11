package com.devsouzx.adotapet.model.enums;

public enum Porte {
    PEQUENO("Pequeno"),
    MEDIO("Médio"),
    GRANDE("Grande");

    private String descricao;

    Porte(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static Porte fromString(String text) {
        for (Porte p : Porte.values()) {
            if (p.descricao.equalsIgnoreCase(text) || p.name().equalsIgnoreCase(text)) {
                return p;
            }
        }
        return MEDIO;
    }
}