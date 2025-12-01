package com.dulceria.pos.DAO;
import com.dulceria.pos.modelo.Venta;
import com.dulceria.pos.util.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

//    // Listar todas las ventas (para reportes)
//    public List<Venta> listarVentas()
//
//    // Buscar venta por ID (para ver detalle del ticket)
//    public Venta buscarPorId(int idVenta)
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
