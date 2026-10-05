package com.devsouzx.adotapet.domain.pet;

public enum StatusPet {
    DISPONIVEL,
    ADOTADO,
    INDISPONIVEL;

    public static StatusPet fromString(String status) {
        for (StatusPet s : StatusPet.values()) {
            if (s.name().equalsIgnoreCase(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Status inválido: " + status);
    }
}
