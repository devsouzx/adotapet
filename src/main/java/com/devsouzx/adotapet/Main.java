package com.devsouzx.adotapet;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== TESTE DE CONEXÃO ===\n");

        util.ConexaoBD.testarConexao();

        System.out.println("\n=== FIM DO TESTE ===");

        util.ConexaoBD.fecharConexao();
    }
}