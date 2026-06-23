package com.devsouzx.adotapet.test.solicitacao;

import com.devsouzx.adotapet.dao.*;
import com.devsouzx.adotapet.model.*;
import com.devsouzx.adotapet.model.enums.StatusSolicitacao;
import com.devsouzx.adotapet.exception.AdocaoException;

import java.time.LocalDate;
import java.util.List;

public class SolicitacaoTest {

    private static AdotanteDAO adotanteDAO = new AdotanteDAO();
    private static AbrigoDAO abrigoDAO = new AbrigoDAO();
    private static PetDAO petDAO = new PetDAO();
    private static EnderecoDAO enderecoDAO = new EnderecoDAO();
    private static SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
    private static int passaram = 0;
    private static int falharam = 0;

    public static void executarTestes() {
        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│               TESTES DE SOLICITAÇÃO                         │");
        System.out.println("└─────────────────────────────────────────────────────────────┘\n");

        testarSolicitacaoAdocao();
        testarSolicitacaoPetComPendencia();

        System.out.println("\n   → SolicitacaoTest: " + (passaram + falharam) + " testes | ✅ " + passaram + " | ❌ " + falharam);
    }

    public static void testarSolicitacaoAdocao() {
        try {
            Adotante adotante = adotanteDAO.buscarPorEmail("joao.silva@email.com");
            if (adotante == null) {
                System.out.println("⚠️ CT13 - Solicitar Adoção: SKIP (adotante não encontrado)");
                return;
            }

            PetDAO petDAO = new PetDAO();
            List<Pet> pets = petDAO.listarDisponiveis();
            if (pets.isEmpty()) {
                System.out.println("⚠️ CT13 - Solicitar Adoção: SKIP (sem pets disponíveis)");
                return;
            }

            Pet pet = pets.get(0);

            if (pet.temSolicitacaoPendente()) {
                System.out.println("⚠️ CT13 - Solicitar Adoção: SKIP (pet já tem pendência)");
                return;
            }

            // Solicitar adoção - agora salva no banco!
            adotante.solicitarAdocao(pet);
            System.out.println("   → Solicitação criada para pet: " + pet.getNome());

            // Verificar se foi salvo no banco
            SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
            List<SolicitacaoAdocao> solicitacoes = solicitacaoDAO.listarPorAdotante(adotante.getId());

            if (solicitacoes.isEmpty()) {
                System.out.println("❌ CT13 - Solicitar Adoção: FALHOU (solicitação não salva no banco)");
                falharam++;
                return;
            }

            System.out.println("✅ CT13 - Solicitar Adoção: PASSou (ID: " + solicitacoes.get(0).getId() + ")");
            passaram++;

        } catch (AdocaoException e) {
            System.out.println("❌ CT13 - Solicitar Adoção: FALHOU - " + e.getMessage());
            falharam++;
        } catch (Exception e) {
            System.out.println("❌ CT13 - Solicitar Adoção: FALHOU - " + e.getMessage());
            e.printStackTrace();
            falharam++;
        }
    }

    public static void testarSolicitacaoPetComPendencia() {
        try {
            // Buscar adotante existente
            Adotante adotante1 = adotanteDAO.buscarPorEmail("joao.silva@email.com");
            if (adotante1 == null) {
                System.out.println("⚠️ CT15 - Solicitar Pet com Pendência: SKIP (adotante não encontrado)");
                return;
            }

            // Buscar pets disponíveis
            PetDAO petDAO = new PetDAO();
            List<Pet> pets = petDAO.listarDisponiveis();
            if (pets.isEmpty()) {
                System.out.println("⚠️ CT15 - Solicitar Pet com Pendência: SKIP (sem pets disponíveis)");
                return;
            }

            Pet pet = pets.get(0);

            // Verificar se o pet já tem solicitação pendente
            if (pet.temSolicitacaoPendente()) {
                System.out.println("⚠️ CT15 - Solicitar Pet com Pendência: SKIP (pet já tem pendência)");
                return;
            }

            // Primeiro adotante solicita o pet
            adotante1.solicitarAdocao(pet);
            System.out.println("   → Primeira solicitação criada para pet: " + pet.getNome());

            // Criar segundo adotante
            EnderecoDAO enderecoDAO = new EnderecoDAO();
            List<Endereco> enderecos = enderecoDAO.listarTodos();
            if (enderecos.isEmpty()) {
                System.out.println("⚠️ CT15 - Solicitar Pet com Pendência: SKIP (sem endereço)");
                return;
            }

            Adotante adotante2 = new Adotante(
                    "Teste 2", "teste2@email.com", "senha123",
                    "11777777777", "444.444.444-44",
                    LocalDate.of(1990, 1, 1), enderecos.get(0)
            );
            adotanteDAO.inserir(adotante2);
            System.out.println("   → Segundo adotante criado: " + adotante2.getNome());

            // Tentar solicitar o mesmo pet com o segundo adotante
            try {
                adotante2.solicitarAdocao(pet);
                System.out.println("❌ CT15 - Solicitar Pet com Pendência: FALHOU (não bloqueou)");
                falharam++;
            } catch (AdocaoException e) {
                if (e.getMessage().contains("pendente") || e.getMessage().contains("já foi solicitado")) {
                    System.out.println("✅ CT15 - Solicitar Pet com Pendência: PASSou");
                    passaram++;
                } else {
                    System.out.println("❌ CT15 - Solicitar Pet com Pendência: FALHOU - " + e.getMessage());
                    falharam++;
                }
            }

        } catch (Exception e) {
            System.out.println("❌ CT15 - Solicitar Pet com Pendência: FALHOU - " + e.getMessage());
            e.printStackTrace();
            falharam++;
        }
    }
}