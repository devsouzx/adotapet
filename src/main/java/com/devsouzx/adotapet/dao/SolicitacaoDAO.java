package com.devsouzx.adotapet.dao;

import com.devsouzx.adotapet.model.SolicitacaoAdocao;
import com.devsouzx.adotapet.model.Adotante;
import com.devsouzx.adotapet.model.Pet;
import com.devsouzx.adotapet.model.enums.StatusSolicitacao;
import com.devsouzx.adotapet.util.ConexaoBD;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SolicitacaoDAO {

    public void inserir(SolicitacaoAdocao solicitacao) throws SQLException {
        String sql = "INSERT INTO solicitacao_adocao (data_solicitacao, data_resposta, status, justificativa, adotante_id, pet_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(solicitacao.getDataSolicitacao()));

            if (solicitacao.getDataResposta() != null) {
                pstmt.setTimestamp(2, Timestamp.valueOf(solicitacao.getDataResposta()));
            } else {
                pstmt.setNull(2, Types.TIMESTAMP);
            }

            pstmt.setString(3, solicitacao.getStatus().name());
            pstmt.setString(4, solicitacao.getJustificativa());
            pstmt.setInt(5, solicitacao.getAdotante().getId());
            pstmt.setInt(6, solicitacao.getPet().getId());

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    solicitacao.setId(rs.getInt(1));
                }
            }
        }
    }

    public SolicitacaoAdocao buscarPorId(int id) {
        String sql = "SELECT * FROM solicitacao_adocao WHERE id = ?";
        SolicitacaoAdocao solicitacao = null;

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    LocalDateTime dataSolicitacao = rs.getTimestamp("data_solicitacao").toLocalDateTime();
                    LocalDateTime dataResposta = rs.getTimestamp("data_resposta") != null ?
                            rs.getTimestamp("data_resposta").toLocalDateTime() : null;
                    StatusSolicitacao status = StatusSolicitacao.valueOf(rs.getString("status"));
                    String justificativa = rs.getString("justificativa");
                    int adotanteId = rs.getInt("adotante_id");
                    int petId = rs.getInt("pet_id");

                    AdotanteDAO adotanteDAO = new AdotanteDAO();
                    PetDAO petDAO = new PetDAO();

                    Adotante adotante = adotanteDAO.buscarPorId(adotanteId);
                    Pet pet = petDAO.buscarPorId(petId);

                    solicitacao = new SolicitacaoAdocao(
                            id, dataSolicitacao, dataResposta, status, justificativa, adotante, pet
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao buscar solicitação: " + e.getMessage());
        }
        return solicitacao;
    }

    public List<SolicitacaoAdocao> listarPorAdotante(int adotanteId) {
        List<SolicitacaoAdocao> lista = new ArrayList<>();
        String sql = "SELECT * FROM solicitacao_adocao WHERE adotante_id = ? ORDER BY data_solicitacao DESC";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, adotanteId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    LocalDateTime dataSolicitacao = rs.getTimestamp("data_solicitacao").toLocalDateTime();
                    LocalDateTime dataResposta = rs.getTimestamp("data_resposta") != null ?
                            rs.getTimestamp("data_resposta").toLocalDateTime() : null;
                    StatusSolicitacao status = StatusSolicitacao.valueOf(rs.getString("status"));
                    String justificativa = rs.getString("justificativa");
                    int adotanteIdDB = rs.getInt("adotante_id");
                    int petId = rs.getInt("pet_id");

                    AdotanteDAO adotanteDAO = new AdotanteDAO();
                    PetDAO petDAO = new PetDAO();

                    Adotante adotante = adotanteDAO.buscarPorId(adotanteIdDB);
                    Pet pet = petDAO.buscarPorId(petId);

                    SolicitacaoAdocao solicitacao = new SolicitacaoAdocao(
                            id, dataSolicitacao, dataResposta, status, justificativa, adotante, pet
                    );
                    lista.add(solicitacao);
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar solicitações do adotante: " + e.getMessage());
        }
        return lista;
    }

    public List<SolicitacaoAdocao> listarPorPet(int petId) {
        List<SolicitacaoAdocao> lista = new ArrayList<>();
        String sql = "SELECT * FROM solicitacao_adocao WHERE pet_id = ? ORDER BY data_solicitacao DESC";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, petId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    LocalDateTime dataSolicitacao = rs.getTimestamp("data_solicitacao").toLocalDateTime();
                    LocalDateTime dataResposta = rs.getTimestamp("data_resposta") != null ?
                            rs.getTimestamp("data_resposta").toLocalDateTime() : null;
                    StatusSolicitacao status = StatusSolicitacao.valueOf(rs.getString("status"));
                    String justificativa = rs.getString("justificativa");
                    int adotanteId = rs.getInt("adotante_id");
                    int petIdDB = rs.getInt("pet_id");

                    AdotanteDAO adotanteDAO = new AdotanteDAO();
                    PetDAO petDAO = new PetDAO();

                    Adotante adotante = adotanteDAO.buscarPorId(adotanteId);
                    Pet pet = petDAO.buscarPorId(petIdDB);

                    SolicitacaoAdocao solicitacao = new SolicitacaoAdocao(
                            id, dataSolicitacao, dataResposta, status, justificativa, adotante, pet
                    );
                    lista.add(solicitacao);
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar solicitações do pet: " + e.getMessage());
        }
        return lista;
    }

    public List<SolicitacaoAdocao> listarTodos() {
        List<SolicitacaoAdocao> lista = new ArrayList<>();
        String sql = "SELECT * FROM solicitacao_adocao ORDER BY data_solicitacao DESC";

        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                LocalDateTime dataSolicitacao = rs.getTimestamp("data_solicitacao").toLocalDateTime();
                LocalDateTime dataResposta = rs.getTimestamp("data_resposta") != null ?
                        rs.getTimestamp("data_resposta").toLocalDateTime() : null;
                StatusSolicitacao status = StatusSolicitacao.valueOf(rs.getString("status"));
                String justificativa = rs.getString("justificativa");
                int adotanteId = rs.getInt("adotante_id");
                int petId = rs.getInt("pet_id");

                AdotanteDAO adotanteDAO = new AdotanteDAO();
                PetDAO petDAO = new PetDAO();

                Adotante adotante = adotanteDAO.buscarPorId(adotanteId);
                Pet pet = petDAO.buscarPorId(petId);

                SolicitacaoAdocao solicitacao = new SolicitacaoAdocao(
                        id, dataSolicitacao, dataResposta, status, justificativa, adotante, pet
                );
                lista.add(solicitacao);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar todas as solicitações: " + e.getMessage());
        }
        return lista;
    }

    public List<SolicitacaoAdocao> listarPendentesPorAbrigo(int abrigoId) {
        List<SolicitacaoAdocao> lista = new ArrayList<>();
        String sql = """
            SELECT s.* FROM solicitacao_adocao s
            INNER JOIN pet p ON s.pet_id = p.id
            WHERE p.abrigo_id = ? AND s.status = 'PENDENTE'
            ORDER BY s.data_solicitacao ASC
        """;

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, abrigoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    LocalDateTime dataSolicitacao = rs.getTimestamp("data_solicitacao").toLocalDateTime();
                    LocalDateTime dataResposta = rs.getTimestamp("data_resposta") != null ?
                            rs.getTimestamp("data_resposta").toLocalDateTime() : null;
                    StatusSolicitacao status = StatusSolicitacao.valueOf(rs.getString("status"));
                    String justificativa = rs.getString("justificativa");
                    int adotanteId = rs.getInt("adotante_id");
                    int petId = rs.getInt("pet_id");

                    AdotanteDAO adotanteDAO = new AdotanteDAO();
                    PetDAO petDAO = new PetDAO();

                    Adotante adotante = adotanteDAO.buscarPorId(adotanteId);
                    Pet pet = petDAO.buscarPorId(petId);

                    SolicitacaoAdocao solicitacao = new SolicitacaoAdocao(
                            id, dataSolicitacao, dataResposta, status, justificativa, adotante, pet
                    );
                    lista.add(solicitacao);
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar solicitações pendentes do abrigo: " + e.getMessage());
        }
        return lista;
    }

    public void atualizar(SolicitacaoAdocao solicitacao) throws SQLException {
        String sql = "UPDATE solicitacao_adocao SET data_resposta = ?, status = ?, justificativa = ? WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (solicitacao.getDataResposta() != null) {
                pstmt.setTimestamp(1, Timestamp.valueOf(solicitacao.getDataResposta()));
            } else {
                pstmt.setNull(1, Types.TIMESTAMP);
            }

            pstmt.setString(2, solicitacao.getStatus().name());
            pstmt.setString(3, solicitacao.getJustificativa());
            pstmt.setInt(4, solicitacao.getId());

            pstmt.executeUpdate();
        }
    }

    public boolean aprovar(int idSolicitacao) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmtSolic = null;
        PreparedStatement pstmtPet = null;

        try {
            conn = ConexaoBD.getConexao();
            conn.setAutoCommit(false);

            SolicitacaoAdocao solicitacao = buscarPorId(idSolicitacao);
            if (solicitacao == null || solicitacao.getStatus() != StatusSolicitacao.PENDENTE) {
                return false;
            }

            String sqlSolic = "UPDATE solicitacao_adocao SET data_resposta = ?, status = ? WHERE id = ?";
            pstmtSolic = conn.prepareStatement(sqlSolic);
            pstmtSolic.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pstmtSolic.setString(2, StatusSolicitacao.APROVADA.name());
            pstmtSolic.setInt(3, idSolicitacao);
            pstmtSolic.executeUpdate();

            String sqlPet = "UPDATE pet SET status = ? WHERE id = ?";
            pstmtPet = conn.prepareStatement(sqlPet);
            pstmtPet.setString(1, "ADOTADO");
            pstmtPet.setInt(2, solicitacao.getPet().getId());
            pstmtPet.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (pstmtSolic != null) pstmtSolic.close();
            if (pstmtPet != null) pstmtPet.close();
            if (conn != null) conn.setAutoCommit(true);
        }
    }

    public boolean recusar(int idSolicitacao, String justificativa) throws SQLException {
        String sql = "UPDATE solicitacao_adocao SET data_resposta = ?, status = ?, justificativa = ? WHERE id = ?";

        if (justificativa == null || justificativa.trim().isEmpty()) {
            return false;
        }

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(2, StatusSolicitacao.RECUSADA.name());
            pstmt.setString(3, justificativa);
            pstmt.setInt(4, idSolicitacao);

            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM solicitacao_adocao WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean petPossuiSolicitacaoPendente(int petId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM solicitacao_adocao WHERE pet_id = ? AND status = 'PENDENTE'";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, petId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public boolean adotantePossuiSolicitacaoPendenteParaPet(int adotanteId, int petId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM solicitacao_adocao WHERE adotante_id = ? AND pet_id = ? AND status = 'PENDENTE'";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, adotanteId);
            pstmt.setInt(2, petId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public int contarSolicitacoesPendentesPorAdotante(int adotanteId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM solicitacao_adocao WHERE adotante_id = ? AND status = 'PENDENTE'";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, adotanteId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
}