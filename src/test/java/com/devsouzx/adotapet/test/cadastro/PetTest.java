package com.devsouzx.adotapet.test.cadastro;

import com.devsouzx.adotapet.dao.AbrigoDAO;
import com.devsouzx.adotapet.dao.PetDAO;
import com.devsouzx.adotapet.model.Abrigo;
import com.devsouzx.adotapet.model.Pet;
import com.devsouzx.adotapet.model.enums.Porte;
import com.devsouzx.adotapet.model.enums.StatusPet;

import java.util.List;

public class PetTest {

    private static AbrigoDAO abrigoDAO = new AbrigoDAO();
    private static PetDAO petDAO = new PetDAO();
    private static int passaram = 0;
    private static int falharam = 0;

    public static void executarTestes() {
        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│               TESTES DE PET                                 │");
        System.out.println("└─────────────────────────────────────────────────────────────┘\n");

        testarCadastroPet();

        System.out.println("\n   → PetTest: " + (passaram + falharam) + " testes | ✅ " + passaram + " | ❌ " + falharam);
    }

    public static void testarCadastroPet() {
        try {
            List<Abrigo> abrigos = abrigoDAO.listarTodos();
            if (abrigos.isEmpty()) {
                System.out.println("⚠️ CT06 - Cadastrar Pet: SKIP (sem abrigo)");
                return;
            }

            Abrigo abrigo = abrigos.get(0);

            // Cadastrar pet
            abrigo.cadastrarPet(
                    "Rex", "CACHORRO", "Labrador", 24,
                    Porte.GRANDE, "Cachorro muito amigável", null
            );

            // Buscar o pet cadastrado
            List<Pet> pets = abrigo.getPets();
            if (pets.isEmpty()) {
                System.out.println("❌ CT06 - Cadastrar Pet: FALHOU (pet não criado)");
                falharam++;
                return;
            }

            Pet pet = pets.get(pets.size() - 1);

            // Salvar no banco de dados (IMPORTANTE!)
            PetDAO petDAO = new PetDAO();
            petDAO.inserir(pet);

            // Verificar se foi salvo
            Pet buscado = petDAO.buscarPorId(pet.getId());
            if (buscado == null) {
                System.out.println("❌ CT06 - Cadastrar Pet: FALHOU (pet não salvo no banco)");
                falharam++;
                return;
            }

            System.out.println("✅ CT06 - Cadastrar Pet: PASSou (ID: " + pet.getId() + ")");
            passaram++;

        } catch (Exception e) {
            System.out.println("❌ CT06 - Cadastrar Pet: FALHOU - " + e.getMessage());
            falharam++;
        }
    }
}