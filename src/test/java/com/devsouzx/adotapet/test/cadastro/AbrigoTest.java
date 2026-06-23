package com.devsouzx.adotapet.test.cadastro;

import com.devsouzx.adotapet.dao.AbrigoDAO;
import com.devsouzx.adotapet.dao.EnderecoDAO;
import com.devsouzx.adotapet.model.Abrigo;
import com.devsouzx.adotapet.model.Endereco;

import java.sql.SQLException;
import java.util.List;

public class AbrigoTest {

    private static AbrigoDAO abrigoDAO = new AbrigoDAO();
    private static EnderecoDAO enderecoDAO = new EnderecoDAO();
    private static int passaram = 0;
    private static int falharam = 0;

    public static void executarTestes() {
        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│               TESTES DE ABRIGO                              │");
        System.out.println("└─────────────────────────────────────────────────────────────┘\n");

        testarCadastroAbrigo();
        testarCadastroAbrigoCnpjDuplicado();

        System.out.println("\n   → AbrigoTest: " + (passaram + falharam) + " testes | ✅ " + passaram + " | ❌ " + falharam);
    }

    public static void testarCadastroAbrigo() {
        try {
            List<Endereco> enderecos = enderecoDAO.listarTodos();
            if (enderecos.isEmpty()) {
                System.out.println("⚠️ CT04 - Cadastrar Abrigo: SKIP (sem endereço)");
                return;
            }

            Abrigo abrigo = new Abrigo(
                    "Abrigo Amigo Fiel", "contato@amigofiel.com", "senha123",
                    "1133333333", "12.345.678/0001-90",
                    "Ana Souza", enderecos.get(0)
            );
            abrigoDAO.inserir(abrigo);

            if (abrigo.getId() <= 0) {
                System.out.println("❌ CT04 - Cadastrar Abrigo: FALHOU (ID inválido)");
                falharam++;
                return;
            }

            System.out.println("✅ CT04 - Cadastrar Abrigo: PASSou (ID: " + abrigo.getId() + ")");
            passaram++;

        } catch (Exception e) {
            System.out.println("❌ CT04 - Cadastrar Abrigo: FALHOU - " + e.getMessage());
            falharam++;
        }
    }

    public static void testarCadastroAbrigoCnpjDuplicado() {
        try {
            List<Endereco> enderecos = enderecoDAO.listarTodos();
            if (enderecos.isEmpty()) {
                System.out.println("⚠️ CT05 - Cadastrar Abrigo (CNPJ duplicado): SKIP");
                return;
            }

            Abrigo abrigo = new Abrigo(
                    "Abrigo Outro", "outro@email.com", "senha789",
                    "1144444444", "12.345.678/0001-90",
                    "Carlos Lima", enderecos.get(0)
            );

            try {
                abrigoDAO.inserir(abrigo);
                System.out.println("❌ CT05 - Cadastrar Abrigo (CNPJ duplicado): FALHOU (não bloqueou)");
                falharam++;
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate entry") || e.getMessage().contains("duplicate")) {
                    System.out.println("✅ CT05 - Cadastrar Abrigo (CNPJ duplicado): PASSou");
                    passaram++;
                } else {
                    System.out.println("❌ CT05 - Cadastrar Abrigo (CNPJ duplicado): FALHOU - " + e.getMessage());
                    falharam++;
                }
            }

        } catch (Exception e) {
            System.out.println("❌ CT05 - Cadastrar Abrigo (CNPJ duplicado): FALHOU - " + e.getMessage());
            falharam++;
        }
    }
}