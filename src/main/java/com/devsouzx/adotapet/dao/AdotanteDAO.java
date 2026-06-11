package com.devsouzx.adotapet.dao;

import com.devsouzx.adotapet.model.Adotante;
import com.devsouzx.adotapet.model.Endereco;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdotanteDAO {
    public void inserir(Adotante adotante) {
        String sql = "INSERT INTO adotante (nome, email, senha, telefone, cpf, data_nascimento, endereco_id, data_cadastro) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = util.ConexaoBD.getConexao();
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

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                adotante.setId(rs.getInt(1));
            }

            System.out.println("Adotante inserido com sucesso! ID: " + adotante.getId());

        } catch (SQLException e) {
            System.out.println("Erro ao inserir adotante: " + e.getMessage());
        }
    }

    public Adotante buscarPorId(int id) {
        String sql = "SELECT * FROM adotante WHERE id = ?";

        try (Connection conn = util.ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extrairAdotante(rs);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar adotante: " + e.getMessage());
        }
        return null;
    }

    public Adotante buscarPorEmail(String email) {
        String sql = "SELECT * FROM adotante WHERE email = ?";

        try (Connection conn = util.ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extrairAdotante(rs);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar adotante: " + e.getMessage());
        }
        return null;
    }

    public List<Adotante> listarTodos() {
        List<Adotante> lista = new ArrayList<>();
        String sql = "SELECT * FROM adotante";

        try (Connection conn = util.ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(extrairAdotante(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar adotantes: " + e.getMessage());
        }
        return lista;
    }

    public void atualizar(Adotante adotante) {
        String sql = "UPDATE adotante SET nome = ?, email = ?, senha = ?, telefone = ?, cpf = ?, data_nascimento = ? WHERE id = ?";

        try (Connection conn = util.ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, adotante.getNome());
            pstmt.setString(2, adotante.getEmail());
            pstmt.setString(3, adotante.getSenha());
            pstmt.setString(4, adotante.getTelefone());
            pstmt.setString(5, adotante.getCpf());
            pstmt.setDate(6, Date.valueOf(adotante.getDataNascimento()));
            pstmt.setInt(7, adotante.getId());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Adotante atualizado com sucesso!");
            } else {
                System.out.println("Adotante não encontrado para atualizar.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar adotante: " + e.getMessage());
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM adotante WHERE id = ?";

        try (Connection conn = util.ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Adotante excluído com sucesso!");
            } else {
                System.out.println("Adotante não encontrado para excluir.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao excluir adotante: " + e.getMessage());
        }
    }

    private Adotante extrairAdotante(ResultSet rs) throws SQLException {
        EnderecoDAO enderecoDAO = new EnderecoDAO();
        Endereco endereco = enderecoDAO.buscarPorId(rs.getInt("endereco_id"));

        return new Adotante(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("email"),
                rs.getString("senha"),
                rs.getString("telefone"),
                rs.getString("cpf"),
                rs.getDate("data_nascimento").toLocalDate(),
                endereco
        );
    }
}