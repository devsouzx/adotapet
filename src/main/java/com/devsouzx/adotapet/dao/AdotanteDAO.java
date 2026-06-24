package com.devsouzx.adotapet.dao;

import com.devsouzx.adotapet.model.Adotante;
import com.devsouzx.adotapet.model.Endereco;
import com.devsouzx.adotapet.util.ConexaoBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de acesso a dados para a entidade {@link Adotante}.
 *
 * <p>Responsável pelas operações de CRUD (Create, Read, Update, Delete)
 * no banco de dados para a tabela {@code adotante}.</p>
 *
 * <p>Métodos disponíveis:</p>
 * <ul>
 *   <li>{@link #inserir(Adotante)} - Cadastra um novo adotante</li>
 *   <li>{@link #buscarPorId(int)} - Busca um adotante pelo ID</li>
 *   <li>{@link #buscarPorEmail(String)} - Busca um adotante pelo e-mail</li>
 *   <li>{@link #listarTodos()} - Lista todos os adotantes</li>
 *   <li>{@link #atualizar(Adotante)} - Atualiza os dados de um adotante</li>
 *   <li>{@link #excluir(int)} - Remove um adotante do sistema</li>
 * </ul>
 *
 *
 * @author Equipe Adoção de Pets
 * @version 1.0
 * @since 2026
 * @see Adotante
 */
public class AdotanteDAO {

    /**
     * Insere um novo adotante no banco de dados.
     *
     * @param adotante Objeto Adotante a ser inserido
     * @throws SQLException Se houver erro na execução da query
     */
    public void inserir(Adotante adotante) throws SQLException {
        String sql = "INSERT INTO adotante (nome, email, senha, telefone, cpf, data_nascimento, endereco_id, data_cadastro) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, adotante.getNome());
            pstmt.setString(2, adotante.getEmail());
            pstmt.setString(3, adotante.getSenha());
            pstmt.setString(4, adotante.getTelefone());
            pstmt.setString(5, adotante.getCpf());
            pstmt.setDate(6, Date.valueOf(adotante.getDataNascimento()));
            pstmt.setInt(7, adotante.getEndereco().getId());
            pstmt.setTimestamp(8, Timestamp.valueOf(adotante.getDataCadastro()));

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    adotante.setId(rs.getInt(1));
                }
            }
        }
    }

    /**
     * Busca um adotante pelo seu ID.
     *
     * @param id ID do adotante
     * @return Objeto Adotante encontrado, ou {@code null} se não existir
     */
    public Adotante buscarPorId(int id) {
        String sql = "SELECT * FROM adotante WHERE id = ?";
        Adotante adotante = null;

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("nome");
                    String email = rs.getString("email");
                    String senha = rs.getString("senha");
                    String telefone = rs.getString("telefone");
                    String cpf = rs.getString("cpf");
                    Date dataNasc = rs.getDate("data_nascimento");
                    int enderecoId = rs.getInt("endereco_id");

                    EnderecoDAO enderecoDAO = new EnderecoDAO();
                    Endereco endereco = enderecoDAO.buscarPorId(enderecoId);

                    adotante = new Adotante(id, nome, email, senha, telefone, cpf, dataNasc.toLocalDate(), endereco);
                    adotante.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao buscar adotante: " + e.getMessage());
        }
        return adotante;
    }

    /**
     * Busca um adotante pelo seu e-mail.
     *
     * @param email E-mail do adotante
     * @return Objeto Adotante encontrado, ou {@code null} se não existir
     */
    public Adotante buscarPorEmail(String email) {
        String sql = "SELECT * FROM adotante WHERE email = ?";
        Adotante adotante = null;

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    String senha = rs.getString("senha");
                    String telefone = rs.getString("telefone");
                    String cpf = rs.getString("cpf");
                    Date dataNasc = rs.getDate("data_nascimento");
                    int enderecoId = rs.getInt("endereco_id");

                    EnderecoDAO enderecoDAO = new EnderecoDAO();
                    Endereco endereco = enderecoDAO.buscarPorId(enderecoId);

                    adotante = new Adotante(id, nome, email, senha, telefone, cpf, dataNasc.toLocalDate(), endereco);
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao buscar adotante: " + e.getMessage());
        }
        return adotante;
    }

    /**
     * Lista todos os adotantes cadastrados.
     *
     * @return Lista de todos os adotantes
     */
    public List<Adotante> listarTodos() {
        List<Adotante> lista = new ArrayList<>();
        String sql = "SELECT * FROM adotante";

        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String email = rs.getString("email");
                String senha = rs.getString("senha");
                String telefone = rs.getString("telefone");
                String cpf = rs.getString("cpf");
                Date dataNasc = rs.getDate("data_nascimento");

                Adotante adotante = new Adotante(id, nome, email, senha, telefone, cpf, dataNasc.toLocalDate(), null);
                lista.add(adotante);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar adotantes: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Atualiza os dados de um adotante existente.
     *
     * @param adotante Objeto Adotante com os dados atualizados
     * @throws SQLException Se houver erro na execução da query
     */
    public void atualizar(Adotante adotante) throws SQLException {
        String sql = "UPDATE adotante SET nome = ?, email = ?, senha = ?, telefone = ?, cpf = ?, data_nascimento = ? WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, adotante.getNome());
            pstmt.setString(2, adotante.getEmail());
            pstmt.setString(3, adotante.getSenha());
            pstmt.setString(4, adotante.getTelefone());
            pstmt.setString(5, adotante.getCpf());
            pstmt.setDate(6, Date.valueOf(adotante.getDataNascimento()));
            pstmt.setInt(7, adotante.getId());

            pstmt.executeUpdate();
        }
    }

    /**
     * Exclui um adotante do sistema pelo seu ID.
     *
     * @param id ID do adotante a ser excluído
     * @throws SQLException Se houver erro na execução da query
     */
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM adotante WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}