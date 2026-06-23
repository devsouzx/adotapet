package com.devsouzx.adotapet.test.exclusao;

import com.devsouzx.adotapet.dao.AdotanteDAO;
import com.devsouzx.adotapet.dao.EnderecoDAO;
import com.devsouzx.adotapet.model.Adotante;
import com.devsouzx.adotapet.model.Endereco;

import java.time.LocalDate;
import java.util.List;

public class ExclusaoAdotanteTest {

    private static AdotanteDAO adotanteDAO = new AdotanteDAO();
    private static EnderecoDAO enderecoDAO = new EnderecoDAO();
    private static int passaram = 0;
    private static int falharam = 0;

    public static void executarTestes() {
        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│               TESTES DE EXCLUSÃO DE ADOTANTE                │");
        System.out.println("└─────────────────────────────────────────────────────────────┘\n");

        testarExclusaoAdotante();
        testarAtualizacaoAdotante();

        System.out.println("\n   → ExclusaoAdotanteTest: " + (passaram + falharam) + " testes | ✅ " + passaram + " | ❌ " + falharam);
    }

    public static void testarExclusaoAdotante() {
        try {
            List<Endereco> enderecos = enderecoDAO.listarTodos();
            if (enderecos.isEmpty()) {
                System.out.println("⚠️ CT27 - Excluir Adotante: SKIP (sem endereço)");
                return;
            }

            Adotante temp = new Adotante(
                    "Temporário", "temp@email.com", "senha123",
                    "11999999999", "111.111.111-11",
                    LocalDate.of(2000, 1, 1), enderecos.get(0)
            );
            adotanteDAO.inserir(temp);

            int id = temp.getId();
            adotanteDAO.excluir(id);

            Adotante buscado = adotanteDAO.buscarPorId(id);
            if (buscado != null) {
                System.out.println("❌ CT27 - Excluir Adotante: FALHOU (não foi excluído)");
                falharam++;
                return;
            }

            System.out.println("✅ CT27 - Excluir Adotante: PASSou (ID: " + id + ")");
            passaram++;

        } catch (Exception e) {
            System.out.println("❌ CT27 - Excluir Adotante: FALHOU - " + e.getMessage());
            falharam++;
        }
    }

    public static void testarAtualizacaoAdotante() {
        try {
            // Criar um adotante temporário para atualizar
            List<Endereco> enderecos = enderecoDAO.listarTodos();
            if (enderecos.isEmpty()) {
                System.out.println("⚠️ CT26 - Atualizar Adotante: SKIP (sem endereço)");
                return;
            }

            Adotante temp = new Adotante(
                    "Teste Update", "update@email.com", "senha123",
                    "11999999999", "222.222.222-22",
                    LocalDate.of(1990, 1, 1), enderecos.get(0)
            );
            adotanteDAO.inserir(temp);

            int id = temp.getId();

            // Buscar o adotante
            Adotante adotante = adotanteDAO.buscarPorId(id);
            if (adotante == null) {
                System.out.println("❌ CT26 - Atualizar Adotante: FALHOU (adotante não encontrado)");
                falharam++;
                return;
            }

            String novoTelefone = "11988888888";
            adotante.setTelefone(novoTelefone);
            adotanteDAO.atualizar(adotante);

            // Buscar novamente para verificar
            Adotante atualizado = adotanteDAO.buscarPorId(id);
            if (atualizado == null) {
                System.out.println("❌ CT26 - Atualizar Adotante: FALHOU (não encontrado após atualização)");
                falharam++;
                return;
            }

            if (!novoTelefone.equals(atualizado.getTelefone())) {
                System.out.println("❌ CT26 - Atualizar Adotante: FALHOU (não atualizou)");
                falharam++;
                return;
            }

            // Limpar - excluir o adotante criado
            adotanteDAO.excluir(id);

            System.out.println("✅ CT26 - Atualizar Adotante: PASSou");
            passaram++;

        } catch (Exception e) {
            System.out.println("❌ CT26 - Atualizar Adotante: FALHOU - " + e.getMessage());
            falharam++;
        }
    }
}