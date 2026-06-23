package com.devsouzx.adotapet.test.avaliacao;

import com.devsouzx.adotapet.dao.AdotanteDAO;
import com.devsouzx.adotapet.model.Adotante;
import com.devsouzx.adotapet.model.SolicitacaoAdocao;
import com.devsouzx.adotapet.model.enums.StatusSolicitacao;
import com.devsouzx.adotapet.exception.AdocaoException;

import java.util.List;

public class AvaliacaoTest {

    private static AdotanteDAO adotanteDAO = new AdotanteDAO();
    private static int passaram = 0;
    private static int falharam = 0;

    public static void executarTestes() {
        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│               TESTES DE AVALIAÇÃO                           │");
        System.out.println("└─────────────────────────────────────────────────────────────┘\n");

        testarAvaliacaoAbrigo();
        testarAvaliacaoNotaInvalida();
        testarAvaliacaoSolicitacaoNaoAprovada();

        System.out.println("\n   → AvaliacaoTest: " + (passaram + falharam) + " testes | ✅ " + passaram + " | ❌ " + falharam);
    }

    public static void testarAvaliacaoAbrigo() {
        try {
            Adotante adotante = adotanteDAO.buscarPorEmail("joao.silva@email.com");
            if (adotante == null) {
                System.out.println("⚠️ CT23 - Avaliar Abrigo: SKIP");
                return;
            }

            List<SolicitacaoAdocao> aprovadas = adotante.getSolicitacoesPorStatus(StatusSolicitacao.APROVADA);
            if (aprovadas.isEmpty()) {
                System.out.println("⚠️ CT23 - Avaliar Abrigo: SKIP (sem aprovação)");
                return;
            }

            adotante.avaliarAbrigo(aprovadas.get(0), 5, "Excelente abrigo!");

            System.out.println("✅ CT23 - Avaliar Abrigo: PASSou");
            passaram++;

        } catch (AdocaoException e) {
            System.out.println("❌ CT23 - Avaliar Abrigo: FALHOU - " + e.getMessage());
            falharam++;
        } catch (Exception e) {
            System.out.println("❌ CT23 - Avaliar Abrigo: FALHOU - " + e.getMessage());
            falharam++;
        }
    }

    public static void testarAvaliacaoNotaInvalida() {
        try {
            Adotante adotante = adotanteDAO.buscarPorEmail("joao.silva@email.com");
            if (adotante == null) {
                System.out.println("⚠️ CT24 - Avaliar com Nota Inválida: SKIP");
                return;
            }

            List<SolicitacaoAdocao> aprovadas = adotante.getSolicitacoesPorStatus(StatusSolicitacao.APROVADA);
            if (aprovadas.isEmpty()) {
                System.out.println("⚠️ CT24 - Avaliar com Nota Inválida: SKIP");
                return;
            }

            try {
                adotante.avaliarAbrigo(aprovadas.get(0), 10, "Nota inválida");
                System.out.println("❌ CT24 - Avaliar com Nota Inválida: FALHOU (não bloqueou)");
                falharam++;
            } catch (AdocaoException e) {
                if (e.getMessage().contains("nota") || e.getMessage().contains("entre 1 e 5")) {
                    System.out.println("✅ CT24 - Avaliar com Nota Inválida: PASSou");
                    passaram++;
                } else {
                    throw e;
                }
            }

        } catch (Exception e) {
            System.out.println("❌ CT24 - Avaliar com Nota Inválida: FALHOU - " + e.getMessage());
            falharam++;
        }
    }

    public static void testarAvaliacaoSolicitacaoNaoAprovada() {
        try {
            Adotante adotante = adotanteDAO.buscarPorEmail("joao.silva@email.com");
            if (adotante == null) {
                System.out.println("⚠️ CT25 - Avaliar Solicitação Não Aprovada: SKIP");
                return;
            }

            List<SolicitacaoAdocao> pendentes = adotante.getSolicitacoesPorStatus(StatusSolicitacao.PENDENTE);
            if (pendentes.isEmpty()) {
                System.out.println("⚠️ CT25 - Avaliar Solicitação Não Aprovada: SKIP");
                return;
            }

            try {
                adotante.avaliarAbrigo(pendentes.get(0), 5, "Avaliação");
                System.out.println("❌ CT25 - Avaliar Solicitação Não Aprovada: FALHOU (não bloqueou)");
                falharam++;
            } catch (AdocaoException e) {
                if (e.getMessage().contains("aprovada")) {
                    System.out.println("✅ CT25 - Avaliar Solicitação Não Aprovada: PASSou");
                    passaram++;
                } else {
                    throw e;
                }
            }

        } catch (Exception e) {
            System.out.println("❌ CT25 - Avaliar Solicitação Não Aprovada: FALHOU - " + e.getMessage());
            falharam++;
        }
    }
}