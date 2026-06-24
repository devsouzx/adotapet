package com.devsouzx.adotapet.model;

import com.devsouzx.adotapet.model.enums.Porte;
import com.devsouzx.adotapet.model.enums.StatusPet;
import com.devsouzx.adotapet.model.enums.StatusSolicitacao;
import com.devsouzx.adotapet.exception.AdocaoException;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um abrigo de animais no sistema.
 * Herda de {@link Usuario} e contém informações específicas do abrigo
 * como CNPJ, responsável e lista de pets.
 *
 * <p>Um abrigo pode cadastrar pets, gerenciar solicitações de adoção
 * e receber avaliações de adotantes.</p>
 *
 * @author Equipe Adoção de Pets
 * @version 1.0
 * @since 2026
 * @see Usuario
 * @see Pet
 * @see SolicitacaoAdocao
 */
public class Abrigo extends Usuario {

    /** CNPJ do abrigo */
    private String cnpj;

    /** Nome do responsável pelo abrigo */
    private String nomeResponsavel;

    /** Horário de funcionamento do abrigo */
    private String horarioFuncionamento;

    /** Endereço do abrigo */
    private Endereco endereco;

    /** Lista de pets cadastrados no abrigo */
    private List<Pet> pets;

    /** Lista de avaliações recebidas pelo abrigo */
    private List<Avaliacao> avaliacoesRecebidas;

    /**
     * Construtor para criar um novo abrigo.
     *
     * @param nome Nome do abrigo
     * @param email E-mail do abrigo
     * @param senha Senha de acesso
     * @param telefone Telefone para contato
     * @param cnpj CNPJ do abrigo
     * @param nomeResponsavel Nome do responsável
     * @param endereco Endereço do abrigo
     */
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

    /**
     * Construtor para criar um abrigo com ID pré-definido.
     *
     * @param id Identificador do abrigo
     * @param nome Nome do abrigo
     * @param email E-mail do abrigo
     * @param senha Senha de acesso
     * @param telefone Telefone para contato
     * @param cnpj CNPJ do abrigo
     * @param nomeResponsavel Nome do responsável
     * @param endereco Endereço do abrigo
     */
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

    /**
     * Cadastra um novo pet no abrigo.
     * O pet é criado com status "Disponível" automaticamente.
     *
     * @param nome Nome do pet
     * @param especie Espécie do pet (Cachorro, Gato, Outro)
     * @param raca Raça do pet
     * @param idadeMeses Idade do pet em meses
     * @param porte Porte do pet (Pequeno, Médio, Grande)
     * @param descricao Descrição detalhada do pet
     * @param foto URL da foto do pet (opcional)
     * @see Pet
     */
    public void cadastrarPet(String nome, String especie, String raca, int idadeMeses,
                             Porte porte, String descricao, String foto) {
        Pet pet = new Pet(nome, especie, raca, idadeMeses, porte, descricao, foto, this);
        pets.add(pet);
    }

    /**
     * Remove um pet do abrigo.
     * O pet só pode ser removido se não tiver solicitações pendentes.
     *
     * @param idPet ID do pet a ser removido
     * @throws AdocaoException Se o pet não for encontrado ou tiver solicitação pendente
     */
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

    /**
     * Aprova uma solicitação de adoção pendente.
     * O status do pet é alterado para "Adotado".
     *
     * @param idSolicitacao ID da solicitação a ser aprovada
     * @throws AdocaoException Se a solicitação não for encontrada ou já estiver respondida
     */
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

    /**
     * Recusa uma solicitação de adoção pendente.
     *
     * @param idSolicitacao ID da solicitação a ser recusada
     * @param justificativa Motivo da recusa (obrigatório)
     * @throws AdocaoException Se a solicitação não for encontrada, já estiver respondida
     *                         ou a justificativa estiver vazia
     */
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

    /**
     * Retorna todas as solicitações recebidas pelo abrigo.
     *
     * @return Lista de todas as solicitações dos pets do abrigo
     */
    public List<SolicitacaoAdocao> getSolicitacoesRecebidas() {
        List<SolicitacaoAdocao> todas = new ArrayList<>();
        for (Pet pet : pets) {
            todas.addAll(pet.getSolicitacoes());
        }
        return todas;
    }

    /**
     * Retorna as solicitações pendentes do abrigo.
     *
     * @return Lista de solicitações com status PENDENTE
     */
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

    /**
     * Retorna os pets disponíveis do abrigo.
     *
     * @return Lista de pets com status DISPONIVEL
     */
    public List<Pet> getPetsDisponiveis() {
        List<Pet> disponiveis = new ArrayList<>();
        for (Pet pet : pets) {
            if (pet.getStatus() == StatusPet.DISPONIVEL) {
                disponiveis.add(pet);
            }
        }
        return disponiveis;
    }

    /**
     * Retorna os pets adotados do abrigo.
     *
     * @return Lista de pets com status ADOTADO
     */
    public List<Pet> getPetsAdotados() {
        List<Pet> adotados = new ArrayList<>();
        for (Pet pet : pets) {
            if (pet.getStatus() == StatusPet.ADOTADO) {
                adotados.add(pet);
            }
        }
        return adotados;
    }

    /**
     * Adiciona uma avaliação ao abrigo.
     *
     * @param avaliacao Avaliação a ser adicionada
     */
    public void adicionarAvaliacao(Avaliacao avaliacao) {
        avaliacoesRecebidas.add(avaliacao);
    }

    @Override
    public String getTipoUsuario() {
        return "ABRIGO";
    }

    // Getters e Setters
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