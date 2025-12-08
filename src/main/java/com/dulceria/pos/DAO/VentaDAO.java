package com.dulceria.pos.DAO;

import com.dulceria.pos.modelo.Venta;
import com.dulceria.pos.modelo.DetalleVenta;
import com.dulceria.pos.util.Conexion;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


  //DAO responsable de las operaciones sobre la entidad `Ventas` y operaciones relacionadas

public class VentaDAO {
    // Campo para almacenar el último error SQL (mensaje) y poder consultarlo desde el controlador
    private String lastError = null;

    public String getLastError() { return lastError; }

  //util para generar reportes
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
        // Devolver lista vacía en caso de error
        return lista;
    }

    public double obtenerTotalVentasHoy() {
        String SQL = "SELECT COALESCE(SUM(total), 0) AS total_dia " +
                "FROM Ventas " +
                "WHERE DATE(fecha_hora) = CURDATE()";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQL);
             ResultSet rs = pstm.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("total_dia");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    /**
     * Cuenta las ventas realizadas el día de hoy
     */
    public int contarVentasHoy() {
        String SQL = "SELECT COUNT(*) AS cantidad " +
                "FROM Ventas " +
                "WHERE DATE(fecha_hora) = CURDATE()";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQL);
             ResultSet rs = pstm.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("cantidad");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Obtiene la última venta registrada (para generar siguiente folio)
     */
    public Venta obtenerUltimaVenta() {
        String SQL = "SELECT * FROM Ventas ORDER BY id_venta DESC LIMIT 1";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQL);
             ResultSet rs = pstm.executeQuery()) {

            if (rs.next()) {
                int idVenta = rs.getInt("id_venta");
                int idUsuario = rs.getInt("id_usuario");
                Date fechaHora = rs.getTimestamp("fecha_hora");
                String metodoPago = rs.getString("metodo_pago");
                double subtotal = rs.getDouble("subtotal");
                double impuestos = rs.getDouble("impuestos");
                double total = rs.getDouble("total");

                return new Venta(idVenta, idUsuario, fechaHora, metodoPago, subtotal, impuestos, total);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * obtenerVentasPorCategoria
     * usa la vista v_ventas_por_categoria_hoy para simplificar y acelerar la consulta.
     */
    public List<Object[]> obtenerVentasPorCategoria() {
        List<Object[]> resultado = new ArrayList<>();
        String SQL = "SELECT nombre_categoria, total_ventas FROM v_ventas_por_categoria_hoy";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQL);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                String categoria = rs.getString("nombre_categoria");
                double total = rs.getDouble("total_ventas");
                resultado.add(new Object[]{categoria, total});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    /**
     * Crear venta usando el procedimiento almacenado sp_crear_venta_json.
     */
    public Venta crearVentaConProcedure(int idUsuario, String metodoPago, double subtotal, double impuestos, double total, String itemsJson) {
        String sql = "CALL sp_crear_venta_json(?,?,?,?,?,?,?)";
        // resetear último error
        lastError = null;
        try (Connection con = Conexion.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt(1, idUsuario);
            cs.setString(2, metodoPago);
            cs.setDouble(3, subtotal);
            cs.setDouble(4, impuestos);
            cs.setDouble(5, total);
            cs.setString(6, itemsJson);
            cs.registerOutParameter(7, Types.INTEGER);

            cs.execute();
            int idVenta = cs.getInt(7);
            return new Venta(idVenta, idUsuario, metodoPago, subtotal, impuestos, total);
        } catch (SQLException e) {
            e.printStackTrace();
            lastError = e.getMessage() + " (SQLState=" + e.getSQLState() + ", ErrorCode=" + e.getErrorCode() + ")";
        }
        return null;
    }


    public Venta crearVentaTransaccional(int idUsuario, String metodoPago, double subtotal, double impuestos, double total, List<DetalleVenta> detalles) {
        lastError = null;
        String sqlInsertVenta = "INSERT INTO Ventas(id_usuario, metodo_pago, subtotal, impuestos, total) VALUES(?,?,?,?,?)";
        String sqlInsertDetalle = "INSERT INTO Detalle_Ventas(id_venta, id_producto, cantidad, precio_unitario_cobrado, importe_total) VALUES(?,?,?,?,?)";

        try (Connection con = Conexion.getConnection()) {
            try {
                con.setAutoCommit(false);

                // Insert venta
                int idVenta;
                try (PreparedStatement pVenta = con.prepareStatement(sqlInsertVenta, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    pVenta.setInt(1, idUsuario);
                    pVenta.setString(2, metodoPago);
                    pVenta.setDouble(3, subtotal);
                    pVenta.setDouble(4, impuestos);
                    pVenta.setDouble(5, total);
                    int filas = pVenta.executeUpdate();
                    if (filas == 0) throw new SQLException("No se pudo insertar la venta");
                    try (ResultSet keys = pVenta.getGeneratedKeys()) {
                        if (keys.next()) {
                            idVenta = keys.getInt(1);
                        } else {
                            throw new SQLException("No se obtuvo id de la venta");
                        }
                    }
                }

                // Insert detalles (triggers en BD manejarán stock)
                try (PreparedStatement pDetalle = con.prepareStatement(sqlInsertDetalle)) {
                    for (DetalleVenta d : detalles) {
                        double importe = Math.round(d.getCantidad() * d.getPrecioUnitarioCobrado() * 100.0) / 100.0;
                        pDetalle.setInt(1, idVenta);
                        pDetalle.setInt(2, d.getIdProducto());
                        pDetalle.setDouble(3, d.getCantidad());
                        pDetalle.setDouble(4, d.getPrecioUnitarioCobrado());
                        pDetalle.setDouble(5, importe);
                        pDetalle.addBatch();
                    }
                    pDetalle.executeBatch();
                }

                con.commit();
                return new Venta(idVenta, idUsuario, metodoPago, subtotal, impuestos, total);

            } catch (SQLException ex) {
                con.rollback();
                ex.printStackTrace();
                lastError = ex.getMessage() + " (SQLState=" + ex.getSQLState() + ", ErrorCode=" + ex.getErrorCode() + ")";
                return null;
            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            lastError = e.getMessage();
            return null;
        }
    }

}
