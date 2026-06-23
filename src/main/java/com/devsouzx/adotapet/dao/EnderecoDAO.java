package com.devsouzx.adotapet.dao;

import com.devsouzx.adotapet.model.Endereco;
import com.devsouzx.adotapet.util.ConexaoBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnderecoDAO {

    public void inserir(Endereco endereco) {
        String sql = "INSERT INTO endereco (logradouro, numero, bairro, cidade, estado, cep) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, endereco.getLogradouro());
            pstmt.setString(2, endereco.getNumero());
            pstmt.setString(3, endereco.getBairro());
            pstmt.setString(4, endereco.getCidade());
            pstmt.setString(5, endereco.getEstado());
            pstmt.setString(6, endereco.getCep());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                endereco.setId(rs.getInt(1));
            }

            System.out.println("Endereço inserido com sucesso! ID: " + endereco.getId());

        } catch (SQLException e) {
            System.out.println("Erro ao inserir endereço: " + e.getMessage());
        }
    }

    public Endereco buscarPorId(int id) {
        String sql = "SELECT * FROM endereco WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Endereco(
                        rs.getInt("id"),
                        rs.getString("logradouro"),
                        rs.getString("numero"),
                        rs.getString("bairro"),
                        rs.getString("cidade"),
                        rs.getString("estado"),
                        rs.getString("cep")
                );
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar endereço: " + e.getMessage());
        }
        return null;
    }

    public List<Endereco> listarTodos() {
        List<Endereco> lista = new ArrayList<>();
        String sql = "SELECT * FROM endereco";

        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Endereco(
                        rs.getInt("id"),
                        rs.getString("logradouro"),
                        rs.getString("numero"),
                        rs.getString("bairro"),
                        rs.getString("cidade"),
                        rs.getString("estado"),
                        rs.getString("cep")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar endereços: " + e.getMessage());
        }
        return lista;
    }

    public void atualizar(Endereco endereco) {
        String sql = "UPDATE endereco SET logradouro = ?, numero = ?, bairro = ?, cidade = ?, estado = ?, cep = ? WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, endereco.getLogradouro());
            pstmt.setString(2, endereco.getNumero());
            pstmt.setString(3, endereco.getBairro());
            pstmt.setString(4, endereco.getCidade());
            pstmt.setString(5, endereco.getEstado());
            pstmt.setString(6, endereco.getCep());
            pstmt.setInt(7, endereco.getId());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Endereço atualizado com sucesso!");
            } else {
                System.out.println("Endereço não encontrado para atualizar.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar endereço: " + e.getMessage());
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM endereco WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Endereço excluído com sucesso!");
            } else {
                System.out.println("Endereço não encontrado para excluir.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao excluir endereço: " + e.getMessage());
        }
    }
}