package com.devsouzx.adotapet.test.aprovacao;

import com.devsouzx.adotapet.dao.AbrigoDAO;
import com.devsouzx.adotapet.dao.PetDAO;
import com.devsouzx.adotapet.model.Abrigo;
import com.devsouzx.adotapet.model.Pet;
import com.devsouzx.adotapet.model.SolicitacaoAdocao;
import com.devsouzx.adotapet.model.enums.StatusPet;
import com.devsouzx.adotapet.model.enums.StatusSolicitacao;
import com.devsouzx.adotapet.exception.AdocaoException;

import java.util.List;

public class AprovacaoTest {

    private static AbrigoDAO abrigoDAO = new AbrigoDAO();
    private static PetDAO petDAO = new PetDAO();
    private static int passaram = 0;
    private static int falharam = 0;

    public static void executarTestes() {
        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│               TESTES DE APROVAÇÃO                           │");
        System.out.println("└─────────────────────────────────────────────────────────────┘\n");

        testarAprovacaoSolicitacao();
        testarAprovacaoSolicitacaoJaRespondida();

        System.out.println("\n   → AprovacaoTest: " + (passaram + falharam) + " testes | ✅ " + passaram + " | ❌ " + falharam);
    }

    public static void testarAprovacaoSolicitacao() {
        try {
            List<Abrigo> abrigos = abrigoDAO.listarTodos();
            if (abrigos == null || abrigos.isEmpty()) {
                System.out.println("⚠️ CT19 - Aprovar Solicitação: SKIP (sem abrigo)");
                return;
            }
            Abrigo abrigo = abrigos.get(0);

            List<SolicitacaoAdocao> pendentes = abrigo.getSolicitacoesPendentes();
            if (pendentes == null || pendentes.isEmpty()) {
                System.out.println("⚠️ CT19 - Aprovar Solicitação: SKIP (sem solicitação pendente)");
                return;
            }

            SolicitacaoAdocao sol = pendentes.get(0);
            int id = sol.getId();
            int petId = sol.getPet().getId();

            abrigo.aprovarSolicitacao(id);

            Pet pet = petDAO.buscarPorId(petId);
            if (pet == null || pet.getStatus() != StatusPet.ADOTADO) {
                System.out.println("❌ CT19 - Aprovar Solicitação: FALHOU (pet não adotado)");
                falharam++;
                return;
            }

            System.out.println("✅ CT19 - Aprovar Solicitação: PASSou (ID: " + id + ")");
            passaram++;

        } catch (AdocaoException e) {
            System.out.println("❌ CT19 - Aprovar Solicitação: FALHOU - " + e.getMessage());
            falharam++;
        } catch (Exception e) {
            System.out.println("❌ CT19 - Aprovar Solicitação: FALHOU - " + e.getMessage());
            falharam++;
        }
    }

    public static void testarAprovacaoSolicitacaoJaRespondida() {
        try {
            Abrigo abrigo = abrigoDAO.listarTodos().get(0);
            if (abrigo == null) {
                System.out.println("⚠️ CT20 - Aprovar Solicitação Já Respondida: SKIP");
                return;
            }

            List<SolicitacaoAdocao> respondidas = abrigo.getSolicitacoesRecebidas().stream()
                    .filter(s -> s.getStatus() == StatusSolicitacao.APROVADA || s.getStatus() == StatusSolicitacao.RECUSADA)
                    .toList();

            if (respondidas.isEmpty()) {
                System.out.println("⚠️ CT20 - Aprovar Solicitação Já Respondida: SKIP");
                return;
            }

            try {
                abrigo.aprovarSolicitacao(respondidas.get(0).getId());
                System.out.println("❌ CT20 - Aprovar Solicitação Já Respondida: FALHOU (não bloqueou)");
                falharam++;
            } catch (AdocaoException e) {
                if (e.getMessage().contains("já foi respondida")) {
                    System.out.println("✅ CT20 - Aprovar Solicitação Já Respondida: PASSou");
                    passaram++;
                } else {
                    throw e;
                }
            }

        } catch (Exception e) {
            System.out.println("❌ CT20 - Aprovar Solicitação Já Respondida: FALHOU - " + e.getMessage());
            falharam++;
        }
    }
}