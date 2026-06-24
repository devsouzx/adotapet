package com.devsouzx.adotapet.model.enums;

/**
 * Enumeração que define os portes dos animais disponíveis para adoção.
 *
 * <p>Utilizada para classificar os pets pelo seu tamanho físico.
 * As opções disponíveis são:</p>
 * <ul>
 *   <li>{@link #PEQUENO} - Animais de pequeno porte (ex: Poodle, Siamês)</li>
 *   <li>{@link #MEDIO} - Animais de médio porte (ex: Beagle, Collie)</li>
 *   <li>{@link #GRANDE} - Animais de grande porte (ex: Labrador, Pastor Alemão)</li>
 * </ul>
 *
 *
 * @author Equipe Adoção de Pets
 * @version 1.0
 * @since 2026
 * @see com.devsouzx.adotapet.model.Pet
 */
public enum Porte {

    /** Animais de pequeno porte */
    PEQUENO("Pequeno"),

    /** Animais de médio porte */
    MEDIO("Médio"),

    /** Animais de grande porte */
    GRANDE("Grande");

    /** Descrição amigável do porte */
    private String descricao;

    /**
     * Construtor privado do enum.
     *
     * @param descricao Descrição do porte
     */
    Porte(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Retorna a descrição amigável do porte.
     *
     * @return Descrição do porte (ex: "Pequeno", "Médio", "Grande")
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
     * @return Enum correspondente, ou {@link #MEDIO} se não encontrar
     */
    public static Porte fromString(String text) {
        for (Porte p : Porte.values()) {
            if (p.descricao.equalsIgnoreCase(text) || p.name().equalsIgnoreCase(text)) {
                return p;
            }
        }
        return MEDIO;
    }
}