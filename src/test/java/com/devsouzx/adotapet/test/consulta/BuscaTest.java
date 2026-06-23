package com.devsouzx.adotapet.test.consulta;

import com.devsouzx.adotapet.dao.AdotanteDAO;
import com.devsouzx.adotapet.dao.AbrigoDAO;
import com.devsouzx.adotapet.model.Adotante;
import com.devsouzx.adotapet.model.Abrigo;

public class BuscaTest {

    private static AdotanteDAO adotanteDAO = new AdotanteDAO();
    private static AbrigoDAO abrigoDAO = new AbrigoDAO();
    private static int passaram = 0;
    private static int falharam = 0;

    public static void executarTestes() {
        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│               TESTES DE BUSCA                               │");
        System.out.println("└─────────────────────────────────────────────────────────────┘\n");

        testarBuscaAdotantePorEmail();
        testarBuscaAbrigoPorEmail();

        System.out.println("\n   → BuscaTest: " + (passaram + falharam) + " testes | ✅ " + passaram + " | ❌ " + falharam);
    }

    public static void testarBuscaAdotantePorEmail() {
        try {
            Adotante adotante = adotanteDAO.buscarPorEmail("joao.silva@email.com");

            if (adotante == null) {
                System.out.println("❌ CT11 - Buscar Adotante por E-mail: FALHOU (não encontrado)");
                falharam++;
                return;
            }

            System.out.println("✅ CT11 - Buscar Adotante por E-mail: PASSou (" + adotante.getNome() + ")");
            passaram++;

        } catch (Exception e) {
            System.out.println("❌ CT11 - Buscar Adotante por E-mail: FALHOU - " + e.getMessage());
            falharam++;
        }
    }

    public static void testarBuscaAbrigoPorEmail() {
        try {
            Abrigo abrigo = abrigoDAO.buscarPorEmail("contato@amigofiel.com");

            if (abrigo == null) {
                System.out.println("❌ CT12 - Buscar Abrigo por E-mail: FALHOU (não encontrado)");
                falharam++;
                return;
            }

            System.out.println("✅ CT12 - Buscar Abrigo por E-mail: PASSou (" + abrigo.getNome() + ")");
            passaram++;

        } catch (Exception e) {
            System.out.println("❌ CT12 - Buscar Abrigo por E-mail: FALHOU - " + e.getMessage());
            falharam++;
        }
    }
}