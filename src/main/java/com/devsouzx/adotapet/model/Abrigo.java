package com.devsouzx.adotapet.model;

import java.util.ArrayList;
import java.util.List;

public class Abrigo extends Usuario {
    private String cnpj;
    private String nomeResponsavel;
    private String horarioFuncionamento;
    private Endereco endereco;
    private List<Pet> pets;
    private List<Avaliacao> avaliacoesRecebidas;

    public Abrigo(int id, String nome, String email, String senha, String telefone,
                  String cnpj, String nomeResponsavel, Endereco endereco) {
        super(id, nome, email, senha, telefone);
        this.cnpj = cnpj;
        this.nomeResponsavel = nomeResponsavel;
        this.endereco = endereco;
        this.pets = new ArrayList<>();
        this.avaliacoesRecebidas = new ArrayList<>();
        this.horarioFuncionamento = "";
    }


    public String getTipoUsuario() {
        return "ABRIGO";
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    public String getHorarioFuncionamento() {
        return horarioFuncionamento;
    }

    public void setHorarioFuncionamento(String horarioFuncionamento) {
        this.horarioFuncionamento = horarioFuncionamento;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public List<Avaliacao> getAvaliacoesRecebidas() {
        return avaliacoesRecebidas;
    }

    public void setAvaliacoesRecebidas(List<Avaliacao> avaliacoesRecebidas) {
        this.avaliacoesRecebidas = avaliacoesRecebidas;
    }
}
