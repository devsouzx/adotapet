package com.devsouzx.adotapet.dao;

import com.devsouzx.adotapet.model.Pet;
import com.devsouzx.adotapet.model.Abrigo;
import com.devsouzx.adotapet.model.enums.Porte;
import com.devsouzx.adotapet.model.enums.StatusPet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PetDAO {

    public void inserir(Pet pet) {
        String sql = "INSERT INTO pet (nome, especie, raca, idade_meses, porte, descricao, foto, status, abrigo_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = util.ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, pet.getNome());
            pstmt.setString(2, pet.getEspecie());
            pstmt.setString(3, pet.getRaca());
            pstmt.setInt(4, pet.getIdadeMeses());
            pstmt.setString(5, pet.getPorte().name());
            pstmt.setString(6, pet.getDescricao());
            pstmt.setString(7, pet.getFoto());
            pstmt.setString(8, pet.getStatus().name());
            pstmt.setInt(9, pet.getAbrigo().getId());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                pet.setId(rs.getInt(1));
            }

            System.out.println("Pet inserido com sucesso! ID: " + pet.getId());

        } catch (SQLException e) {
            System.out.println("Erro ao inserir pet: " + e.getMessage());
        }
    }

    public Pet buscarPorId(int id) {
        String sql = "SELECT * FROM pet WHERE id = ?";

        try (Connection conn = util.ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extrairPet(rs);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar pet: " + e.getMessage());
        }
        return null;
    }

    public List<Pet> listarTodos() {
        List<Pet> lista = new ArrayList<>();
        String sql = "SELECT * FROM pet";

        try (Connection conn = util.ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(extrairPet(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar pets: " + e.getMessage());
        }
        return lista;
    }

    public List<Pet> listarDisponiveis() {
        List<Pet> lista = new ArrayList<>();
        String sql = "SELECT * FROM pet WHERE status = 'DISPONIVEL'";

        try (Connection conn = util.ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(extrairPet(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar pets disponíveis: " + e.getMessage());
        }
        return lista;
    }

    public List<Pet> listarPorAbrigo(int abrigoId) {
        List<Pet> lista = new ArrayList<>();
        String sql = "SELECT * FROM pet WHERE abrigo_id = ?";

        try (Connection conn = util.ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, abrigoId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(extrairPet(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar pets do abrigo: " + e.getMessage());
        }
        return lista;
    }

    public void atualizar(Pet pet) {
        String sql = "UPDATE pet SET nome = ?, especie = ?, raca = ?, idade_meses = ?, porte = ?, descricao = ?, foto = ?, status = ? WHERE id = ?";

        try (Connection conn = util.ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, pet.getNome());
            pstmt.setString(2, pet.getEspecie());
            pstmt.setString(3, pet.getRaca());
            pstmt.setInt(4, pet.getIdadeMeses());
            pstmt.setString(5, pet.getPorte().name());
            pstmt.setString(6, pet.getDescricao());
            pstmt.setString(7, pet.getFoto());
            pstmt.setString(8, pet.getStatus().name());
            pstmt.setInt(9, pet.getId());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Pet atualizado com sucesso!");
            } else {
                System.out.println("Pet não encontrado para atualizar.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar pet: " + e.getMessage());
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM pet WHERE id = ?";

        try (Connection conn = util.ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Pet excluído com sucesso!");
            } else {
                System.out.println("Pet não encontrado para excluir.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao excluir pet: " + e.getMessage());
        }
    }

    private Pet extrairPet(ResultSet rs) throws SQLException {
        AbrigoDAO abrigoDAO = new AbrigoDAO();
        Abrigo abrigo = abrigoDAO.buscarPorId(rs.getInt("abrigo_id"));

        return new Pet(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("especie"),
                rs.getString("raca"),
                rs.getInt("idade_meses"),
                Porte.valueOf(rs.getString("porte")),
                rs.getString("descricao"),
                rs.getString("foto"),
                StatusPet.valueOf(rs.getString("status")),
                abrigo
        );
    }
}