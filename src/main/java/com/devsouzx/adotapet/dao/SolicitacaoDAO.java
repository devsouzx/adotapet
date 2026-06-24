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

/**
 * Classe de acesso a dados para a entidade {@link SolicitacaoAdocao}.
 *
 * <p>Responsável pelas operações de CRUD (Create, Read, Update, Delete)
 * no banco de dados para a tabela {@code solicitacao_adocao}.</p>
 *
 * <p>Métodos disponíveis:</p>
 * <ul>
 *   <li>{@link #inserir(SolicitacaoAdocao)} - Cria uma nova solicitação</li>
 *   <li>{@link #buscarPorId(int)} - Busca uma solicitação pelo ID</li>
 *   <li>{@link #listarPorAdotante(int)} - Lista solicitações de um adotante</li>
 *   <li>{@link #listarPorPet(int)} - Lista solicitações de um pet</li>
 *   <li>{@link #listarTodos()} - Lista todas as solicitações</li>
 *   <li>{@link #atualizar(SolicitacaoAdocao)} - Atualiza uma solicitação</li>
 *   <li>{@link #aprovar(int)} - Aprova uma solicitação</li>
 *   <li>{@link #recusar(int, String)} - Recusa uma solicitação</li>
 *   <li>{@link #excluir(int)} - Remove uma solicitação</li>
 * </ul>
 *
 *
 * @author Equipe Adoção de Pets
 * @version 1.0
 * @since 2026
 * @see SolicitacaoAdocao
 */
public class SolicitacaoDAO {

    /**
     * Insere uma nova solicitação de adoção no banco de dados.
     *
     * @param solicitacao Objeto SolicitacaoAdocao a ser inserido
     * @throws SQLException Se houver erro na execução da query
     */
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

    /**
     * Busca uma solicitação pelo seu ID.
     *
     * @param id ID da solicitação
     * @return Objeto SolicitacaoAdocao encontrado, ou {@code null} se não existir
     */
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

    /**
     * Lista todas as solicitações de um adotante específico.
     *
     * @param adotanteId ID do adotante
     * @return Lista de solicitações do adotante
     */
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

    /**
     * Lista todas as solicitações de um pet específico.
     *
     * @param petId ID do pet
     * @return Lista de solicitações do pet
     */
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

    /**
     * Lista todas as solicitações de adoção cadastradas.
     *
     * @return Lista de todas as solicitações
     */
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

    /**
     * Atualiza os dados de uma solicitação de adoção.
     *
     * @param solicitacao Objeto SolicitacaoAdocao com os dados atualizados
     * @throws SQLException Se houver erro na execução da query
     */
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

    /**
     * Aprova uma solicitação de adoção.
     * Altera o status da solicitação para APROVADA e o status do pet para ADOTADO.
     *
     * @param idSolicitacao ID da solicitação a ser aprovada
     * @return {@code true} se aprovada com sucesso, {@code false} caso contrário
     * @throws SQLException Se houver erro na execução da query
     */
    public boolean aprovar(int idSolicitacao) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmtSolic = null;
        PreparedStatement pstmtPet = null;
        PreparedStatement pstmtBusca = null;
        ResultSet rs = null;

        try {
            conn = ConexaoBD.getConexao();
            conn.setAutoCommit(false);

            String sqlBusca = "SELECT * FROM solicitacao_adocao WHERE id = ?";
            pstmtBusca = conn.prepareStatement(sqlBusca);
            pstmtBusca.setInt(1, idSolicitacao);
            rs = pstmtBusca.executeQuery();

            if (!rs.next()) {
                return false;
            }

            int petId = rs.getInt("pet_id");
            String statusAtual = rs.getString("status");

            if (!statusAtual.equals("PENDENTE")) {
                return false;
            }

            rs.close();
            pstmtBusca.close();

            String sqlSolic = "UPDATE solicitacao_adocao SET data_resposta = ?, status = ? WHERE id = ?";
            pstmtSolic = conn.prepareStatement(sqlSolic);
            pstmtSolic.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pstmtSolic.setString(2, StatusSolicitacao.APROVADA.name());
            pstmtSolic.setInt(3, idSolicitacao);
            pstmtSolic.executeUpdate();

            String sqlPet = "UPDATE pet SET status = ? WHERE id = ?";
            pstmtPet = conn.prepareStatement(sqlPet);
            pstmtPet.setString(1, "ADOTADO");
            pstmtPet.setInt(2, petId);
            pstmtPet.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            // Fechar todos os recursos
            try {
                if (rs != null) rs.close();
                if (pstmtBusca != null) pstmtBusca.close();
                if (pstmtSolic != null) pstmtSolic.close();
                if (pstmtPet != null) pstmtPet.close();
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Recusa uma solicitação de adoção com uma justificativa.
     *
     * @param idSolicitacao ID da solicitação a ser recusada
     * @param justificativa Motivo da recusa
     * @return {@code true} se recusada com sucesso, {@code false} caso contrário
     * @throws SQLException Se houver erro na execução da query
     */
    public boolean recusar(int idSolicitacao, String justificativa) throws SQLException {
        if (justificativa == null || justificativa.trim().isEmpty()) {
            return false;
        }

        String sql = "UPDATE solicitacao_adocao SET data_resposta = ?, status = ?, justificativa = ? WHERE id = ? AND status = 'PENDENTE'";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(2, StatusSolicitacao.RECUSADA.name());
            pstmt.setString(3, justificativa);
            pstmt.setInt(4, idSolicitacao);

            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Exclui uma solicitação de adoção do sistema.
     *
     * @param id ID da solicitação a ser excluída
     * @return {@code true} se excluída com sucesso, {@code false} caso contrário
     * @throws SQLException Se houver erro na execução da query
     */
    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM solicitacao_adocao WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Verifica se um pet possui solicitação pendente.
     *
     * @param petId ID do pet
     * @return {@code true} se possui solicitação pendente, {@code false} caso contrário
     * @throws SQLException Se houver erro na execução da query
     */
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

    /**
     * Verifica se um adotante já possui solicitação pendente para um pet específico.
     *
     * @param adotanteId ID do adotante
     * @param petId ID do pet
     * @return {@code true} se possui solicitação pendente, {@code false} caso contrário
     * @throws SQLException Se houver erro na execução da query
     */
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

    /**
     * Conta quantas solicitações pendentes um adotante possui.
     *
     * @param adotanteId ID do adotante
     * @return Número de solicitações pendentes
     * @throws SQLException Se houver erro na execução da query
     */
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