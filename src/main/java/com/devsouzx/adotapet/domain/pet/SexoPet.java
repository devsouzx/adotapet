package com.devsouzx.adotapet.domain.pet;

public enum SexoPet {
    MACHO,
    FEMEA;

    public static SexoPet fromString(String sexo) {
        if (sexo.equalsIgnoreCase("MACHO")) {
            return MACHO;
        } else if (sexo.equalsIgnoreCase("FEMEA")) {
            return FEMEA;
        } else {
            throw new IllegalArgumentException("Sexo inválido: " + sexo);
        }
    }
}
