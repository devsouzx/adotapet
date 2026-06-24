package com.devsouzx.adotapet.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe utilitária para gerenciar a conexão com o banco de dados MySQL.
 *
 * <p>Implementa o padrão Singleton para garantir que apenas uma conexão
 * seja mantida durante a execução da aplicação. Gerencia a abertura,
 * fechamento e validação da conexão com o banco de dados.</p>
 *
 * <p>Configurações da conexão:</p>
 * <ul>
 *   <li>URL: jdbc:mysql://localhost:3306/adotapet</li>
 *   <li>Usuário: root</li>
 *   <li>Senha: configurada na constante SENHA</li>
 * </ul>
 *
 *
 * @author Equipe Adoção de Pets
 * @version 1.0
 * @since 2026
 */
public class ConexaoBD {

    /** URL de conexão com o banco de dados MySQL */
    private static final String URL = "jdbc:mysql://localhost:3306/adotapet?useSSL=false&allowPublicKeyRetrieval=true";

    /** Usuário do banco de dados */
    private static final String USUARIO = "root";

    /** Senha do banco de dados */
    private static final String SENHA = "";

    /** Conexão única com o banco de dados (Singleton) */
    private static Connection conexao = null;

    /**
     * Bloco estático para carregar o driver JDBC do MySQL.
     * Executado quando a classe é carregada pela primeira vez.
     */
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ Driver MySQL carregado!");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Driver MySQL não encontrado!");
        }
    }

    /**
     * Obtém a conexão com o banco de dados.
     * Se a conexão for nula ou estiver fechada, uma nova conexão é criada.
     *
     * @return Conexão ativa com o banco de dados, ou {@code null} em caso de erro
     */
    public static Connection getConexao() {
        try {
            if (conexao == null || conexao.isClosed()) {
                conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
                System.out.println("✅ Conectado ao banco de dados!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao conectar: " + e.getMessage());
        }
        return conexao;
    }

    /**
     * Fecha a conexão com o banco de dados se estiver aberta.
     * A conexão é definida como {@code null} após o fechamento.
     */
    public static void fecharConexao() {
        if (conexao != null) {
            try {
                conexao.close();
                conexao = null;
                System.out.println("✅ Conexão fechada.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Fecha um objeto {@link Statement} liberando seus recursos.
     *
     * @param stmt Statement a ser fechado
     */
    public static void fecharStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Fecha um objeto {@link ResultSet} liberando seus recursos.
     *
     * @param rs ResultSet a ser fechado
     */
    public static void fecharResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Fecha um objeto {@link PreparedStatement} liberando seus recursos.
     *
     * @param pstmt PreparedStatement a ser fechado
     */
    public static void fecharPreparedStatement(PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Testa a conexão com o banco de dados exibindo informações no console.
     * Exibe o nome do banco de dados e o status da conexão.
     */
    public static void testarConexao() {
        try {
            Connection conn = getConexao();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Banco: " + conn.getCatalog());
                System.out.println("✅ Status: Conectado!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Falha na conexão!");
            e.printStackTrace();
        }
    }
}