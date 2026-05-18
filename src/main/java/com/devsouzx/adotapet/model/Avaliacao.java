package com.devsouzx.adotapet.model;

import java.time.LocalDateTime;

public class Avaliacao {
    private int id;
    private int nota;
    private String comentario;
    private LocalDateTime dataAvaliacao;
    private Adotante adotante;
    private SolicitacaoAdocao solicitacao;
    private Abrigo abrigo;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getDataAvaliacao() {
        return dataAvaliacao;
    }

    public void setDataAvaliacao(LocalDateTime dataAvaliacao) {
        this.dataAvaliacao = dataAvaliacao;
    }

    public Adotante getAdotante() {
        return adotante;
    }

    public void setAdotante(Adotante adotante) {
        this.adotante = adotante;
    }

    public SolicitacaoAdocao getSolicitacao() {
        return solicitacao;
    }

    public void setSolicitacao(SolicitacaoAdocao solicitacao) {
        this.solicitacao = solicitacao;
    }

    public Abrigo getAbrigo() {
        return abrigo;
    }

    public void setAbrigo(Abrigo abrigo) {
        this.abrigo = abrigo;
    }
}
