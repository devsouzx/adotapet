package com.devsouzx.adotapet.model.enums;

/**
 * Enumeração que define as espécies de animais disponíveis para adoção.
 *
 * <p>Utilizada para categorizar os pets cadastrados no sistema.
 * As opções disponíveis são:</p>
 * <ul>
 *   <li>{@link #CACHORRO} - Animais da espécie canina</li>
 *   <li>{@link #GATO} - Animais da espécie felina</li>
 *   <li>{@link #OUTRO} - Outras espécies (coelhos, hamsters, aves, etc.)</li>
 * </ul>
 *
 *
 * @author Equipe Adoção de Pets
 * @version 1.0
 * @since 2026
 * @see com.devsouzx.adotapet.model.Pet
 */
public enum Especie {

    /** Espécie canina (cachorros) */
    CACHORRO("Cachorro"),

    /** Espécie felina (gatos) */
    GATO("Gato"),

    /** Outras espécies (coelhos, hamsters, aves, etc.) */
    OUTRO("Outro");

    /** Descrição amigável da espécie */
    private String descricao;

    /**
     * Construtor privado do enum.
     *
     * @param descricao Descrição da espécie
     */
    Especie(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Retorna a descrição amigável da espécie.
     *
     * @return Descrição da espécie (ex: "Cachorro", "Gato", "Outro")
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Converte uma string para o enum correspondente.
     *
     * <p>A conversão ignora maiúsculas/minúsculas e verifica tanto
     * o nome do enum quanto a descrição.</p>
     *
     * @param text Texto a ser convertido
     * @return Enum correspondente, ou {@link #OUTRO} se não encontrar
     */
    public static Especie fromString(String text) {
        for (Especie e : Especie.values()) {
            if (e.descricao.equalsIgnoreCase(text) || e.name().equalsIgnoreCase(text)) {
                return e;
            }
        }
        return OUTRO;
    }
}