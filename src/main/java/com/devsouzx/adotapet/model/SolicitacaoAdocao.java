package com.devsouzx.adotapet.model;

import com.devsouzx.adotapet.model.enums.StatusSolicitacao;

import java.time.LocalDateTime;

/**
 * Classe que representa uma solicitação de adoção.
 *
 * <p>Uma solicitação é feita por um {@link Adotante} para um {@link Pet}.
 * Pode ter status: PENDENTE, APROVADA, RECUSADA ou CANCELADA_PELO_ADOTANTE.</p>
 *
 * @author Equipe Adoção de Pets
 * @version 1.0
 * @since 2026
 * @see Adotante
 * @see Pet
 */
public class SolicitacaoAdocao {

    /** Identificador único da solicitação */
    private int id;

    /** Data e hora em que a solicitação foi feita */
    private LocalDateTime dataSolicitacao;

    /** Data e hora em que o abrigo respondeu */
    private LocalDateTime dataResposta;

    /** Status da solicitação */
    private StatusSolicitacao status;

    /** Justificativa (usada em recusas ou cancelamentos) */
    private String justificativa;

    /** Adotante que fez a solicitação */
    private Adotante adotante;

    /** Pet solicitado para adoção */
    private Pet pet;

    /**
     * Construtor para criar uma nova solicitação de adoção.
     * A solicitação é criada com status PENDENTE.
     *
     * @param adotante Adotante solicitante
     * @param pet Pet solicitado
     */
    public SolicitacaoAdocao(Adotante adotante, Pet pet) {
        this.dataSolicitacao = LocalDateTime.now();
        this.status = StatusSolicitacao.PENDENTE;
        this.adotante = adotante;
        this.pet = pet;
    }

    /**
     * Construtor para criar uma solicitação com dados pré-definidos.
     *
     * @param id Identificador da solicitação
     * @param dataSolicitacao Data da solicitação
     * @param dataResposta Data da resposta
     * @param status Status da solicitação
     * @param justificativa Justificativa
     * @param adotante Adotante solicitante
     * @param pet Pet solicitado
     */
    public SolicitacaoAdocao(int id, LocalDateTime dataSolicitacao, LocalDateTime dataResposta,
                             StatusSolicitacao status, String justificativa,
                             Adotante adotante, Pet pet) {
        this.id = id;
        this.dataSolicitacao = dataSolicitacao;
        this.dataResposta = dataResposta;
        this.status = status;
        this.justificativa = justificativa;
        this.adotante = adotante;
        this.pet = pet;
    }

    /**
     * Aprova a solicitação de adoção.
     * Altera o status para APROVADA e registra a data da resposta.
     */
    public void aprovar() {
        this.status = StatusSolicitacao.APROVADA;
        this.dataResposta = LocalDateTime.now();
    }

    /**
     * Recusa a solicitação de adoção.
     *
     * @param justificativa Motivo da recusa
     */
    public void recusar(String justificativa) {
        this.status = StatusSolicitacao.RECUSADA;
        this.dataResposta = LocalDateTime.now();
        this.justificativa = justificativa;
    }

    /**
     * Cancela a solicitação pelo adotante.
     * Altera o status para CANCELADA_PELO_ADOTANTE.
     */
    public void cancelar() {
        this.status = StatusSolicitacao.CANCELADA_PELO_ADOTANTE;
        this.dataResposta = LocalDateTime.now();
    }

    /**
     * Verifica se a solicitação está pendente.
     *
     * @return {@code true} se o status for PENDENTE
     */
    public boolean isPendente() {
        return status == StatusSolicitacao.PENDENTE;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDateTime getDataSolicitacao() { return dataSolicitacao; }
    public void setDataSolicitacao(LocalDateTime dataSolicitacao) { this.dataSolicitacao = dataSolicitacao; }
    public LocalDateTime getDataResposta() { return dataResposta; }
    public void setDataResposta(LocalDateTime dataResposta) { this.dataResposta = dataResposta; }
    public StatusSolicitacao getStatus() { return status; }
    public void setStatus(StatusSolicitacao status) { this.status = status; }
    public String getJustificativa() { return justificativa; }
    public void setJustificativa(String justificativa) { this.justificativa = justificativa; }
    public Adotante getAdotante() { return adotante; }
    public void setAdotante(Adotante adotante) { this.adotante = adotante; }
    public Pet getPet() { return pet; }
    public void setPet(Pet pet) { this.pet = pet; }
}