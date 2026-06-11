package com.devsouzx.adotapet.model;

import com.devsouzx.adotapet.model.enums.Porte;
import com.devsouzx.adotapet.model.enums.StatusPet;
import com.devsouzx.adotapet.model.enums.StatusSolicitacao;

import java.util.ArrayList;
import java.util.List;

public class Pet {
    private int id;
    private String nome;
    private String especie;
    private String raca;
    private int idadeMeses;
    private Porte porte;
    private String descricao;
    private String foto;
    private StatusPet status;
    private Abrigo abrigo;
    private List<SolicitacaoAdocao> solicitacoes;

    public Pet(String nome, String especie, String raca, int idadeMeses,
               Porte porte, String descricao, String foto, Abrigo abrigo) {
        this.nome = nome;
        this.especie = especie;
        this.raca = raca;
        this.idadeMeses = idadeMeses;
        this.porte = porte;
        this.descricao = descricao;
        this.foto = foto;
        this.status = StatusPet.DISPONIVEL;
        this.abrigo = abrigo;
        this.solicitacoes = new ArrayList<>();
    }

    public Pet(int id, String nome, String especie, String raca, int idadeMeses,
               Porte porte, String descricao, String foto, StatusPet status, Abrigo abrigo) {
        this.id = id;
        this.nome = nome;
        this.especie = especie;
        this.raca = raca;
        this.idadeMeses = idadeMeses;
        this.porte = porte;
        this.descricao = descricao;
        this.foto = foto;
        this.status = status;
        this.abrigo = abrigo;
        this.solicitacoes = new ArrayList<>();
    }

    public boolean isDisponivel() {
        return status == StatusPet.DISPONIVEL;
    }

    public void adotar() {
        this.status = StatusPet.ADOTADO;
    }

    public boolean temSolicitacaoPendente() {
        for (SolicitacaoAdocao sol : solicitacoes) {
            if (sol.getStatus() == StatusSolicitacao.PENDENTE) {
                return true;
            }
        }
        return false;
    }

    public void adicionarSolicitacao(SolicitacaoAdocao solicitacao) {
        solicitacoes.add(solicitacao);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }
    public String getRaca() { return raca; }
    public void setRaca(String raca) { this.raca = raca; }
    public int getIdadeMeses() { return idadeMeses; }
    public void setIdadeMeses(int idadeMeses) { this.idadeMeses = idadeMeses; }
    public Porte getPorte() { return porte; }
    public void setPorte(Porte porte) { this.porte = porte; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }
    public StatusPet getStatus() { return status; }
    public void setStatus(StatusPet status) { this.status = status; }
    public Abrigo getAbrigo() { return abrigo; }
    public void setAbrigo(Abrigo abrigo) { this.abrigo = abrigo; }
    public List<SolicitacaoAdocao> getSolicitacoes() { return solicitacoes; }
    public void setSolicitacoes(List<SolicitacaoAdocao> solicitacoes) { this.solicitacoes = solicitacoes; }
}