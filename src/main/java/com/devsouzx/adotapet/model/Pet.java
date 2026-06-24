package com.devsouzx.adotapet.model;

import com.devsouzx.adotapet.model.enums.Porte;
import com.devsouzx.adotapet.model.enums.StatusPet;
import com.devsouzx.adotapet.model.enums.StatusSolicitacao;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um pet disponível para adoção.
 *
 * <p>Um pet pertence a um abrigo e pode receber solicitações de adoção.
 * Seu status pode ser "Disponível" ou "Adotado".</p>
 *
 * @author Equipe Adoção de Pets
 * @version 1.0
 * @since 2026
 * @see Abrigo
 * @see SolicitacaoAdocao
 */
public class Pet {

    /** Identificador único do pet */
    private int id;

    /** Nome do pet */
    private String nome;

    /** Espécie do pet (Cachorro, Gato, Outro) */
    private String especie;

    /** Raça do pet */
    private String raca;

    /** Idade do pet em meses */
    private int idadeMeses;

    /** Porte do pet (Pequeno, Médio, Grande) */
    private Porte porte;

    /** Descrição detalhada do pet */
    private String descricao;

    /** URL da foto do pet (opcional) */
    private String foto;

    /** Status do pet (Disponível, Adotado) */
    private StatusPet status;

    /** Abrigo responsável pelo pet */
    private Abrigo abrigo;

    /** Lista de solicitações de adoção recebidas pelo pet */
    private List<SolicitacaoAdocao> solicitacoes;

    /**
     * Construtor para criar um novo pet.
     * O pet é criado com status "Disponível" automaticamente.
     *
     * @param nome Nome do pet
     * @param especie Espécie do pet
     * @param raca Raça do pet
     * @param idadeMeses Idade em meses
     * @param porte Porte do pet
     * @param descricao Descrição detalhada
     * @param foto URL da foto (opcional)
     * @param abrigo Abrigo responsável
     */
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

    /**
     * Construtor para criar um pet com ID pré-definido.
     *
     * @param id Identificador do pet
     * @param nome Nome do pet
     * @param especie Espécie do pet
     * @param raca Raça do pet
     * @param idadeMeses Idade em meses
     * @param porte Porte do pet
     * @param descricao Descrição detalhada
     * @param foto URL da foto
     * @param status Status do pet
     * @param abrigo Abrigo responsável
     */
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

    /**
     * Verifica se o pet está disponível para adoção.
     *
     * @return {@code true} se o status for DISPONIVEL
     */
    public boolean isDisponivel() {
        return status == StatusPet.DISPONIVEL;
    }

    /**
     * Marca o pet como adotado.
     * Altera o status para ADOTADO.
     */
    public void adotar() {
        this.status = StatusPet.ADOTADO;
    }

    /**
     * Verifica se o pet possui alguma solicitação pendente.
     *
     * @return {@code true} se houver solicitação com status PENDENTE
     */
    public boolean temSolicitacaoPendente() {
        for (SolicitacaoAdocao sol : solicitacoes) {
            if (sol.getStatus() == StatusSolicitacao.PENDENTE) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adiciona uma solicitação de adoção ao pet.
     *
     * @param solicitacao Solicitação a ser adicionada
     */
    public void adicionarSolicitacao(SolicitacaoAdocao solicitacao) {
        solicitacoes.add(solicitacao);
    }

    // Getters e Setters
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