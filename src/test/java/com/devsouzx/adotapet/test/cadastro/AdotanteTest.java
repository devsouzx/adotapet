package com.devsouzx.adotapet.test.cadastro;

import com.devsouzx.adotapet.dao.AdotanteDAO;
import com.devsouzx.adotapet.dao.EnderecoDAO;
import com.devsouzx.adotapet.model.Adotante;
import com.devsouzx.adotapet.model.Endereco;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AdotanteTest {

    private static AdotanteDAO adotanteDAO = new AdotanteDAO();
    private static EnderecoDAO enderecoDAO = new EnderecoDAO();
    private static int passaram = 0;
    private static int falharam = 0;

    public static void executarTestes() {
        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│               TESTES DE ADOTANTE                            │");
        System.out.println("└─────────────────────────────────────────────────────────────┘\n");

        testarCadastroAdotante();
        testarCadastroAdotanteEmailDuplicado();

        System.out.println("\n   → AdotanteTest: " + (passaram + falharam) + " testes | ✅ " + passaram + " | ❌ " + falharam);
    }

    public static void testarCadastroAdotante() {
        try {
            List<Endereco> enderecos = enderecoDAO.listarTodos();
            if (enderecos.isEmpty()) {
                System.out.println("⚠️ CT02 - Cadastrar Adotante: SKIP (sem endereço)");
                return;
            }

            Adotante adotante = new Adotante(
                    "João Silva", "joao.silva@email.com", "senha123",
                    "11999999999", "123.456.789-00",
                    LocalDate.of(1990, 1, 15), enderecos.get(0)
            );
            adotanteDAO.inserir(adotante);

            if (adotante.getId() <= 0) {
                System.out.println("❌ CT02 - Cadastrar Adotante: FALHOU (ID inválido)");
                falharam++;
                return;
            }

            System.out.println("✅ CT02 - Cadastrar Adotante: PASSou (ID: " + adotante.getId() + ")");
            passaram++;

        } catch (Exception e) {
            System.out.println("❌ CT02 - Cadastrar Adotante: FALHOU - " + e.getMessage());
            falharam++;
        }
    }

    public static void testarCadastroAdotanteEmailDuplicado() {
        try {
            List<Endereco> enderecos = enderecoDAO.listarTodos();
            if (enderecos.isEmpty()) {
                System.out.println("⚠️ CT03 - Cadastrar Adotante (E-mail duplicado): SKIP");
                return;
            }

            Adotante adotante = new Adotante(
                    "Maria Silva", "joao.silva@email.com", "senha456",
                    "11888888888", "987.654.321-00",
                    LocalDate.of(1995, 5, 20), enderecos.get(0)
            );

            try {
                adotanteDAO.inserir(adotante);
                System.out.println("❌ CT03 - Cadastrar Adotante (E-mail duplicado): FALHOU (não bloqueou)");
                falharam++;
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate entry") || e.getMessage().contains("duplicate")) {
                    System.out.println("✅ CT03 - Cadastrar Adotante (E-mail duplicado): PASSou");
                    passaram++;
                } else {
                    System.out.println("❌ CT03 - Cadastrar Adotante (E-mail duplicado): FALHOU - " + e.getMessage());
                    falharam++;
                }
            }

        } catch (Exception e) {
            System.out.println("❌ CT03 - Cadastrar Adotante (E-mail duplicado): FALHOU - " + e.getMessage());
            falharam++;
        }
    }
}