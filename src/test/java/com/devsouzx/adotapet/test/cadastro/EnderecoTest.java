package com.devsouzx.adotapet.test.cadastro;

import com.devsouzx.adotapet.dao.EnderecoDAO;
import com.devsouzx.adotapet.model.Endereco;

public class EnderecoTest {

    private static EnderecoDAO enderecoDAO = new EnderecoDAO();
    private static int passaram = 0;
    private static int falharam = 0;

    public static void executarTestes() {
        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│               TESTES DE ENDEREÇO                            │");
        System.out.println("└─────────────────────────────────────────────────────────────┘\n");

        testarCadastroEndereco();

        System.out.println("\n   → EndereçoTest: " + (passaram + falharam) + " testes | ✅ " + passaram + " | ❌ " + falharam);
    }

    public static void testarCadastroEndereco() {
        try {
            Endereco endereco = new Endereco(
                    "Rua das Flores", "123", "Centro",
                    "São Paulo", "SP", "01001-000"
            );
            enderecoDAO.inserir(endereco);

            if (endereco.getId() <= 0) {
                System.out.println("❌ CT01 - Cadastrar Endereço: FALHOU (ID inválido)");
                falharam++;
                return;
            }

            Endereco buscado = enderecoDAO.buscarPorId(endereco.getId());
            if (buscado == null || !buscado.getLogradouro().equals("Rua das Flores")) {
                System.out.println("❌ CT01 - Cadastrar Endereço: FALHOU (dados incorretos)");
                falharam++;
                return;
            }

            System.out.println("✅ CT01 - Cadastrar Endereço: PASSou (ID: " + endereco.getId() + ")");
            passaram++;

        } catch (Exception e) {
            System.out.println("❌ CT01 - Cadastrar Endereço: FALHOU - " + e.getMessage());
            falharam++;
        }
    }
}
