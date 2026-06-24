package com.devsouzx.adotapet.dao;

import com.devsouzx.adotapet.model.Pet;
import com.devsouzx.adotapet.model.Abrigo;
import com.devsouzx.adotapet.model.enums.Porte;
import com.devsouzx.adotapet.model.enums.StatusPet;
import com.devsouzx.adotapet.util.ConexaoBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de acesso a dados para a entidade {@link Pet}.
 *
 * <p>Responsável pelas operações de CRUD (Create, Read, Update, Delete)
 * no banco de dados para a tabela {@code pet}.</p>
 *
 * <p>Métodos disponíveis:</p>
 * <ul>
 *   <li>{@link #inserir(Pet)} - Cadastra um novo pet</li>
 *   <li>{@link #buscarPorId(int)} - Busca um pet pelo ID</li>
 *   <li>{@link #listarTodos()} - Lista todos os pets</li>
 *   <li>{@link #listarDisponiveis()} - Lista pets com status DISPONIVEL</li>
 *   <li>{@link #listarPorAbrigo(int)} - Lista pets de um abrigo</li>
 *   <li>{@link #atualizar(Pet)} - Atualiza os dados de um pet</li>
 *   <li>{@link #excluir(int)} - Remove um pet do sistema</li>
 * </ul>
 *
 *
 * @author Equipe Adoção de Pets
 * @version 1.0
 * @since 2026
 * @see Pet
 */
public class PetDAO {

    /**
     * Insere um novo pet no banco de dados.
     *
     * @param pet Objeto Pet a ser inserido
     * @throws SQLException Se houver erro na execução da query
     */
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

    /**
     * Busca um pet pelo seu ID.
     *
     * @param id ID do pet
     * @return Objeto Pet encontrado, ou {@code null} se não existir
     */
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

    /**
     * Lista todos os pets cadastrados.
     *
     * @return Lista de todos os pets
     */
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

                Pet pet = new Pet(id, nome, especie, raca, idadeMeses, porte, descricao, foto, status, null);
                lista.add(pet);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar pets: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Lista apenas os pets disponíveis para adoção.
     *
     * @return Lista de pets com status DISPONIVEL
     */
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

    /**
     * Lista os pets de um abrigo específico.
     *
     * @param abrigoId ID do abrigo
     * @return Lista de pets do abrigo
     */
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

    /**
     * Atualiza os dados de um pet existente.
     *
     * @param pet Objeto Pet com os dados atualizados
     * @throws SQLException Se houver erro na execução da query
     */
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

    /**
     * Exclui um pet do sistema pelo seu ID.
     *
     * @param id ID do pet a ser excluído
     * @throws SQLException Se houver erro na execução da query
     */
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM pet WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}