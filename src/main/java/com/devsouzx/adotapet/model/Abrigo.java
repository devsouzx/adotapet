package com.devsouzx.adotapet.model;

import com.devsouzx.adotapet.model.enums.Porte;
import com.devsouzx.adotapet.model.enums.StatusPet;
import com.devsouzx.adotapet.model.enums.StatusSolicitacao;
import com.devsouzx.adotapet.exception.AdocaoException;

import java.util.ArrayList;
import java.util.List;

public class Abrigo extends Usuario {
    private String cnpj;
    private String nomeResponsavel;
    private String horarioFuncionamento;
    private Endereco endereco;
    private List<Pet> pets;
    private List<Avaliacao> avaliacoesRecebidas;

    public Abrigo(String nome, String email, String senha, String telefone,
                  String cnpj, String nomeResponsavel, Endereco endereco) {
        super(nome, email, senha, telefone);
        this.cnpj = cnpj;
        this.nomeResponsavel = nomeResponsavel;
        this.endereco = endereco;
        this.pets = new ArrayList<>();
        this.avaliacoesRecebidas = new ArrayList<>();
        this.horarioFuncionamento = "Segunda a Sexta, 9h às 18h";
    }

    public Abrigo(int id, String nome, String email, String senha, String telefone,
                  String cnpj, String nomeResponsavel, Endereco endereco) {
        super(id, nome, email, senha, telefone);
        this.cnpj = cnpj;
        this.nomeResponsavel = nomeResponsavel;
        this.endereco = endereco;
        this.pets = new ArrayList<>();
        this.avaliacoesRecebidas = new ArrayList<>();
        this.horarioFuncionamento = "Segunda a Sexta, 9h às 18h";
    }

    public void cadastrarPet(String nome, String especie, String raca, int idadeMeses,
                             Porte porte, String descricao, String foto) {
        Pet pet = new Pet(nome, especie, raca, idadeMeses, porte, descricao, foto, this);
        pets.add(pet);
    }

    public void removerPet(int idPet) throws AdocaoException {
        Pet petRemover = null;
        for (Pet pet : pets) {
            if (pet.getId() == idPet) {
                petRemover = pet;
                break;
            }
        }
        if (petRemover == null) {
            throw new AdocaoException("Pet não encontrado.");
        }
        if (petRemover.temSolicitacaoPendente()) {
            throw new AdocaoException("Não é possível remover este pet pois ele possui solicitações de adoção pendentes.");
        }
        pets.remove(petRemover);
    }

    public void aprovarSolicitacao(int idSolicitacao) throws AdocaoException {
        for (Pet pet : pets) {
            for (SolicitacaoAdocao sol : pet.getSolicitacoes()) {
                if (sol.getId() == idSolicitacao) {
                    if (sol.getStatus() != StatusSolicitacao.PENDENTE) {
                        throw new AdocaoException("Esta solicitação já foi respondida.");
                    }
                    sol.aprovar();
                    pet.adotar();
                    return;
                }
            }
        }
        throw new AdocaoException("Solicitação não encontrada.");
    }

    public void recusarSolicitacao(int idSolicitacao, String justificativa) throws AdocaoException {
        for (Pet pet : pets) {
            for (SolicitacaoAdocao sol : pet.getSolicitacoes()) {
                if (sol.getId() == idSolicitacao) {
                    if (sol.getStatus() != StatusSolicitacao.PENDENTE) {
                        throw new AdocaoException("Esta solicitação já foi respondida.");
                    }
                    if (justificativa == null || justificativa.trim().isEmpty()) {
                        throw new AdocaoException("Por favor, informe uma justificativa para a recusa.");
                    }
                    sol.recusar(justificativa);
                    return;
                }
            }
        }
        throw new AdocaoException("Solicitação não encontrada.");
    }

    public List<SolicitacaoAdocao> getSolicitacoesRecebidas() {
        List<SolicitacaoAdocao> todas = new ArrayList<>();
        for (Pet pet : pets) {
            todas.addAll(pet.getSolicitacoes());
        }
        return todas;
    }

    public List<SolicitacaoAdocao> getSolicitacoesPendentes() {
        List<SolicitacaoAdocao> pendentes = new ArrayList<>();
        for (Pet pet : pets) {
            for (SolicitacaoAdocao sol : pet.getSolicitacoes()) {
                if (sol.getStatus() == StatusSolicitacao.PENDENTE) {
                    pendentes.add(sol);
                }
            }
        }
        return pendentes;
    }

    public List<Pet> getPetsDisponiveis() {
        List<Pet> disponiveis = new ArrayList<>();
        for (Pet pet : pets) {
            if (pet.getStatus() == StatusPet.DISPONIVEL) {
                disponiveis.add(pet);
            }
        }
        return disponiveis;
    }

    public List<Pet> getPetsAdotados() {
        List<Pet> adotados = new ArrayList<>();
        for (Pet pet : pets) {
            if (pet.getStatus() == StatusPet.ADOTADO) {
                adotados.add(pet);
            }
        }
        return adotados;
    }

    public void adicionarAvaliacao(Avaliacao avaliacao) {
        avaliacoesRecebidas.add(avaliacao);
    }

    @Override
    public String getTipoUsuario() {
        return "ABRIGO";
    }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public String getNomeResponsavel() { return nomeResponsavel; }
    public void setNomeResponsavel(String nomeResponsavel) { this.nomeResponsavel = nomeResponsavel; }
    public String getHorarioFuncionamento() { return horarioFuncionamento; }
    public void setHorarioFuncionamento(String horarioFuncionamento) { this.horarioFuncionamento = horarioFuncionamento; }
    public Endereco getEndereco() { return endereco; }
    public void setEndereco(Endereco endereco) { this.endereco = endereco; }
    public List<Pet> getPets() { return pets; }
    public void setPets(List<Pet> pets) { this.pets = pets; }
    public List<Avaliacao> getAvaliacoesRecebidas() { return avaliacoesRecebidas; }
    public void setAvaliacoesRecebidas(List<Avaliacao> avaliacoesRecebidas) { this.avaliacoesRecebidas = avaliacoesRecebidas; }
}