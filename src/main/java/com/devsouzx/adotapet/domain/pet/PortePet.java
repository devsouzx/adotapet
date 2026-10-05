package com.devsouzx.adotapet.domain.pet;

public enum PortePet {
    PEQUENO,
    MEDIO,
    GRANDE;

    public static PortePet fromString(String porte) {
        for (PortePet p : PortePet.values()) {
            if (p.name().equalsIgnoreCase(porte)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Porte inválido: " + porte);
    }
}
