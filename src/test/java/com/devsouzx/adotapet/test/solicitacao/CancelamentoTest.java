package com.devsouzx.adotapet.test.solicitacao;

import com.devsouzx.adotapet.dao.AdotanteDAO;
import com.devsouzx.adotapet.dao.SolicitacaoDAO;
import com.devsouzx.adotapet.model.Adotante;
import com.devsouzx.adotapet.model.SolicitacaoAdocao;
import com.devsouzx.adotapet.model.enums.StatusSolicitacao;
import com.devsouzx.adotapet.exception.AdocaoException;

import java.util.List;

public class CancelamentoTest {

    private static AdotanteDAO adotanteDAO = new AdotanteDAO();
    private static SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
    private static int passaram = 0;
    private static int falharam = 0;

    public static void executarTestes() {
        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│               TESTES DE CANCELAMENTO                        │");
        System.out.println("└─────────────────────────────────────────────────────────────┘\n");

        testarCancelamentoSolicitacao();
        testarCancelamentoSolicitacaoJaRespondida();

        System.out.println("\n   → CancelamentoTest: " + (passaram + falharam) + " testes | ✅ " + passaram + " | ❌ " + falharam);
    }

    public static void testarCancelamentoSolicitacao() {
        try {
            Adotante adotante = adotanteDAO.buscarPorEmail("joao.silva@email.com");
            if (adotante == null) {
                System.out.println("⚠️ CT17 - Cancelar Solicitação: SKIP");
                return;
            }

            List<SolicitacaoAdocao> pendentes = adotante.getSolicitacoesPorStatus(StatusSolicitacao.PENDENTE);
            if (pendentes.isEmpty()) {
                System.out.println("⚠️ CT17 - Cancelar Solicitação: SKIP (sem pendente)");
                return;
            }

            int id = pendentes.get(0).getId();
            adotante.cancelarSolicitacao(id);

            SolicitacaoAdocao atualizada = solicitacaoDAO.buscarPorId(id);
            if (atualizada == null || atualizada.getStatus() != StatusSolicitacao.CANCELADA_PELO_ADOTANTE) {
                System.out.println("❌ CT17 - Cancelar Solicitação: FALHOU (status incorreto)");
                falharam++;
                return;
            }

            System.out.println("✅ CT17 - Cancelar Solicitação: PASSou (ID: " + id + ")");
            passaram++;

        } catch (AdocaoException e) {
            System.out.println("❌ CT17 - Cancelar Solicitação: FALHOU - " + e.getMessage());
            falharam++;
        } catch (Exception e) {
            System.out.println("❌ CT17 - Cancelar Solicitação: FALHOU - " + e.getMessage());
            falharam++;
        }
    }

    public static void testarCancelamentoSolicitacaoJaRespondida() {
        try {
            Adotante adotante = adotanteDAO.buscarPorEmail("joao.silva@email.com");
            if (adotante == null) {
                System.out.println("⚠️ CT18 - Cancelar Solicitação Já Respondida: SKIP");
                return;
            }

            List<SolicitacaoAdocao> respondidas = adotante.getSolicitacoesPorStatus(StatusSolicitacao.APROVADA);
            if (respondidas.isEmpty()) {
                respondidas = adotante.getSolicitacoesPorStatus(StatusSolicitacao.RECUSADA);
            }

            if (respondidas.isEmpty()) {
                System.out.println("⚠️ CT18 - Cancelar Solicitação Já Respondida: SKIP (sem respondida)");
                return;
            }

            try {
                adotante.cancelarSolicitacao(respondidas.get(0).getId());
                System.out.println("❌ CT18 - Cancelar Solicitação Já Respondida: FALHOU (não bloqueou)");
                falharam++;
            } catch (AdocaoException e) {
                if (e.getMessage().contains("já foi respondida") || e.getMessage().contains("cancelar")) {
                    System.out.println("✅ CT18 - Cancelar Solicitação Já Respondida: PASSou");
                    passaram++;
                } else {
                    throw e;
                }
            }

        } catch (Exception e) {
            System.out.println("❌ CT18 - Cancelar Solicitação Já Respondida: FALHOU - " + e.getMessage());
            falharam++;
        }
    }
}