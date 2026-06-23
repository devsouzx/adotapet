package com.devsouzx.adotapet.test.consulta;

import com.devsouzx.adotapet.dao.*;
import com.devsouzx.adotapet.model.*;

import java.util.List;

public class ListagemTest {

    private static AdotanteDAO adotanteDAO = new AdotanteDAO();
    private static AbrigoDAO abrigoDAO = new AbrigoDAO();
    private static PetDAO petDAO = new PetDAO();
    private static int passaram = 0;
    private static int falharam = 0;

    public static void executarTestes() {
        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│               TESTES DE LISTAGEM                            │");
        System.out.println("└─────────────────────────────────────────────────────────────┘\n");

        testarListagemPetsDisponiveis();
        testarListagemPetsPorAbrigo();
        testarListagemAdotantes();
        testarListagemAbrigos();

        System.out.println("\n   → ListagemTest: " + (passaram + falharam) + " testes | ✅ " + passaram + " | ❌ " + falharam);
    }

    public static void testarListagemPetsDisponiveis() {
        try {
            List<Pet> pets = petDAO.listarDisponiveis();

            if (pets == null) {
                System.out.println("❌ CT07 - Listar Pets Disponíveis: FALHOU (null)");
                falharam++;
                return;
            }

            for (Pet pet : pets) {
                if (pet.getStatus() != com.devsouzx.adotapet.model.enums.StatusPet.DISPONIVEL) {
                    System.out.println("❌ CT07 - Listar Pets Disponíveis: FALHOU (status incorreto)");
                    falharam++;
                    return;
                }
            }

            System.out.println("✅ CT07 - Listar Pets Disponíveis: PASSou (" + pets.size() + " encontrados)");
            passaram++;

        } catch (Exception e) {
            System.out.println("❌ CT07 - Listar Pets Disponíveis: FALHOU - " + e.getMessage());
            falharam++;
        }
    }

    public static void testarListagemPetsPorAbrigo() {
        try {
            List<Abrigo> abrigos = abrigoDAO.listarTodos();
            if (abrigos.isEmpty()) {
                System.out.println("⚠️ CT08 - Listar Pets por Abrigo: SKIP (sem abrigo)");
                return;
            }

            List<Pet> pets = petDAO.listarPorAbrigo(abrigos.get(0).getId());

            if (pets == null) {
                System.out.println("❌ CT08 - Listar Pets por Abrigo: FALHOU (null)");
                falharam++;
                return;
            }

            System.out.println("✅ CT08 - Listar Pets por Abrigo: PASSou (" + pets.size() + " encontrados)");
            passaram++;

        } catch (Exception e) {
            System.out.println("❌ CT08 - Listar Pets por Abrigo: FALHOU - " + e.getMessage());
            falharam++;
        }
    }

    public static void testarListagemAdotantes() {
        try {
            List<Adotante> adotantes = adotanteDAO.listarTodos();

            if (adotantes == null) {
                System.out.println("❌ CT09 - Listar Adotantes: FALHOU (null)");
                falharam++;
                return;
            }

            System.out.println("✅ CT09 - Listar Adotantes: PASSou (" + adotantes.size() + " encontrados)");
            passaram++;

        } catch (Exception e) {
            System.out.println("❌ CT09 - Listar Adotantes: FALHOU - " + e.getMessage());
            falharam++;
        }
    }

    public static void testarListagemAbrigos() {
        try {
            List<Abrigo> abrigos = abrigoDAO.listarTodos();

            if (abrigos == null) {
                System.out.println("❌ CT10 - Listar Abrigos: FALHOU (null)");
                falharam++;
                return;
            }

            System.out.println("✅ CT10 - Listar Abrigos: PASSou (" + abrigos.size() + " encontrados)");
            passaram++;

        } catch (Exception e) {
            System.out.println("❌ CT10 - Listar Abrigos: FALHOU - " + e.getMessage());
            falharam++;
        }
    }
}
