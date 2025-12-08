package com.dulceria.pos.util;
 //Importamos las librerias necesarias para establecer conexion con SQL
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexion {

    // Credenciales e IP de conexión
    private final static String url = "jdbc:mariadb://192.168.56.101/dulceria";
    private final static String user = "vic";
    private final static String password = "vic123";

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, password);

        try (Statement stmt = conn.createStatement()) {
            stmt.execute("SET time_zone = '-06:00'");
        } catch (SQLException e) {
            System.err.println("Advertencia: No se pudo configurar zona horaria");
            e.printStackTrace();
        }

        return conn;
    }

    public static boolean probarConexion() {
        try {
            Connection con = getConnection();
            if (con != null) {
                con.close(); // Cerramos la conexión
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}