package com.devsouzx.adotapet.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Adotante extends Usuario {
    private String cpf;
    private LocalDate dataNascimento;
    private Endereco endereco;
    private List<SolicitacaoAdocao> solicitacoes;
    private List<Avaliacao> avaliacoes;

    public Adotante(int id, String nome, String email, String senha, String telefone,
                    String cpf, LocalDate dataNascimento, Endereco endereco) {
        super(id, nome, email, senha, telefone);
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.endereco = endereco;
        this.solicitacoes = new ArrayList<>();
        this.avaliacoes = new ArrayList<>();
    }

    public String getTipoUsuario() {
        return "ADOTANTE";
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public List<SolicitacaoAdocao> getSolicitacoes() {
        return solicitacoes;
    }

    public void setSolicitacoes(List<SolicitacaoAdocao> solicitacoes) {
        this.solicitacoes = solicitacoes;
    }

    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<Avaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }
}
