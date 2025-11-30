package com.dulceria.pos.util;
 //Importamos las librerias necesarias para establecer conexion con SQL
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Conexion {

    //Credenciales e ip de conexion
    private final static String url = "jdbc:mariadb://192.168.56.101/dulceria";
    private final static String user = "vic";
    private final static String password = "vic123";

    public static Connection getConnection() throws SQLException{

        return DriverManager.getConnection(url,user,password);
    }
}
