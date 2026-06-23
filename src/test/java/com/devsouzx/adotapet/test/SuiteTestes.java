package com.devsouzx.adotapet.test;

import com.devsouzx.adotapet.test.aprovacao.AprovacaoTest;
import com.devsouzx.adotapet.test.aprovacao.RecusaTest;
import com.devsouzx.adotapet.test.avaliacao.AvaliacaoTest;
import com.devsouzx.adotapet.test.cadastro.AbrigoTest;
import com.devsouzx.adotapet.test.cadastro.AdotanteTest;
import com.devsouzx.adotapet.test.cadastro.EnderecoTest;
import com.devsouzx.adotapet.test.cadastro.PetTest;
import com.devsouzx.adotapet.test.consulta.BuscaTest;
import com.devsouzx.adotapet.test.consulta.ListagemTest;
import com.devsouzx.adotapet.test.exclusao.ExclusaoAdotanteTest;
import com.devsouzx.adotapet.test.exclusao.RemocaoPetTest;
import com.devsouzx.adotapet.test.solicitacao.CancelamentoTest;
import com.devsouzx.adotapet.test.solicitacao.LimiteSolicitacoesTest;
import com.devsouzx.adotapet.test.solicitacao.SolicitacaoTest;

public class SuiteTestes {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║      SUITE DE TESTES - SISTEMA DE ADOÇÃO DE PETS        ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");

        // TESTES DE CADASTRO
        EnderecoTest.executarTestes();
        AdotanteTest.executarTestes();
        AbrigoTest.executarTestes();
        PetTest.executarTestes();

        // TESTES DE CONSULTA
        ListagemTest.executarTestes();
        BuscaTest.executarTestes();

        // TESTES DE SOLICITAÇÃO
        SolicitacaoTest.executarTestes();
        LimiteSolicitacoesTest.executarTestes();
        CancelamentoTest.executarTestes();

        // TESTES DE APROVAÇÃO E RECUSA
        AprovacaoTest.executarTestes();
        RecusaTest.executarTestes();

        // TESTES DE AVALIAÇÃO
        AvaliacaoTest.executarTestes();

        // TESTES DE EXCLUSÃO
        ExclusaoAdotanteTest.executarTestes();
        RemocaoPetTest.executarTestes();

        System.out.println("\nTODOS OS TESTES CONCLUÍDOS!");
    }
}
