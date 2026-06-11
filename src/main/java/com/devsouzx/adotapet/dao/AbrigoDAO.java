package com.devsouzx.adotapet.dao;

import com.devsouzx.adotapet.model.Abrigo;
import com.devsouzx.adotapet.model.Endereco;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbrigoDAO {

    public void inserir(Abrigo abrigo) {
        String sql = "INSERT INTO abrigo (nome, email, senha, telefone, cnpj, nome_responsavel, horario_funcionamento, endereco_id, data_cadastro) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = util.ConexaoBD.getConexao();
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

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                abrigo.setId(rs.getInt(1));
            }

            System.out.println("Abrigo inserido com sucesso! ID: " + abrigo.getId());

        } catch (SQLException e) {
            System.out.println("Erro ao inserir abrigo: " + e.getMessage());
        }
    }

    public Abrigo buscarPorId(int id) {
        String sql = "SELECT * FROM abrigo WHERE id = ?";

        try (Connection conn = util.ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extrairAbrigo(rs);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar abrigo: " + e.getMessage());
        }
        return null;
    }

    public Abrigo buscarPorEmail(String email) {
        String sql = "SELECT * FROM abrigo WHERE email = ?";

        try (Connection conn = util.ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extrairAbrigo(rs);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar abrigo: " + e.getMessage());
        }
        return null;
    }

    public List<Abrigo> listarTodos() {
        List<Abrigo> lista = new ArrayList<>();
        String sql = "SELECT * FROM abrigo";

        try (Connection conn = util.ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(extrairAbrigo(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar abrigos: " + e.getMessage());
        }
        return lista;
    }

    public void atualizar(Abrigo abrigo) {
        String sql = "UPDATE abrigo SET nome = ?, email = ?, senha = ?, telefone = ?, nome_responsavel = ?, horario_funcionamento = ? WHERE id = ?";

        try (Connection conn = util.ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, abrigo.getNome());
            pstmt.setString(2, abrigo.getEmail());
            pstmt.setString(3, abrigo.getSenha());
            pstmt.setString(4, abrigo.getTelefone());
            pstmt.setString(5, abrigo.getNomeResponsavel());
            pstmt.setString(6, abrigo.getHorarioFuncionamento());
            pstmt.setInt(7, abrigo.getId());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Abrigo atualizado com sucesso!");
            } else {
                System.out.println("Abrigo não encontrado para atualizar.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar abrigo: " + e.getMessage());
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM abrigo WHERE id = ?";

        try (Connection conn = util.ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Abrigo excluído com sucesso!");
            } else {
                System.out.println("Abrigo não encontrado para excluir.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao excluir abrigo: " + e.getMessage());
        }
    }

    private Abrigo extrairAbrigo(ResultSet rs) throws SQLException {
        EnderecoDAO enderecoDAO = new EnderecoDAO();
        Endereco endereco = enderecoDAO.buscarPorId(rs.getInt("endereco_id"));

        Abrigo abrigo = new Abrigo(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("email"),
                rs.getString("senha"),
                rs.getString("telefone"),
                rs.getString("cnpj"),
                rs.getString("nome_responsavel"),
                endereco
        );
        abrigo.setHorarioFuncionamento(rs.getString("horario_funcionamento"));
        abrigo.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());
        return abrigo;
    }
}