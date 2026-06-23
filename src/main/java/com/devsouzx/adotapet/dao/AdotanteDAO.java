package com.devsouzx.adotapet.dao;

import com.devsouzx.adotapet.model.Adotante;
import com.devsouzx.adotapet.model.Endereco;
import com.devsouzx.adotapet.util.ConexaoBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdotanteDAO {

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

                // Não buscar endereço aqui para evitar múltiplas conexões
                Adotante adotante = new Adotante(id, nome, email, senha, telefone, cpf, dataNasc.toLocalDate(), null);
                lista.add(adotante);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar adotantes: " + e.getMessage());
        }
        return lista;
    }

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

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM adotante WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}