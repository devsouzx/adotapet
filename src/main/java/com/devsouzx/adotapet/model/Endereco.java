package com.devsouzx.adotapet.model;

/**
 * Classe que representa um endereço no sistema.
 * Utilizada por {@link Adotante} e {@link Abrigo}.
 *
 * <p>Contém informações de localização como logradouro, número,
 * bairro, cidade, estado e CEP.</p>
 *
 * @author Equipe Adoção de Pets
 * @version 1.0
 * @since 2026
 * @see Adotante
 * @see Abrigo
 */
public class Endereco {

    /** Identificador único do endereço */
    private int id;

    /** Nome da rua ou avenida */
    private String logradouro;

    /** Número do imóvel */
    private String numero;

    /** Bairro */
    private String bairro;

    /** Cidade */
    private String cidade;

    /** Estado (UF) */
    private String estado;

    /** Código Postal */
    private String cep;

    /**
     * Construtor para criar um novo endereço.
     *
     * @param logradouro Nome da rua ou avenida
     * @param numero Número do imóvel
     * @param bairro Bairro
     * @param cidade Cidade
     * @param estado Estado (UF)
     * @param cep Código Postal
     */
    public Endereco(String logradouro, String numero, String bairro,
                    String cidade, String estado, String cep) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }

    /**
     * Construtor para criar um endereço com ID pré-definido.
     *
     * @param id Identificador do endereço
     * @param logradouro Nome da rua ou avenida
     * @param numero Número do imóvel
     * @param bairro Bairro
     * @param cidade Cidade
     * @param estado Estado (UF)
     * @param cep Código Postal
     */
    public Endereco(int id, String logradouro, String numero, String bairro,
                    String cidade, String estado, String cep) {
        this.id = id;
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }

    /**
     * Retorna o endereço completo formatado.
     *
     * @return String com o endereço completo
     */
    public String getEnderecoCompleto() {
        return logradouro + ", " + numero + " - " + bairro + ", " +
                cidade + " - " + estado + ", CEP: " + cep;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getLogradouro() { return logradouro; }
    public void setLogradouro(String logradouro) { this.logradouro = logradouro; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
}