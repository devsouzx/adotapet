package com.devsouzx.adotapet.dao;

import com.devsouzx.adotapet.model.Pet;
import com.devsouzx.adotapet.model.Abrigo;
import com.devsouzx.adotapet.model.enums.Porte;
import com.devsouzx.adotapet.model.enums.StatusPet;
import com.devsouzx.adotapet.util.ConexaoBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PetDAO {

    public void inserir(Pet pet) throws SQLException {
        String sql = "INSERT INTO pet (nome, especie, raca, idade_meses, porte, descricao, foto, status, abrigo_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.getConexao();
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

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    pet.setId(rs.getInt(1));
                }
            }
        }
    }

    public Pet buscarPorId(int id) {
        String sql = "SELECT * FROM pet WHERE id = ?";
        Pet pet = null;

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("nome");
                    String especie = rs.getString("especie");
                    String raca = rs.getString("raca");
                    int idadeMeses = rs.getInt("idade_meses");
                    Porte porte = Porte.valueOf(rs.getString("porte"));
                    String descricao = rs.getString("descricao");
                    String foto = rs.getString("foto");
                    StatusPet status = StatusPet.valueOf(rs.getString("status"));
                    int abrigoId = rs.getInt("abrigo_id");

                    AbrigoDAO abrigoDAO = new AbrigoDAO();
                    Abrigo abrigo = abrigoDAO.buscarPorId(abrigoId);

                    pet = new Pet(id, nome, especie, raca, idadeMeses, porte, descricao, foto, status, abrigo);
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao buscar pet: " + e.getMessage());
        }
        return pet;
    }

    public List<Pet> listarTodos() {
        List<Pet> lista = new ArrayList<>();
        String sql = "SELECT * FROM pet";

        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String especie = rs.getString("especie");
                String raca = rs.getString("raca");
                int idadeMeses = rs.getInt("idade_meses");
                Porte porte = Porte.valueOf(rs.getString("porte"));
                String descricao = rs.getString("descricao");
                String foto = rs.getString("foto");
                StatusPet status = StatusPet.valueOf(rs.getString("status"));
                int abrigoId = rs.getInt("abrigo_id");

                // Não buscar abrigo para evitar múltiplas conexões
                Pet pet = new Pet(id, nome, especie, raca, idadeMeses, porte, descricao, foto, status, null);
                lista.add(pet);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar pets: " + e.getMessage());
        }
        return lista;
    }

    public List<Pet> listarDisponiveis() {
        List<Pet> lista = new ArrayList<>();
        String sql = "SELECT * FROM pet WHERE status = 'DISPONIVEL'";

        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String especie = rs.getString("especie");
                String raca = rs.getString("raca");
                int idadeMeses = rs.getInt("idade_meses");
                Porte porte = Porte.valueOf(rs.getString("porte"));
                String descricao = rs.getString("descricao");
                String foto = rs.getString("foto");
                StatusPet status = StatusPet.valueOf(rs.getString("status"));

                Pet pet = new Pet(id, nome, especie, raca, idadeMeses, porte, descricao, foto, status, null);
                lista.add(pet);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar pets disponíveis: " + e.getMessage());
        }
        return lista;
    }

    public List<Pet> listarPorAbrigo(int abrigoId) {
        List<Pet> lista = new ArrayList<>();
        String sql = "SELECT * FROM pet WHERE abrigo_id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, abrigoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    String especie = rs.getString("especie");
                    String raca = rs.getString("raca");
                    int idadeMeses = rs.getInt("idade_meses");
                    Porte porte = Porte.valueOf(rs.getString("porte"));
                    String descricao = rs.getString("descricao");
                    String foto = rs.getString("foto");
                    StatusPet status = StatusPet.valueOf(rs.getString("status"));

                    Pet pet = new Pet(id, nome, especie, raca, idadeMeses, porte, descricao, foto, status, null);
                    lista.add(pet);
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar pets do abrigo: " + e.getMessage());
        }
        return lista;
    }

    public void atualizar(Pet pet) throws SQLException {
        String sql = "UPDATE pet SET nome = ?, especie = ?, raca = ?, idade_meses = ?, porte = ?, descricao = ?, foto = ?, status = ? WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
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

            pstmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM pet WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}