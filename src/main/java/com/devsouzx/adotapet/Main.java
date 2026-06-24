package com.devsouzx.adotapet;

import com.devsouzx.adotapet.dao.AbrigoDAO;
import com.devsouzx.adotapet.dao.AdotanteDAO;
import com.devsouzx.adotapet.dao.EnderecoDAO;
import com.devsouzx.adotapet.dao.PetDAO;
import com.devsouzx.adotapet.dao.SolicitacaoDAO;
import com.devsouzx.adotapet.model.Abrigo;
import com.devsouzx.adotapet.model.Adotante;
import com.devsouzx.adotapet.model.Endereco;
import com.devsouzx.adotapet.model.Pet;
import com.devsouzx.adotapet.model.SolicitacaoAdocao;
import com.devsouzx.adotapet.model.enums.Porte;
import com.devsouzx.adotapet.model.enums.StatusSolicitacao;
import com.devsouzx.adotapet.util.ConexaoBD;

import javax.swing.*;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.List;

/**
 * Classe principal do sistema Adotapet.
 * Responsável pela interface gráfica e interação com o usuário via JOptionPane.
 *
 * <p>O sistema permite que adotantes e abrigos realizem login, cadastro,
 * gerenciem pets e solicitações de adoção.</p>
 *
 * @author Equipe Adoção de Pets
 * @version 1.0
 * @since 2026
 */
public class Main {

    /** DAO para operações com Pets */
    private static final PetDAO petDAO = new PetDAO();

    /** DAO para operações com Abrigos */
    private static final AbrigoDAO abrigoDAO = new AbrigoDAO();

    /** DAO para operações com Adotantes */
    private static final AdotanteDAO adotanteDAO = new AdotanteDAO();

    /** DAO para operações com Endereços */
    private static final EnderecoDAO enderecoDAO = new EnderecoDAO();

    /** DAO para operações com Solicitações de Adoção */
    private static final SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();

    /**
     * Ponto de entrada principal do sistema.
     *
     * @param args Argumentos da linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        ConexaoBD.testarConexao();
        exibirMenuPublico();
        ConexaoBD.fecharConexao();
    }

    /**
     * Exibe o menu público do sistema, acessível a qualquer visitante.
     * Opções: Login, Cadastro, Visualização de Pets e Abrigos.
     */
    private static void exibirMenuPublico() {
        while (true) {
            String opcao = JOptionPane.showInputDialog(
                    """
                            ===== ADOTAPET =====

                            1 - Entrar como Adotante
                            2 - Entrar como Abrigo
                            3 - Cadastrar Adotante
                            4 - Cadastrar Abrigo
                            5 - Ver Pets Disponiveis
                            6 - Buscar Pet por ID
                            7 - Listar Abrigos
                            0 - Sair

                            Escolha uma opcao:
                            """
            );

            if (opcao == null || opcao.equals("0")) {
                return;
            }

            try {
                switch (opcao) {
                    case "1" -> {
                        Adotante adotante = loginAdotante();
                        if (adotante != null) {
                            exibirMenuAdotante(adotante);
                        }
                    }
                    case "2" -> {
                        Abrigo abrigo = loginAbrigo();
                        if (abrigo != null) {
                            exibirMenuAbrigo(abrigo);
                        }
                    }
                    case "3" -> cadastrarAdotante();
                    case "4" -> cadastrarAbrigo();
                    case "5" -> listarPets(true);
                    case "6" -> buscarPetPorId();
                    case "7" -> listarAbrigos();
                    default -> JOptionPane.showMessageDialog(null, "Opcao invalida!");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
            }
        }
    }

    /**
     * Exibe o menu específico para um adotante logado.
     *
     * @param adotante Adotante atualmente logado
     */
    private static void exibirMenuAdotante(Adotante adotante) {
        while (true) {
            String opcao = JOptionPane.showInputDialog(
                    """
                            ===== MENU ADOTANTE =====

                            1 - Ver Pets Disponiveis
                            2 - Buscar Pet por ID
                            3 - Solicitar Adocao
                            4 - Listar Minhas Solicitacoes
                            5 - Cancelar Solicitacao Pendente
                            0 - Sair da Conta

                            Escolha uma opcao:
                            """
            );

            if (opcao == null || opcao.equals("0")) {
                return;
            }

            try {
                switch (opcao) {
                    case "1" -> listarPets(true);
                    case "2" -> buscarPetPorId();
                    case "3" -> solicitarAdocao(adotante);
                    case "4" -> listarSolicitacoesDoAdotante(adotante);
                    case "5" -> cancelarSolicitacao(adotante);
                    default -> JOptionPane.showMessageDialog(null, "Opcao invalida!");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
            }
        }
    }

    /**
     * Exibe o menu específico para um abrigo logado.
     *
     * @param abrigo Abrigo atualmente logado
     */
    private static void exibirMenuAbrigo(Abrigo abrigo) {
        while (true) {
            String opcao = JOptionPane.showInputDialog(
                    """
                            ===== MENU ABRIGO =====

                            1 - Cadastrar Pet
                            2 - Listar Meus Pets
                            3 - Listar Solicitacoes Recebidas
                            4 - Aprovar Solicitacao
                            5 - Recusar Solicitacao
                            6 - Remover Pet
                            0 - Sair da Conta

                            Escolha uma opcao:
                            """
            );

            if (opcao == null || opcao.equals("0")) {
                return;
            }

            try {
                switch (opcao) {
                    case "1" -> cadastrarPet(abrigo);
                    case "2" -> listarPetsDoAbrigo(abrigo);
                    case "3" -> listarSolicitacoesDoAbrigo(abrigo);
                    case "4" -> aprovarSolicitacao(abrigo);
                    case "5" -> recusarSolicitacao(abrigo);
                    case "6" -> removerPet(abrigo);
                    default -> JOptionPane.showMessageDialog(null, "Opcao invalida!");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
            }
        }
    }

    /**
     * Realiza o login de um adotante.
     *
     * @return Objeto Adotante se autenticado, ou null se falhar
     */
    private static Adotante loginAdotante() {
        String email = lerTexto("Email do adotante:");
        String senha = lerTexto("Senha:");
        Adotante adotante = adotanteDAO.buscarPorEmail(email);

        if (adotante == null || !adotante.autenticar(email, senha)) {
            JOptionPane.showMessageDialog(null, "Email ou senha invalidos para adotante.");
            return null;
        }

        JOptionPane.showMessageDialog(null, "Bem-vindo(a), " + adotante.getNome() + "!");
        return adotante;
    }

    /**
     * Realiza o login de um abrigo.
     *
     * @return Objeto Abrigo se autenticado, ou null se falhar
     */
    private static Abrigo loginAbrigo() {
        String email = lerTexto("Email do abrigo:");
        String senha = lerTexto("Senha:");
        Abrigo abrigo = abrigoDAO.buscarPorEmail(email);

        if (abrigo == null || !abrigo.autenticar(email, senha)) {
            JOptionPane.showMessageDialog(null, "Email ou senha invalidos para abrigo.");
            return null;
        }

        JOptionPane.showMessageDialog(null, "Bem-vindo(a), " + abrigo.getNome() + "!");
        return abrigo;
    }

    /**
     * Lista todos os pets ou apenas os disponíveis.
     *
     * @param apenasDisponiveis true para listar apenas disponíveis, false para todos
     */
    private static void listarPets(boolean apenasDisponiveis) {
        List<Pet> pets = apenasDisponiveis ? petDAO.listarDisponiveis() : petDAO.listarTodos();
        exibirPets(pets, "Nenhum pet encontrado.");
    }

    /**
     * Lista os pets de um abrigo específico.
     *
     * @param abrigo Abrigo cujos pets serão listados
     */
    private static void listarPetsDoAbrigo(Abrigo abrigo) {
        List<Pet> pets = petDAO.listarPorAbrigo(abrigo.getId());
        exibirPets(pets, "Nenhum pet encontrado para este abrigo.");
    }

    /**
     * Exibe uma lista de pets em uma janela de diálogo.
     *
     * @param pets Lista de pets a ser exibida
     * @param mensagemVazia Mensagem a ser exibida se a lista estiver vazia
     */
    private static void exibirPets(List<Pet> pets, String mensagemVazia) {
        StringBuilder sb = new StringBuilder();

        for (Pet pet : pets) {
            sb.append("ID: ").append(pet.getId())
                    .append("\nNome: ").append(pet.getNome())
                    .append("\nEspecie: ").append(pet.getEspecie())
                    .append("\nRaca: ").append(pet.getRaca())
                    .append("\nIdade (meses): ").append(pet.getIdadeMeses())
                    .append("\nPorte: ").append(pet.getPorte())
                    .append("\nStatus: ").append(pet.getStatus())
                    .append("\n----------------------\n");
        }

        JOptionPane.showMessageDialog(null, sb.length() == 0 ? mensagemVazia : sb.toString());
    }

    /**
     * Busca e exibe um pet pelo seu ID.
     */
    private static void buscarPetPorId() {
        int id = lerInteiro("Digite o ID do pet:");
        Pet pet = petDAO.buscarPorId(id);

        if (pet == null) {
            JOptionPane.showMessageDialog(null, "Pet nao encontrado.");
            return;
        }

        String abrigo = pet.getAbrigo() == null ? "Nao carregado" : pet.getAbrigo().getNome();
        JOptionPane.showMessageDialog(null,
                "ID: " + pet.getId() +
                        "\nNome: " + pet.getNome() +
                        "\nEspecie: " + pet.getEspecie() +
                        "\nRaca: " + pet.getRaca() +
                        "\nIdade (meses): " + pet.getIdadeMeses() +
                        "\nPorte: " + pet.getPorte() +
                        "\nDescricao: " + pet.getDescricao() +
                        "\nFoto: " + pet.getFoto() +
                        "\nStatus: " + pet.getStatus() +
                        "\nAbrigo: " + abrigo);
    }

    /**
     * Lista todos os abrigos cadastrados.
     */
    private static void listarAbrigos() {
        List<Abrigo> abrigos = abrigoDAO.listarTodos();
        StringBuilder sb = new StringBuilder();

        for (Abrigo abrigo : abrigos) {
            sb.append("ID: ").append(abrigo.getId())
                    .append("\nNome: ").append(abrigo.getNome())
                    .append("\nEmail: ").append(abrigo.getEmail())
                    .append("\nTelefone: ").append(abrigo.getTelefone())
                    .append("\nResponsavel: ").append(abrigo.getNomeResponsavel())
                    .append("\nHorario: ").append(abrigo.getHorarioFuncionamento())
                    .append("\n----------------------\n");
        }

        JOptionPane.showMessageDialog(null,
                sb.length() == 0 ? "Nenhum abrigo encontrado." : sb.toString());
    }

    /**
     * Cadastra um novo adotante no sistema.
     *
     * @throws Exception Se algum dado obrigatório estiver faltando
     */
    private static void cadastrarAdotante() throws Exception {
        JTextField nome = new JTextField();
        JTextField email = new JTextField();
        JPasswordField senha = new JPasswordField();
        JTextField telefone = new JTextField();
        JTextField cpf = new JTextField();
        JTextField dataNascimento = new JTextField("2000-01-01");
        JTextField logradouro = new JTextField();
        JTextField numero = new JTextField();
        JTextField bairro = new JTextField();
        JTextField cidade = new JTextField();
        JTextField estado = new JTextField();
        JTextField cep = new JTextField();

        JPanel painel = criarFormulario(
                "Nome completo:", nome,
                "Email:", email,
                "Senha:", senha,
                "Telefone:", telefone,
                "CPF:", cpf,
                "Data nascimento (AAAA-MM-DD):", dataNascimento,
                "Logradouro:", logradouro,
                "Numero:", numero,
                "Bairro:", bairro,
                "Cidade:", cidade,
                "Estado (UF):", estado,
                "CEP:", cep
        );

        if (!confirmarFormulario("Cadastrar Adotante", painel)) {
            return;
        }

        Endereco endereco = new Endereco(
                textoObrigatorio(logradouro, "Logradouro"),
                textoObrigatorio(numero, "Numero"),
                textoObrigatorio(bairro, "Bairro"),
                textoObrigatorio(cidade, "Cidade"),
                textoUF(estado),
                textoObrigatorio(cep, "CEP")
        );
        LocalDate nascimento = LocalDate.parse(textoObrigatorio(dataNascimento, "Data de nascimento"));

        salvarEndereco(endereco);
        Adotante adotante = new Adotante(
                textoObrigatorio(nome, "Nome"),
                textoObrigatorio(email, "Email"),
                textoObrigatorio(senha, "Senha"),
                textoObrigatorio(telefone, "Telefone"),
                textoObrigatorio(cpf, "CPF"),
                nascimento,
                endereco
        );
        adotanteDAO.inserir(adotante);

        JOptionPane.showMessageDialog(null, "Adotante cadastrado com ID: " + adotante.getId());
    }

    /**
     * Cadastra um novo abrigo no sistema.
     *
     * @throws Exception Se algum dado obrigatório estiver faltando
     */
    private static void cadastrarAbrigo() throws Exception {
        JTextField nome = new JTextField();
        JTextField email = new JTextField();
        JPasswordField senha = new JPasswordField();
        JTextField telefone = new JTextField();
        JTextField cnpj = new JTextField();
        JTextField responsavel = new JTextField();
        JTextField horario = new JTextField("Segunda a Sexta, 9h as 18h");
        JTextField logradouro = new JTextField();
        JTextField numero = new JTextField();
        JTextField bairro = new JTextField();
        JTextField cidade = new JTextField();
        JTextField estado = new JTextField();
        JTextField cep = new JTextField();

        JPanel painel = criarFormulario(
                "Nome do abrigo:", nome,
                "Email:", email,
                "Senha:", senha,
                "Telefone:", telefone,
                "CNPJ:", cnpj,
                "Responsavel:", responsavel,
                "Horario:", horario,
                "Logradouro:", logradouro,
                "Numero:", numero,
                "Bairro:", bairro,
                "Cidade:", cidade,
                "Estado (UF):", estado,
                "CEP:", cep
        );

        if (!confirmarFormulario("Cadastrar Abrigo", painel)) {
            return;
        }

        Endereco endereco = new Endereco(
                textoObrigatorio(logradouro, "Logradouro"),
                textoObrigatorio(numero, "Numero"),
                textoObrigatorio(bairro, "Bairro"),
                textoObrigatorio(cidade, "Cidade"),
                textoUF(estado),
                textoObrigatorio(cep, "CEP")
        );

        salvarEndereco(endereco);
        Abrigo abrigo = new Abrigo(
                textoObrigatorio(nome, "Nome"),
                textoObrigatorio(email, "Email"),
                textoObrigatorio(senha, "Senha"),
                textoObrigatorio(telefone, "Telefone"),
                textoObrigatorio(cnpj, "CNPJ"),
                textoObrigatorio(responsavel, "Responsavel"),
                endereco
        );
        abrigo.setHorarioFuncionamento(textoObrigatorio(horario, "Horario"));
        abrigoDAO.inserir(abrigo);

        JOptionPane.showMessageDialog(null, "Abrigo cadastrado com ID: " + abrigo.getId());
    }

    /**
     * Cadastra um novo endereço.
     *
     * @return Endereço criado
     */
    private static Endereco cadastrarEndereco() {
        Endereco endereco = new Endereco(
                lerTexto("Logradouro:"),
                lerTexto("Numero:"),
                lerTexto("Bairro:"),
                lerTexto("Cidade:"),
                lerUF("Estado (UF):"),
                lerTexto("CEP:")
        );
        salvarEndereco(endereco);
        return endereco;
    }

    /**
     * Cadastra um novo pet para um abrigo.
     *
     * @param abrigo Abrigo responsável pelo pet
     * @throws Exception Se algum dado obrigatório estiver faltando
     */
    private static void cadastrarPet(Abrigo abrigo) throws Exception {
        JTextField nome = new JTextField();
        JComboBox<String> especie = new JComboBox<>(new String[]{"CACHORRO", "GATO", "OUTRO"});
        JTextField raca = new JTextField();
        JTextField idadeMeses = new JTextField();
        JComboBox<Porte> porte = new JComboBox<>(Porte.values());
        JTextField descricao = new JTextField();
        JTextField foto = new JTextField();

        JPanel painel = criarFormulario(
                "Nome:", nome,
                "Especie:", especie,
                "Raca:", raca,
                "Idade em meses:", idadeMeses,
                "Porte:", porte,
                "Descricao:", descricao,
                "Foto (opcional):", foto
        );

        if (!confirmarFormulario("Cadastrar Pet", painel)) {
            return;
        }

        Pet pet = new Pet(
                textoObrigatorio(nome, "Nome"),
                String.valueOf(especie.getSelectedItem()),
                textoObrigatorio(raca, "Raca"),
                Integer.parseInt(textoObrigatorio(idadeMeses, "Idade em meses")),
                (Porte) porte.getSelectedItem(),
                textoObrigatorio(descricao, "Descricao"),
                foto.getText().trim().isEmpty() ? null : foto.getText().trim(),
                abrigo
        );
        petDAO.inserir(pet);

        JOptionPane.showMessageDialog(null, "Pet cadastrado com ID: " + pet.getId());
    }

    /**
     * Realiza uma solicitação de adoção para um pet.
     *
     * @param adotante Adotante solicitante
     * @throws Exception Se houver erro na solicitação
     */
    private static void solicitarAdocao(Adotante adotante) throws Exception {
        int petId = lerInteiro("ID do pet:");
        Pet pet = petDAO.buscarPorId(petId);

        if (pet == null) {
            JOptionPane.showMessageDialog(null, "Pet nao encontrado.");
            return;
        }

        if (!pet.isDisponivel()) {
            JOptionPane.showMessageDialog(null, "Este pet nao esta disponivel para adocao.");
            return;
        }

        if (solicitacaoDAO.contarSolicitacoesPendentesPorAdotante(adotante.getId()) >= 3) {
            JOptionPane.showMessageDialog(null,
                    "Voce ja possui 3 solicitacoes pendentes. Aguarde a resposta dos abrigos antes de fazer uma nova solicitacao.");
            return;
        }

        if (solicitacaoDAO.petPossuiSolicitacaoPendente(petId)) {
            JOptionPane.showMessageDialog(null, "Este pet ja possui uma solicitacao pendente.");
            return;
        }

        if (solicitacaoDAO.adotantePossuiSolicitacaoPendenteParaPet(adotante.getId(), petId)) {
            JOptionPane.showMessageDialog(null, "Voce ja solicitou este pet.");
            return;
        }

        SolicitacaoAdocao solicitacao = new SolicitacaoAdocao(adotante, pet);
        solicitacaoDAO.inserir(solicitacao);

        JOptionPane.showMessageDialog(null, "Solicitacao criada com ID: " + solicitacao.getId());
    }

    /**
     * Lista as solicitações de um adotante.
     *
     * @param adotante Adotante cujas solicitações serão listadas
     */
    private static void listarSolicitacoesDoAdotante(Adotante adotante) {
        List<SolicitacaoAdocao> solicitacoes = solicitacaoDAO.listarPorAdotante(adotante.getId());
        exibirSolicitacoes(solicitacoes, "Nenhuma solicitacao encontrada para este adotante.");
    }

    /**
     * Lista as solicitações recebidas por um abrigo.
     *
     * @param abrigo Abrigo que recebeu as solicitações
     */
    private static void listarSolicitacoesDoAbrigo(Abrigo abrigo) {
        List<SolicitacaoAdocao> solicitacoes = solicitacaoDAO.listarTodos().stream()
                .filter(solicitacao -> pertenceAoAbrigo(solicitacao, abrigo))
                .toList();
        exibirSolicitacoes(solicitacoes, "Nenhuma solicitacao encontrada para este abrigo.");
    }

    /**
     * Exibe uma lista de solicitações em uma janela de diálogo.
     *
     * @param solicitacoes Lista de solicitações a ser exibida
     * @param mensagemVazia Mensagem a ser exibida se a lista estiver vazia
     */
    private static void exibirSolicitacoes(List<SolicitacaoAdocao> solicitacoes, String mensagemVazia) {
        StringBuilder sb = new StringBuilder();

        for (SolicitacaoAdocao solicitacao : solicitacoes) {
            String adotante = solicitacao.getAdotante() == null ? "Nao carregado" : solicitacao.getAdotante().getNome();
            String cidade = solicitacao.getAdotante() == null || solicitacao.getAdotante().getEndereco() == null
                    ? "Nao carregada"
                    : solicitacao.getAdotante().getEndereco().getCidade();
            String pet = solicitacao.getPet() == null ? "Nao carregado" : solicitacao.getPet().getNome();

            sb.append("ID: ").append(solicitacao.getId())
                    .append("\nAdotante: ").append(adotante)
                    .append("\nCidade do adotante: ").append(cidade)
                    .append("\nPet: ").append(pet)
                    .append("\nData: ").append(solicitacao.getDataSolicitacao())
                    .append("\nStatus: ").append(solicitacao.getStatus())
                    .append("\nJustificativa: ").append(solicitacao.getJustificativa())
                    .append("\n----------------------\n");
        }

        JOptionPane.showMessageDialog(null, sb.length() == 0 ? mensagemVazia : sb.toString());
    }

    /**
     * Cancela uma solicitação de adoção pendente.
     *
     * @param adotante Adotante que deseja cancelar a solicitação
     * @throws Exception Se houver erro no cancelamento
     */
    private static void cancelarSolicitacao(Adotante adotante) throws Exception {
        int id = lerInteiro("ID da solicitacao:");
        SolicitacaoAdocao solicitacao = solicitacaoDAO.buscarPorId(id);

        if (solicitacao == null || solicitacao.getAdotante() == null || solicitacao.getAdotante().getId() != adotante.getId()) {
            JOptionPane.showMessageDialog(null, "Solicitacao nao encontrada para este adotante.");
            return;
        }

        if (solicitacao.getStatus() != StatusSolicitacao.PENDENTE) {
            JOptionPane.showMessageDialog(null, "So e possivel cancelar solicitacoes pendentes.");
            return;
        }

        solicitacao.setStatus(StatusSolicitacao.CANCELADA_PELO_ADOTANTE);
        solicitacao.setDataResposta(java.time.LocalDateTime.now());
        solicitacao.setJustificativa("Cancelada pelo adotante");
        solicitacaoDAO.atualizar(solicitacao);

        JOptionPane.showMessageDialog(null, "Solicitacao cancelada.");
    }

    /**
     * Aprova uma solicitação de adoção pendente.
     *
     * @param abrigo Abrigo que está aprovando a solicitação
     * @throws Exception Se houver erro na aprovação
     */
    private static void aprovarSolicitacao(Abrigo abrigo) throws Exception {
        int id = lerInteiro("ID da solicitacao:");
        SolicitacaoAdocao solicitacao = solicitacaoDAO.buscarPorId(id);

        if (!podeAbrigoResponder(solicitacao, abrigo)) {
            JOptionPane.showMessageDialog(null, "Solicitacao nao encontrada para este abrigo ou nao esta pendente.");
            return;
        }

        boolean aprovada = solicitacaoDAO.aprovar(id);
        JOptionPane.showMessageDialog(null,
                aprovada ? "Solicitacao aprovada e pet marcado como adotado." : "Nao foi possivel aprovar a solicitacao.");
    }

    /**
     * Recusa uma solicitação de adoção pendente.
     *
     * @param abrigo Abrigo que está recusando a solicitação
     * @throws Exception Se houver erro na recusa
     */
    private static void recusarSolicitacao(Abrigo abrigo) throws Exception {
        int id = lerInteiro("ID da solicitacao:");
        SolicitacaoAdocao solicitacao = solicitacaoDAO.buscarPorId(id);

        if (!podeAbrigoResponder(solicitacao, abrigo)) {
            JOptionPane.showMessageDialog(null, "Solicitacao nao encontrada para este abrigo ou nao esta pendente.");
            return;
        }

        String justificativa = lerTexto("Justificativa da recusa:");
        boolean recusada = solicitacaoDAO.recusar(id, justificativa);
        JOptionPane.showMessageDialog(null,
                recusada ? "Solicitacao recusada." : "Nao foi possivel recusar a solicitacao.");
    }

    /**
     * Remove um pet do sistema.
     *
     * @param abrigo Abrigo dono do pet
     * @throws Exception Se houver erro na remoção
     */
    private static void removerPet(Abrigo abrigo) throws Exception {
        int petId = lerInteiro("ID do pet:");
        Pet pet = petDAO.buscarPorId(petId);

        if (pet == null || pet.getAbrigo() == null || pet.getAbrigo().getId() != abrigo.getId()) {
            JOptionPane.showMessageDialog(null, "Pet nao encontrado para este abrigo.");
            return;
        }

        if (solicitacaoDAO.petPossuiSolicitacaoPendente(petId)) {
            JOptionPane.showMessageDialog(null, "Nao e possivel remover pet com solicitacao pendente.");
            return;
        }

        petDAO.excluir(petId);
        JOptionPane.showMessageDialog(null, "Pet removido.");
    }

    /**
     * Verifica se um abrigo pode responder a uma solicitação.
     *
     * @param solicitacao Solicitação a ser verificada
     * @param abrigo Abrigo que deseja responder
     * @return true se o abrigo pode responder, false caso contrário
     */
    private static boolean podeAbrigoResponder(SolicitacaoAdocao solicitacao, Abrigo abrigo) {
        return solicitacao != null
                && solicitacao.getStatus() == StatusSolicitacao.PENDENTE
                && pertenceAoAbrigo(solicitacao, abrigo);
    }

    /**
     * Verifica se uma solicitação pertence a um abrigo específico.
     *
     * @param solicitacao Solicitação a ser verificada
     * @param abrigo Abrigo a ser verificado
     * @return true se a solicitação pertence ao abrigo, false caso contrário
     */
    private static boolean pertenceAoAbrigo(SolicitacaoAdocao solicitacao, Abrigo abrigo) {
        return solicitacao != null
                && solicitacao.getPet() != null
                && solicitacao.getPet().getAbrigo() != null
                && solicitacao.getPet().getAbrigo().getId() == abrigo.getId();
    }

    /**
     * Lê um texto do usuário via JOptionPane.
     *
     * @param mensagem Mensagem a ser exibida
     * @return Texto digitado pelo usuário
     * @throws IllegalArgumentException Se o campo estiver vazio
     */
    private static String lerTexto(String mensagem) {
        String valor = JOptionPane.showInputDialog(mensagem);
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("Campo obrigatorio nao preenchido.");
        }
        return valor.trim();
    }

    /**
     * Cria um painel de formulário com pares de label e componente.
     *
     * @param campos Pares de (label, componente) em sequência
     * @return JPanel com o formulário
     */
    private static JPanel criarFormulario(Object... campos) {
        JPanel painel = new JPanel(new GridLayout(campos.length / 2, 2, 8, 6));

        for (int i = 0; i < campos.length; i += 2) {
            painel.add(new JLabel(String.valueOf(campos[i])));
            painel.add((java.awt.Component) campos[i + 1]);
        }

        return painel;
    }

    /**
     * Exibe um formulário em uma janela de diálogo e aguarda confirmação.
     *
     * @param titulo Título da janela
     * @param painel Painel do formulário
     * @return true se o usuário clicou em OK, false se cancelou
     */
    private static boolean confirmarFormulario(String titulo, JPanel painel) {
        return JOptionPane.showConfirmDialog(null, painel, titulo, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE)
                == JOptionPane.OK_OPTION;
    }

    /**
     * Obtém o texto de um JTextField e valida se não está vazio.
     *
     * @param campo JTextField a ser lido
     * @param nomeCampo Nome do campo para mensagem de erro
     * @return Texto do campo
     * @throws IllegalArgumentException Se o campo estiver vazio
     */
    private static String textoObrigatorio(JTextField campo, String nomeCampo) {
        String valor = campo.getText().trim();
        if (valor.isEmpty()) {
            throw new IllegalArgumentException("Campo obrigatorio nao preenchido: " + nomeCampo);
        }
        return valor;
    }

    /**
     * Obtém a senha de um JPasswordField e valida se não está vazia.
     *
     * @param campo JPasswordField a ser lido
     * @param nomeCampo Nome do campo para mensagem de erro
     * @return Senha digitada
     * @throws IllegalArgumentException Se o campo estiver vazio
     */
    private static String textoObrigatorio(JPasswordField campo, String nomeCampo) {
        String valor = new String(campo.getPassword()).trim();
        if (valor.isEmpty()) {
            throw new IllegalArgumentException("Campo obrigatorio nao preenchido: " + nomeCampo);
        }
        return valor;
    }

    /**
     * Obtém e valida a UF (estado) de um JTextField.
     *
     * @param campo JTextField com a UF
     * @return UF em maiúsculas
     * @throws IllegalArgumentException Se a UF não tiver 2 caracteres
     */
    private static String textoUF(JTextField campo) {
        return validarUF(textoObrigatorio(campo, "Estado"));
    }

    /**
     * Lê e valida a UF (estado) do usuário.
     *
     * @param mensagem Mensagem a ser exibida
     * @return UF em maiúsculas
     * @throws IllegalArgumentException Se a UF não tiver 2 caracteres
     */
    private static String lerUF(String mensagem) {
        return validarUF(lerTexto(mensagem));
    }

    /**
     * Valida se uma string é uma UF válida (2 letras).
     *
     * @param valor String a ser validada
     * @return UF em maiúsculas
     * @throws IllegalArgumentException Se a UF não tiver 2 caracteres
     */
    private static String validarUF(String valor) {
        String uf = valor.trim().toUpperCase();
        if (uf.length() != 2) {
            throw new IllegalArgumentException("Estado deve ser a sigla com 2 letras. Exemplo: SP, RJ, MG.");
        }
        return uf;
    }

    /**
     * Salva um endereço no banco de dados.
     *
     * @param endereco Endereço a ser salvo
     * @throws IllegalStateException Se o endereço não for salvo
     */
    private static void salvarEndereco(Endereco endereco) {
        enderecoDAO.inserir(endereco);
        if (endereco.getId() <= 0) {
            throw new IllegalStateException("Endereco nao foi salvo. Verifique os dados informados antes de cadastrar usuario.");
        }
    }

    /**
     * Lê um número inteiro do usuário.
     *
     * @param mensagem Mensagem a ser exibida
     * @return Número inteiro digitado
     * @throws NumberFormatException Se o valor não for um número válido
     */
    private static int lerInteiro(String mensagem) {
        return Integer.parseInt(lerTexto(mensagem));
    }
}