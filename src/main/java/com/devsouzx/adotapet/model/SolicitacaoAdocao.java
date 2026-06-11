package com.devsouzx.adotapet.model;

import com.devsouzx.adotapet.model.enums.StatusSolicitacao;

import java.time.LocalDateTime;

public class SolicitacaoAdocao {
    private int id;
    private LocalDateTime dataSolicitacao;
    private LocalDateTime dataResposta;
    private StatusSolicitacao status;
    private String justificativa;
    private Adotante adotante;
    private Pet pet;

    public SolicitacaoAdocao(Adotante adotante, Pet pet) {
        this.dataSolicitacao = LocalDateTime.now();
        this.status = StatusSolicitacao.PENDENTE;
        this.adotante = adotante;
        this.pet = pet;
    }

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

    public void aprovar() {
        this.status = StatusSolicitacao.APROVADA;
        this.dataResposta = LocalDateTime.now();
    }

    public void recusar(String justificativa) {
        this.status = StatusSolicitacao.RECUSADA;
        this.dataResposta = LocalDateTime.now();
        this.justificativa = justificativa;
    }

    public void cancelar() {
        this.status = StatusSolicitacao.CANCELADA_PELO_ADOTANTE;
        this.dataResposta = LocalDateTime.now();
    }

    public boolean isPendente() {
        return status == StatusSolicitacao.PENDENTE;
    }

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