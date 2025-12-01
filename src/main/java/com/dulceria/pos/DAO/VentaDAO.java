package com.dulceria.pos.DAO;
import com.dulceria.pos.modelo.Venta;
import com.dulceria.pos.util.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class VentaDAO {
    // Crear una venta (registrar ticket)
    public Venta crearVenta(int idUsuario, String metodoPago, double subtotal, double impuestos, double total){
        String SQL = "INSERT INTO Ventas(id_usuario, metodo_pago, subtotal, impuestos, total) VALUES(?,?,?,?,?); ";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQL,PreparedStatement.RETURN_GENERATED_KEYS)){
            pstm.setInt(1,idUsuario);
            pstm.setString(2,metodoPago);
            pstm.setDouble(3,subtotal);
            pstm.setDouble(4,impuestos);
            pstm.setDouble(5,total);
            int filas = pstm.executeUpdate();
            if (filas > 0){
                ResultSet keys = pstm.getGeneratedKeys();
                if (keys.next()){
                    int idVenta = keys.getInt(1);
                    return new Venta(idVenta,idUsuario,metodoPago,subtotal,impuestos,total);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

      // Listar todas las ventas (para reportes)
      public List<Venta> listarVentas(){
        List<Venta> lista = new ArrayList<>();
        String SQL = "SELECT * FROM Ventas";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQL);
             ResultSet rs = pstm.executeQuery()){
            while (rs.next()){
                int idVenta = rs.getInt("id_venta");
                int idUsuario = rs.getInt("id_usuario");
                Date fechaHora = rs.getTimestamp("fecha_hora");
                String metodoPago = rs.getString("metodo_pago");
                double subtotal = rs.getDouble("subtotal");
                double impuestos = rs.getDouble("impuestos");
                double total = rs.getDouble("total");
                Venta v = new Venta(idVenta,idUsuario,fechaHora,metodoPago,subtotal,impuestos,total);
                lista.add(v);
            }
            return lista;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    //Buscar venta por ID (para ver detalle del ticket)
    public Venta buscarPorId(int idVenta){
        String SQL = "SELECT * FROM Ventas WHERE id_venta = ?";
        try (Connection con = Conexion.getConnection();
            PreparedStatement pstm = con.prepareStatement(SQL)){
            pstm.setInt(1,idVenta);
            try (ResultSet rs = pstm.executeQuery()){
                if (rs.next()){
                    rs.getInt("id_venta");
                    int idUsuario = rs.getInt("id_usuario");
                    Date fechaHora = rs.getTimestamp("fecha_hora");
                    String metodoPago = rs.getString("metodo_pago");
                    double subtotal = rs.getDouble("subtotal");
                    double impuestos = rs.getDouble("impuestos");
                    double total = rs.getDouble("total");
                    return new Venta(idVenta,idUsuario,fechaHora,metodoPago,subtotal,impuestos,total);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;

    }

    // Listar ventas por fecha (para reportes filtrados)
    public List<Venta> listarVentasPorFecha(Date fechaInicio, Date fechaFin){
        List<Venta> lista = new ArrayList<>();
        String SQL = "SELECT * FROM Ventas WHERE DATE(fecha_hora) BETWEEN ? AND ?;";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQL)){
            pstm.setDate(1,new java.sql.Date(fechaInicio.getTime())); //En ambos hacemos un casteo para convertir java.util.Date a java.sql.Date
            pstm.setDate(2,new java.sql.Date(fechaFin.getTime()));  // En este tambien

            try (ResultSet rs = pstm.executeQuery()){
                while (rs.next()){
                    int idVenta = rs.getInt("id_venta");
                    int idUsuario = rs.getInt("id_usuario");
                    Date fechaHora = rs.getTimestamp("fecha_hora");
                    String metodoPago = rs.getString("metodo_pago");
                    double subtotal = rs.getDouble("subtotal");
                    double impuestos = rs.getDouble("impuestos");
                    double total = rs.getDouble("total");
                    Venta v = new Venta(idVenta,idUsuario,fechaHora,metodoPago,subtotal,impuestos,total);
                    lista.add(v);
                }
                return lista;

            }catch (SQLException e){
                e.printStackTrace();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    // Obtener total de ventas del día (para dashboard)
//    public double obtenerTotalVentasHoy(){
//        String SQL = "SELECT SUM(total) AS total_dia " +
//                "FROM Ventas " +
//                "WHERE DATE(fecha_hora) = ?;";
//        try (Connection con = Conexion.getConnection();
//             PreparedStatement pstm = con.prepareStatement(SQL)){
//            pstm.setDate(1,"fe");
//
//    // Listar ventas por fecha (para reportes filtrados)
//    public List<Venta> listarVentasPorFecha(Date fechaInicio, Date fechaFin)
//
//    // Obtener total de ventas del día (para dashboard)
//    public double obtenerTotalVentasHoy()
//
//    // Contar ventas del día (para dashboard)
//    public int contarVentasHoy()
//
//    // Obtener última venta (para mostrar folio siguiente)
//    public Venta obtenerUltimaVenta()
//
//    // Ventas por usuario (reportes por cajero)
//    public List<Venta> listarVentasPorUsuario(int idUsuario)

}
