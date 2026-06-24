package com.devsouzx.adotapet.model;

import com.devsouzx.adotapet.model.enums.StatusSolicitacao;
import com.devsouzx.adotapet.exception.AdocaoException;
import com.devsouzx.adotapet.dao.SolicitacaoDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um adotante no sistema.
 * Herda de {@link Usuario} e contém informações específicas do adotante
 * como CPF, data de nascimento e endereço.
 *
 * <p>Um adotante pode solicitar adoções, cancelar solicitações pendentes
 * e avaliar abrigos após uma adoção ser aprovada.</p>
 *
 * @author Equipe Adoção de Pets
 * @version 1.0
 * @since 2026
 * @see Usuario
 * @see SolicitacaoAdocao
 * @see Avaliacao
 */
public class Adotante extends Usuario {

    /** CPF do adotante */
    private String cpf;

    /** Data de nascimento do adotante */
    private LocalDate dataNascimento;

    /** Endereço do adotante */
    private Endereco endereco;

    /** Lista de solicitações de adoção feitas pelo adotante */
    private List<SolicitacaoAdocao> solicitacoes;

    /** Lista de avaliações feitas pelo adotante */
    private List<Avaliacao> avaliacoes;

    /**
     * Construtor para criar um novo adotante.
     *
     * @param nome Nome completo do adotante
     * @param email E-mail do adotante
     * @param senha Senha de acesso
     * @param telefone Telefone para contato
     * @param cpf CPF do adotante
     * @param dataNascimento Data de nascimento
     * @param endereco Endereço do adotante
     */
    public Adotante(String nome, String email, String senha, String telefone,
                    String cpf, LocalDate dataNascimento, Endereco endereco) {
        super(nome, email, senha, telefone);
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.endereco = endereco;
        this.solicitacoes = new ArrayList<>();
        this.avaliacoes = new ArrayList<>();
    }

    /**
     * Construtor para criar um adotante com ID pré-definido.
     *
     * @param id Identificador do adotante
     * @param nome Nome completo do adotante
     * @param email E-mail do adotante
     * @param senha Senha de acesso
     * @param telefone Telefone para contato
     * @param cpf CPF do adotante
     * @param dataNascimento Data de nascimento
     * @param endereco Endereço do adotante
     */
    public Adotante(int id, String nome, String email, String senha, String telefone,
                    String cpf, LocalDate dataNascimento, Endereco endereco) {
        super(id, nome, email, senha, telefone);
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.endereco = endereco;
        this.solicitacoes = new ArrayList<>();
        this.avaliacoes = new ArrayList<>();
    }

    /**
     * Realiza uma solicitação de adoção para um pet.
     *
     * <p>Validações realizadas:</p>
     * <ul>
     *   <li>Adotante não pode ter mais de 3 solicitações pendentes</li>
     *   <li>Pet deve estar disponível</li>
     *   <li>Pet não pode ter solicitação pendente de outro adotante</li>
     * </ul>
     *
     *
     * @param pet Pet que se deseja adotar
     * @throws AdocaoException Se alguma validação falhar
     * @throws SQLException Se houver erro ao salvar no banco
     */
    public void solicitarAdocao(Pet pet) throws AdocaoException, SQLException {
        if (getSolicitacoesPendentes() >= 3) {
            throw new AdocaoException("Você já possui 3 solicitações pendentes.");
        }
        if (!pet.isDisponivel()) {
            throw new AdocaoException("Este pet não está mais disponível para adoção.");
        }
        if (pet.temSolicitacaoPendente()) {
            throw new AdocaoException("Este pet já foi solicitado por outro adotante.");
        }

        SolicitacaoAdocao solicitacao = new SolicitacaoAdocao(this, pet);
        solicitacoes.add(solicitacao);
        pet.adicionarSolicitacao(solicitacao);

        SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
        solicitacaoDAO.inserir(solicitacao);
    }

    /**
     * Cancela uma solicitação de adoção pendente.
     *
     * @param idSolicitacao ID da solicitação a ser cancelada
     * @throws AdocaoException Se a solicitação não for encontrada ou já estiver respondida
     */
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

    /**
     * Retorna a quantidade de solicitações pendentes do adotante.
     *
     * @return Número de solicitações com status PENDENTE
     */
    public int getSolicitacoesPendentes() {
        int count = 0;
        for (SolicitacaoAdocao sol : solicitacoes) {
            if (sol.getStatus() == StatusSolicitacao.PENDENTE) {
                count++;
            }
        }
        return count;
    }

    /**
     * Retorna as solicitações do adotante com um status específico.
     *
     * @param status Status das solicitações desejadas
     * @return Lista de solicitações com o status informado
     */
    public List<SolicitacaoAdocao> getSolicitacoesPorStatus(StatusSolicitacao status) {
        List<SolicitacaoAdocao> resultado = new ArrayList<>();
        for (SolicitacaoAdocao sol : solicitacoes) {
            if (sol.getStatus() == status) {
                resultado.add(sol);
            }
        }
        return resultado;
    }

    /**
     * Avalia um abrigo após uma adoção ser aprovada.
     *
     * @param solicitacao Solicitação de adoção aprovada
     * @param nota Nota de 1 a 5 estrelas
     * @param comentario Comentário opcional sobre a experiência
     * @throws AdocaoException Se a solicitação não estiver aprovada
     */
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

    // Getters e Setters
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