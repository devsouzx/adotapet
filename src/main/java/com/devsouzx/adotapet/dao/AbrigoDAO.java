package com.devsouzx.adotapet.dao;

import com.devsouzx.adotapet.model.Abrigo;
import com.devsouzx.adotapet.model.Endereco;
import com.devsouzx.adotapet.util.ConexaoBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de acesso a dados para a entidade {@link Abrigo}.
 *
 * <p>Responsável pelas operações de CRUD (Create, Read, Update, Delete)
 * no banco de dados para a tabela {@code abrigo}.</p>
 *
 * <p>Métodos disponíveis:</p>
 * <ul>
 *   <li>{@link #inserir(Abrigo)} - Cadastra um novo abrigo</li>
 *   <li>{@link #buscarPorId(int)} - Busca um abrigo pelo ID</li>
 *   <li>{@link #buscarPorEmail(String)} - Busca um abrigo pelo e-mail</li>
 *   <li>{@link #listarTodos()} - Lista todos os abrigos</li>
 *   <li>{@link #atualizar(Abrigo)} - Atualiza os dados de um abrigo</li>
 *   <li>{@link #excluir(int)} - Remove um abrigo do sistema</li>
 * </ul>
 *
 *
 * @author Equipe Adoção de Pets
 * @version 1.0
 * @since 2026
 * @see Abrigo
 */
public class AbrigoDAO {

    /**
     * Insere um novo abrigo no banco de dados.
     *
     * @param abrigo Objeto Abrigo a ser inserido
     * @throws SQLException Se houver erro na execução da query
     */
    public void inserir(Abrigo abrigo) throws SQLException {
        String sql = "INSERT INTO abrigo (nome, email, senha, telefone, cnpj, nome_responsavel, horario_funcionamento, endereco_id, data_cadastro) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, abrigo.getNome());
            pstmt.setString(2, abrigo.getEmail());
            pstmt.setString(3, abrigo.getSenha());
            pstmt.setString(4, abrigo.getTelefone());
            pstmt.setString(5, abrigo.getCnpj());
            pstmt.setString(6, abrigo.getNomeResponsavel());
            pstmt.setString(7, abrigo.getHorarioFuncionamento());
            pstmt.setInt(8, abrigo.getEndereco().getId());
            pstmt.setTimestamp(9, Timestamp.valueOf(abrigo.getDataCadastro()));

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    abrigo.setId(rs.getInt(1));
                }
            }
        }
    }

    /**
     * Busca um abrigo pelo seu ID.
     *
     * @param id ID do abrigo
     * @return Objeto Abrigo encontrado, ou {@code null} se não existir
     */
    public Abrigo buscarPorId(int id) {
        String sql = "SELECT * FROM abrigo WHERE id = ?";
        Abrigo abrigo = null;

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("nome");
                    String email = rs.getString("email");
                    String senha = rs.getString("senha");
                    String telefone = rs.getString("telefone");
                    String cnpj = rs.getString("cnpj");
                    String nomeResponsavel = rs.getString("nome_responsavel");
                    String horarioFuncionamento = rs.getString("horario_funcionamento");
                    int enderecoId = rs.getInt("endereco_id");

                    EnderecoDAO enderecoDAO = new EnderecoDAO();
                    Endereco endereco = enderecoDAO.buscarPorId(enderecoId);

                    abrigo = new Abrigo(id, nome, email, senha, telefone, cnpj, nomeResponsavel, endereco);
                    abrigo.setHorarioFuncionamento(horarioFuncionamento);
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao buscar abrigo: " + e.getMessage());
        }
        return abrigo;
    }

    /**
     * Busca um abrigo pelo seu e-mail.
     *
     * @param email E-mail do abrigo
     * @return Objeto Abrigo encontrado, ou {@code null} se não existir
     */
    public Abrigo buscarPorEmail(String email) {
        String sql = "SELECT * FROM abrigo WHERE email = ?";
        Abrigo abrigo = null;

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    String senha = rs.getString("senha");
                    String telefone = rs.getString("telefone");
                    String cnpj = rs.getString("cnpj");
                    String nomeResponsavel = rs.getString("nome_responsavel");
                    String horarioFuncionamento = rs.getString("horario_funcionamento");
                    int enderecoId = rs.getInt("endereco_id");

                    EnderecoDAO enderecoDAO = new EnderecoDAO();
                    Endereco endereco = enderecoDAO.buscarPorId(enderecoId);

                    abrigo = new Abrigo(id, nome, email, senha, telefone, cnpj, nomeResponsavel, endereco);
                    abrigo.setHorarioFuncionamento(horarioFuncionamento);
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao buscar abrigo: " + e.getMessage());
        }
        return abrigo;
    }

    /**
     * Lista todos os abrigos cadastrados.
     *
     * @return Lista de todos os abrigos
     */
    public List<Abrigo> listarTodos() {
        List<Abrigo> lista = new ArrayList<>();
        String sql = "SELECT * FROM abrigo";

        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String email = rs.getString("email");
                String senha = rs.getString("senha");
                String telefone = rs.getString("telefone");
                String cnpj = rs.getString("cnpj");
                String nomeResponsavel = rs.getString("nome_responsavel");
                String horarioFuncionamento = rs.getString("horario_funcionamento");

                Abrigo abrigo = new Abrigo(id, nome, email, senha, telefone, cnpj, nomeResponsavel, null);
                abrigo.setHorarioFuncionamento(horarioFuncionamento);
                lista.add(abrigo);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar abrigos: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Atualiza os dados de um abrigo existente.
     *
     * @param abrigo Objeto Abrigo com os dados atualizados
     * @throws SQLException Se houver erro na execução da query
     */
    public void atualizar(Abrigo abrigo) throws SQLException {
        String sql = "UPDATE abrigo SET nome = ?, email = ?, senha = ?, telefone = ?, nome_responsavel = ?, horario_funcionamento = ? WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, abrigo.getNome());
            pstmt.setString(2, abrigo.getEmail());
            pstmt.setString(3, abrigo.getSenha());
            pstmt.setString(4, abrigo.getTelefone());
            pstmt.setString(5, abrigo.getNomeResponsavel());
            pstmt.setString(6, abrigo.getHorarioFuncionamento());
            pstmt.setInt(7, abrigo.getId());

            pstmt.executeUpdate();
        }
    }

    /**
     * Exclui um abrigo do sistema pelo seu ID.
     *
     * @param id ID do abrigo a ser excluído
     * @throws SQLException Se houver erro na execução da query
     */
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM abrigo WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}