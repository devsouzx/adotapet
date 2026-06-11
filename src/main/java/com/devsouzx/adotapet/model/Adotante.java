package com.devsouzx.adotapet.model;

import com.devsouzx.adotapet.model.enums.StatusSolicitacao;
import com.devsouzx.adotapet.exception.AdocaoException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Adotante extends Usuario {
    private String cpf;
    private LocalDate dataNascimento;
    private Endereco endereco;
    private List<SolicitacaoAdocao> solicitacoes;
    private List<Avaliacao> avaliacoes;

    public Adotante(String nome, String email, String senha, String telefone,
                    String cpf, LocalDate dataNascimento, Endereco endereco) {
        super(nome, email, senha, telefone);
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.endereco = endereco;
        this.solicitacoes = new ArrayList<>();
        this.avaliacoes = new ArrayList<>();
    }

    public Adotante(int id, String nome, String email, String senha, String telefone,
                    String cpf, LocalDate dataNascimento, Endereco endereco) {
        super(id, nome, email, senha, telefone);
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.endereco = endereco;
        this.solicitacoes = new ArrayList<>();
        this.avaliacoes = new ArrayList<>();
    }

    public void solicitarAdocao(Pet pet) throws AdocaoException {
        if (getSolicitacoesPendentes() >= 3) {
            throw new AdocaoException("Você já possui 3 solicitações pendentes. Aguarde a resposta dos abrigos.");
        }
        if (!pet.isDisponivel()) {
            throw new AdocaoException("Este pet não está mais disponível para adoção.");
        }
        if (pet.temSolicitacaoPendente()) {
            throw new AdocaoException("Este pet já foi solicitado por outro adotante e está aguardando análise.");
        }

        SolicitacaoAdocao solicitacao = new SolicitacaoAdocao(this, pet);
        solicitacoes.add(solicitacao);
        pet.adicionarSolicitacao(solicitacao);
    }

    public void cancelarSolicitacao(int idSolicitacao) throws AdocaoException {
        for (SolicitacaoAdocao sol : solicitacoes) {
            if (sol.getId() == idSolicitacao) {
                if (sol.getStatus() != StatusSolicitacao.PENDENTE) {
                    throw new AdocaoException("Não é possível cancelar uma solicitação que já foi respondida.");
                }
                sol.cancelar();
                return;
            }
        }
        throw new AdocaoException("Solicitação não encontrada.");
    }

    public int getSolicitacoesPendentes() {
        int count = 0;
        for (SolicitacaoAdocao sol : solicitacoes) {
            if (sol.getStatus() == StatusSolicitacao.PENDENTE) {
                count++;
            }
        }
        return count;
    }

    public List<SolicitacaoAdocao> getSolicitacoesPorStatus(StatusSolicitacao status) {
        List<SolicitacaoAdocao> resultado = new ArrayList<>();
        for (SolicitacaoAdocao sol : solicitacoes) {
            if (sol.getStatus() == status) {
                resultado.add(sol);
            }
        }
        return resultado;
    }

    public void avaliarAbrigo(SolicitacaoAdocao solicitacao, int nota, String comentario) throws AdocaoException {
        if (solicitacao.getStatus() != StatusSolicitacao.APROVADA) {
            throw new AdocaoException("Só é possível avaliar uma adoção que foi aprovada.");
        }
        Avaliacao avaliacao = new Avaliacao(this, solicitacao.getPet().getAbrigo(), solicitacao, nota, comentario);
        avaliacoes.add(avaliacao);
        solicitacao.getPet().getAbrigo().adicionarAvaliacao(avaliacao);
    }

    @Override
    public String getTipoUsuario() {
        return "ADOTANTE";
    }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    public Endereco getEndereco() { return endereco; }
    public void setEndereco(Endereco endereco) { this.endereco = endereco; }
    public List<SolicitacaoAdocao> getSolicitacoes() { return solicitacoes; }
    public void setSolicitacoes(List<SolicitacaoAdocao> solicitacoes) { this.solicitacoes = solicitacoes; }
    public List<Avaliacao> getAvaliacoes() { return avaliacoes; }
    public void setAvaliacoes(List<Avaliacao> avaliacoes) { this.avaliacoes = avaliacoes; }
}