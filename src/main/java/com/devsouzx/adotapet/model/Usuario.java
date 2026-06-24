package com.devsouzx.adotapet.model;

import java.time.LocalDateTime;

/**
 * Classe abstrata que representa um usuário do sistema.
 * Serve como base para {@link Adotante} e {@link Abrigo}.
 *
 * <p>Contém os atributos e métodos comuns a todos os usuários,
 * como nome, e-mail, senha e autenticação.</p>
 *
 * @author Equipe Adoção de Pets
 * @version 1.0
 * @since 2026
 * @see Adotante
 * @see Abrigo
 */
public abstract class Usuario {

    /** Identificador único do usuário */
    protected int id;

    /** Nome completo do usuário */
    protected String nome;

    /** E-mail do usuário (usado para login) */
    protected String email;

    /** Senha de acesso ao sistema */
    protected String senha;

    /** Telefone para contato */
    protected String telefone;

    /** Data e hora do cadastro no sistema */
    protected LocalDateTime dataCadastro;

    /**
     * Construtor para criar um novo usuário.
     *
     * @param nome Nome completo do usuário
     * @param email E-mail do usuário
     * @param senha Senha de acesso
     * @param telefone Telefone para contato
     */
    public Usuario(String nome, String email, String senha, String telefone) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.dataCadastro = LocalDateTime.now();
    }

    /**
     * Construtor para criar um usuário com ID pré-definido.
     *
     * @param id Identificador do usuário
     * @param nome Nome completo do usuário
     * @param email E-mail do usuário
     * @param senha Senha de acesso
     * @param telefone Telefone para contato
     */
    public Usuario(int id, String nome, String email, String senha, String telefone) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.dataCadastro = LocalDateTime.now();
    }

    /**
     * Autentica o usuário com base no e-mail e senha.
     *
     * @param email E-mail informado no login
     * @param senha Senha informada no login
     * @return {@code true} se as credenciais forem válidas, {@code false} caso contrário
     */
    public boolean autenticar(String email, String senha) {
        return this.email.equals(email) && this.senha.equals(senha);
    }

    /**
     * Retorna o tipo do usuário.
     *
     * @return "ADOTANTE" ou "ABRIGO"
     */
    public abstract String getTipoUsuario();

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
}