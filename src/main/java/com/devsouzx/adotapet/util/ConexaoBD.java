package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoBD {
    private static final String URL = "jdbc:mysql://localhost:3306/adotapet";
    private static final String USUARIO = "root";
    private static final String SENHA = "";

    private static Connection conexao = null;

    public static Connection getConexao() {
        if (conexao == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
                System.out.println("Conectado ao banco de dados!");
            } catch (ClassNotFoundException e) {
                System.out.println("Driver MySQL não encontrado!");
                e.printStackTrace();
            } catch (SQLException e) {
                System.out.println("Erro ao conectar ao banco: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return conexao;
    }

    public static void fecharConexao() {
        if (conexao != null) {
            try {
                conexao.close();
                conexao = null;
                System.out.println("Conexão fechada.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void fecharStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void fecharResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void fecharPreparedStatement(PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void testarConexao() {
        try {
            Connection conn = getConexao();
            if (conn != null && !conn.isClosed()) {
                System.out.println("Banco: " + conn.getCatalog());
                System.out.println("Status: Conectado!");
            }
        } catch (SQLException e) {
            System.out.println("Falha na conexão!");
            e.printStackTrace();
        }
    }
}