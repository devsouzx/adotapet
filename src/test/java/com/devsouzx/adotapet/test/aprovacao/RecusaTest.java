package com.devsouzx.adotapet.test.aprovacao;

import com.devsouzx.adotapet.dao.AbrigoDAO;
import com.devsouzx.adotapet.dao.SolicitacaoDAO;
import com.devsouzx.adotapet.model.Abrigo;
import com.devsouzx.adotapet.model.SolicitacaoAdocao;
import com.devsouzx.adotapet.model.enums.StatusSolicitacao;
import com.devsouzx.adotapet.exception.AdocaoException;

import java.util.List;

public class RecusaTest {

    private static AbrigoDAO abrigoDAO = new AbrigoDAO();
    private static SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
    private static int passaram = 0;
    private static int falharam = 0;

    public static void executarTestes() {
        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│               TESTES DE RECUSA                              │");
        System.out.println("└─────────────────────────────────────────────────────────────┘\n");

        testarRecusaSolicitacao();
        testarRecusaSolicitacaoSemJustificativa();

        System.out.println("\n   → RecusaTest: " + (passaram + falharam) + " testes | ✅ " + passaram + " | ❌ " + falharam);
    }

    public static void testarRecusaSolicitacao() {
        try {
            Abrigo abrigo = abrigoDAO.listarTodos().get(0);
            if (abrigo == null) {
                System.out.println("⚠️ CT21 - Recusar Solicitação: SKIP (sem abrigo)");
                return;
            }

            List<SolicitacaoAdocao> pendentes = abrigo.getSolicitacoesPendentes();
            if (pendentes.isEmpty()) {
                System.out.println("⚠️ CT21 - Recusar Solicitação: SKIP (sem pendente)");
                return;
            }

            int id = pendentes.get(0).getId();
            String justificativa = "Adotante reside muito distante do abrigo";

            abrigo.recusarSolicitacao(id, justificativa);

            SolicitacaoAdocao atualizada = solicitacaoDAO.buscarPorId(id);
            if (atualizada == null || atualizada.getStatus() != StatusSolicitacao.RECUSADA) {
                System.out.println("❌ CT21 - Recusar Solicitação: FALHOU (status incorreto)");
                falharam++;
                return;
            }

            if (!justificativa.equals(atualizada.getJustificativa())) {
                System.out.println("❌ CT21 - Recusar Solicitação: FALHOU (justificativa incorreta)");
                falharam++;
                return;
            }

            System.out.println("✅ CT21 - Recusar Solicitação: PASSou (ID: " + id + ")");
            passaram++;

        } catch (AdocaoException e) {
            System.out.println("❌ CT21 - Recusar Solicitação: FALHOU - " + e.getMessage());
            falharam++;
        } catch (Exception e) {
            System.out.println("❌ CT21 - Recusar Solicitação: FALHOU - " + e.getMessage());
            falharam++;
        }
    }

    public static void testarRecusaSolicitacaoSemJustificativa() {
        try {
            Abrigo abrigo = abrigoDAO.listarTodos().get(0);
            if (abrigo == null) {
                System.out.println("⚠️ CT22 - Recusar Solicitação sem Justificativa: SKIP");
                return;
            }

            List<SolicitacaoAdocao> pendentes = abrigo.getSolicitacoesPendentes();
            if (pendentes.isEmpty()) {
                System.out.println("⚠️ CT22 - Recusar Solicitação sem Justificativa: SKIP");
                return;
            }

            try {
                abrigo.recusarSolicitacao(pendentes.get(0).getId(), "");
                System.out.println("❌ CT22 - Recusar Solicitação sem Justificativa: FALHOU (não bloqueou)");
                falharam++;
            } catch (AdocaoException e) {
                if (e.getMessage().contains("justificativa")) {
                    System.out.println("✅ CT22 - Recusar Solicitação sem Justificativa: PASSou");
                    passaram++;
                } else {
                    throw e;
                }
            }

        } catch (Exception e) {
            System.out.println("❌ CT22 - Recusar Solicitação sem Justificativa: FALHOU - " + e.getMessage());
            falharam++;
        }
    }
}