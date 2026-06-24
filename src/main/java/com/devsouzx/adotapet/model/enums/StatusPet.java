package com.devsouzx.adotapet.model.enums;

/**
 * Enumeração que define os possíveis status de um pet no sistema.
 *
 * <p>Utilizada para controlar a disponibilidade do pet para adoção.
 * As opções disponíveis são:</p>
 * <ul>
 *   <li>{@link #DISPONIVEL} - Pet está disponível para adoção</li>
 *   <li>{@link #ADOTADO} - Pet já foi adotado e não está mais disponível</li>
 * </ul>
 *
 *
 * <p>O status do pet é automaticamente definido como DISPONIVEL
 * quando cadastrado e alterado para ADOTADO quando uma solicitação
 * de adoção é aprovada.</p>
 *
 * @author Equipe Adoção de Pets
 * @version 1.0
 * @since 2026
 * @see com.devsouzx.adotapet.model.Pet
 */
public enum StatusPet {

    /** Pet disponível para adoção */
    DISPONIVEL("Disponível"),

    /** Pet já foi adotado */
    ADOTADO("Adotado");

    /** Descrição amigável do status */
    private String descricao;

    /**
     * Construtor privado do enum.
     *
     * @param descricao Descrição do status
     */
    StatusPet(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Retorna a descrição amigável do status.
     *
     * @return Descrição do status (ex: "Disponível", "Adotado")
     */
    public String getDescricao() {
        return descricao;
    }
}