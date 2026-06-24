package com.devsouzx.adotapet.model;

import com.devsouzx.adotapet.exception.AdocaoException;
import com.devsouzx.adotapet.model.enums.StatusSolicitacao;

import java.time.LocalDateTime;

/**
 * Classe que representa uma avaliação feita por um adotante sobre um abrigo.
 *
 * <p>Uma avaliação só pode ser feita após uma solicitação de adoção ser aprovada.
 * Contém uma nota de 1 a 5 estrelas e um comentário opcional.</p>
 *
 * @author Equipe Adoção de Pets
 * @version 1.0
 * @since 2026
 * @see Adotante
 * @see Abrigo
 * @see SolicitacaoAdocao
 */
public class Avaliacao {

    /** Identificador único da avaliação */
    private int id;

    /** Nota de 1 a 5 estrelas */
    private int nota;

    /** Comentário opcional sobre a experiência */
    private String comentario;

    /** Data e hora em que a avaliação foi feita */
    private LocalDateTime dataAvaliacao;

    /** Adotante que fez a avaliação */
    private Adotante adotante;

    /** Solicitação de adoção relacionada */
    private SolicitacaoAdocao solicitacao;

    /** Abrigo avaliado */
    private Abrigo abrigo;

    /**
     * Construtor para criar uma nova avaliação.
     *
     * @param adotante Adotante que está avaliando
     * @param abrigo Abrigo sendo avaliado
     * @param solicitacao Solicitação de adoção aprovada
     * @param nota Nota de 1 a 5 estrelas
     * @param comentario Comentário opcional
     * @throws AdocaoException Se a nota for inválida ou a solicitação não estiver aprovada
     */
    public Avaliacao(Adotante adotante, Abrigo abrigo,
                     SolicitacaoAdocao solicitacao, int nota, String comentario) throws AdocaoException {
        if (nota < 1 || nota > 5) {
            throw new AdocaoException("A nota deve ser entre 1 e 5 estrelas.");
        }
        if (solicitacao.getStatus() != StatusSolicitacao.APROVADA) {
            throw new AdocaoException("Só é possível avaliar uma adoção que foi aprovada.");
        }
        this.nota = nota;
        this.comentario = comentario;
        this.dataAvaliacao = LocalDateTime.now();
        this.adotante = adotante;
        this.abrigo = abrigo;
        this.solicitacao = solicitacao;
    }

    /**
     * Construtor para criar uma avaliação com dados pré-definidos.
     *
     * @param id Identificador da avaliação
     * @param nota Nota de 1 a 5 estrelas
     * @param comentario Comentário
     * @param dataAvaliacao Data da avaliação
     * @param adotante Adotante que avaliou
     * @param abrigo Abrigo avaliado
     * @param solicitacao Solicitação relacionada
     */
    public Avaliacao(int id, int nota, String comentario, LocalDateTime dataAvaliacao,
                     Adotante adotante, Abrigo abrigo, SolicitacaoAdocao solicitacao) {
        this.id = id;
        this.nota = nota;
        this.comentario = comentario;
        this.dataAvaliacao = dataAvaliacao;
        this.adotante = adotante;
        this.abrigo = abrigo;
        this.solicitacao = solicitacao;
    }

    /**
     * Valida se a nota está entre 1 e 5.
     *
     * @return {@code true} se a nota for válida
     */
    public boolean validarNota() {
        return nota >= 1 && nota <= 5;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getNota() { return nota; }
    public void setNota(int nota) { this.nota = nota; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public LocalDateTime getDataAvaliacao() { return dataAvaliacao; }
    public void setDataAvaliacao(LocalDateTime dataAvaliacao) { this.dataAvaliacao = dataAvaliacao; }
    public Adotante getAdotante() { return adotante; }
    public void setAdotante(Adotante adotante) { this.adotante = adotante; }
    public SolicitacaoAdocao getSolicitacao() { return solicitacao; }
    public void setSolicitacao(SolicitacaoAdocao solicitacao) { this.solicitacao = solicitacao; }
    public Abrigo getAbrigo() { return abrigo; }
    public void setAbrigo(Abrigo abrigo) { this.abrigo = abrigo; }
}