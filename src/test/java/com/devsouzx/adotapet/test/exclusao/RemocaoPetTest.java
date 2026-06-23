package com.devsouzx.adotapet.test.exclusao;

import com.devsouzx.adotapet.dao.AbrigoDAO;
import com.devsouzx.adotapet.dao.PetDAO;
import com.devsouzx.adotapet.model.Abrigo;
import com.devsouzx.adotapet.model.Pet;
import com.devsouzx.adotapet.model.enums.Porte;
import com.devsouzx.adotapet.exception.AdocaoException;

import java.util.List;

public class RemocaoPetTest {

    private static AbrigoDAO abrigoDAO = new AbrigoDAO();
    private static PetDAO petDAO = new PetDAO();
    private static int passaram = 0;
    private static int falharam = 0;

    public static void executarTestes() {
        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│               TESTES DE REMOÇÃO DE PET                      │");
        System.out.println("└─────────────────────────────────────────────────────────────┘\n");

        testarRemocaoPetSemSolicitacao();
        testarRemocaoPetComSolicitacaoPendente();

        System.out.println("\n   → RemocaoPetTest: " + (passaram + falharam) + " testes | ✅ " + passaram + " | ❌ " + falharam);
    }

    public static void testarRemocaoPetSemSolicitacao() {
        try {
            List<Abrigo> abrigos = abrigoDAO.listarTodos();
            if (abrigos.isEmpty()) {
                System.out.println("⚠️ CT28 - Remover Pet sem Solicitação: SKIP (sem abrigo)");
                return;
            }

            Abrigo abrigo = abrigos.get(0);

            // Criar pet temporário
            abrigo.cadastrarPet(
                    "TempPet", "GATO", "Siamês", 12,
                    Porte.PEQUENO, "Pet temporário para teste", null
            );

            List<Pet> pets = abrigo.getPets();
            if (pets.isEmpty()) {
                System.out.println("❌ CT28 - Remover Pet sem Solicitação: FALHOU (pet não criado)");
                falharam++;
                return;
            }

            Pet pet = pets.get(pets.size() - 1);

            // SALVAR NO BANCO (IMPORTANTE!)
            PetDAO petDAO = new PetDAO();
            petDAO.inserir(pet);

            int id = pet.getId();
            System.out.println("   → Pet criado com ID: " + id);

            // Verificar se foi salvo
            Pet buscado = petDAO.buscarPorId(id);
            if (buscado == null) {
                System.out.println("❌ CT28 - Remover Pet sem Solicitação: FALHOU (pet não salvo no banco)");
                falharam++;
                return;
            }

            // Remover do abrigo (remove da lista em memória)
            abrigo.removerPet(id);

            // REMOVER DO BANCO DE DADOS
            petDAO.excluir(id);

            // Verificar se foi removido do banco
            Pet removido = petDAO.buscarPorId(id);
            if (removido != null) {
                System.out.println("❌ CT28 - Remover Pet sem Solicitação: FALHOU (não foi removido do banco)");
                falharam++;
                return;
            }

            System.out.println("✅ CT28 - Remover Pet sem Solicitação: PASSou (ID: " + id + ")");
            passaram++;

        } catch (AdocaoException e) {
            System.out.println("❌ CT28 - Remover Pet sem Solicitação: FALHOU - " + e.getMessage());
            falharam++;
        } catch (Exception e) {
            System.out.println("❌ CT28 - Remover Pet sem Solicitação: FALHOU - " + e.getMessage());
            e.printStackTrace();
            falharam++;
        }
    }

    public static void testarRemocaoPetComSolicitacaoPendente() {
        try {
            Abrigo abrigo = abrigoDAO.listarTodos().get(0);
            if (abrigo == null) {
                System.out.println("⚠️ CT29 - Remover Pet com Solicitação Pendente: SKIP");
                return;
            }

            List<Pet> pets = abrigo.getPets();

            Pet petComPendencia = null;
            for (Pet p : pets) {
                if (p.temSolicitacaoPendente()) {
                    petComPendencia = p;
                    break;
                }
            }

            if (petComPendencia == null) {
                System.out.println("⚠️ CT29 - Remover Pet com Solicitação Pendente: SKIP (sem pendente)");
                return;
            }

            try {
                abrigo.removerPet(petComPendencia.getId());
                System.out.println("❌ CT29 - Remover Pet com Solicitação Pendente: FALHOU (não bloqueou)");
                falharam++;
            } catch (AdocaoException e) {
                if (e.getMessage().contains("pendente") || e.getMessage().contains("solicitação")) {
                    System.out.println("✅ CT29 - Remover Pet com Solicitação Pendente: PASSou");
                    passaram++;
                } else {
                    throw e;
                }
            }

        } catch (Exception e) {
            System.out.println("❌ CT29 - Remover Pet com Solicitação Pendente: FALHOU - " + e.getMessage());
            falharam++;
        }
    }
}