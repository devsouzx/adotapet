package com.devsouzx.adotapet.model.enums;

/**
 * Enumeração que define os possíveis status de uma solicitação de adoção.
 *
 * <p>Utilizada para controlar o fluxo de aprovação da solicitação.
 * As opções disponíveis são:</p>
 * <ul>
 *   <li>{@link #PENDENTE} - Solicitação aguardando análise do abrigo</li>
 *   <li>{@link #APROVADA} - Solicitação aprovada pelo abrigo</li>
 *   <li>{@link #RECUSADA} - Solicitação recusada pelo abrigo</li>
 *   <li>{@link #CANCELADA_PELO_ADOTANTE} - Solicitação cancelada pelo adotante</li>
 * </ul>
 *
 *
 * <p>Fluxo de status:</p>
 * <pre>
 * PENDENTE → APROVADA (pet é marcado como adotado)
 * PENDENTE → RECUSADA (pet permanece disponível)
 * PENDENTE → CANCELADA_PELO_ADOTANTE (pet permanece disponível)
 * </pre>
 *
 *
 * @author Equipe Adoção de Pets
 * @version 1.0
 * @since 2026
 * @see com.devsouzx.adotapet.model.SolicitacaoAdocao
 */
public enum StatusSolicitacao {

    /** Solicitação aguardando análise do abrigo */
    PENDENTE("Pendente"),

    /** Solicitação aprovada pelo abrigo */
    APROVADA("Aprovada"),

    /** Solicitação recusada pelo abrigo */
    RECUSADA("Recusada"),

    /** Solicitação cancelada pelo adotante */
    CANCELADA_PELO_ADOTANTE("Cancelada pelo Adotante");

    /** Descrição amigável do status */
    private String descricao;

    /**
     * Construtor privado do enum.
     *
     * @param descricao Descrição do status
     */
    StatusSolicitacao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Retorna a descrição amigável do status.
     *
     * @return Descrição do status (ex: "Pendente", "Aprovada", etc.)
     */
    public String getDescricao() {
        return descricao;
    }
}