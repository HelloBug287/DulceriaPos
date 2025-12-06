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
      // En caso de excepción devolvemos lista vacía en lugar de null para evitar NPE en consumidores
      return lista;
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
     * Lista ventas realizadas por un usuario específico
     */
    public List<Venta> listarVentasPorUsuario(int idUsuario) {
        List<Venta> lista = new ArrayList<>();
        String SQL = "SELECT * FROM Ventas WHERE id_usuario = ? ORDER BY fecha_hora DESC";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQL)) {

            pstm.setInt(1, idUsuario);

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    int idVenta = rs.getInt("id_venta");
                    Date fechaHora = rs.getTimestamp("fecha_hora");
                    String metodoPago = rs.getString("metodo_pago");
                    double subtotal = rs.getDouble("subtotal");
                    double impuestos = rs.getDouble("impuestos");
                    double total = rs.getDouble("total");

                    Venta v = new Venta(idVenta, idUsuario, fechaHora, metodoPago, subtotal, impuestos, total);
                    lista.add(v);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * obtenerVentasPorCategoria
     * -------------------------
     * Antes: consulta JOIN directo filtrando por CURDATE()
     * Ahora: usa la vista `v_ventas_por_categoria_hoy` para simplificar y acelerar la consulta.
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
     * listarResumenVentasPorDia
     * -------------------------
     * Lee la vista `v_ventas_resumen_dia` que devuelve dia, num_ventas y total_ventas.
     */
    public List<Object[]> listarResumenVentasPorDia() {
        List<Object[]> resultado = new ArrayList<>();
        String SQL = "SELECT dia, num_ventas, total_ventas FROM v_ventas_resumen_dia ORDER BY dia DESC";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQL);
             ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                java.sql.Date dia = rs.getDate("dia");
                int numVentas = rs.getInt("num_ventas");
                double total = rs.getDouble("total_ventas");
                resultado.add(new Object[]{dia, numVentas, total});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    /**
     * listarDetalleVentaView
     * ----------------------
     * Obtiene los detalles de una venta usando la vista `v_venta_detalle`.
     * Mapea los resultados al modelo DetalleVenta.
     */
    public List<DetalleVenta> listarDetalleVentaView(int idVenta) {
        List<DetalleVenta> lista = new ArrayList<>();
        String SQL = "SELECT id_detalle_venta, id_venta, id_producto, cantidad, precio_unitario_cobrado, importe_total " +
                "FROM v_venta_detalle WHERE id_venta = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQL)) {

            pstm.setInt(1, idVenta);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    int idDetalle = rs.getInt("id_detalle_venta");
                    int idProd = rs.getInt("id_producto");
                    double cantidad = rs.getDouble("cantidad");
                    double precioUnitario = rs.getDouble("precio_unitario_cobrado");
                    double importe = rs.getDouble("importe_total");

                    DetalleVenta detalle = new DetalleVenta(idDetalle, idVenta, idProd, cantidad, precioUnitario, importe);
                    lista.add(detalle);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Crear venta usando el procedimiento almacenado sp_crear_venta_json.
     * itemsJson formato: [{"idProducto":1,"cantidad":2.0,"precio":10.5}, ...]
     * Retorna la venta creada con id (o null en caso de error)
     */
    public Venta crearVentaConProcedure(int idUsuario, String metodoPago, double subtotal, double impuestos, double total, String itemsJson) {
        String sql = "CALL sp_crear_venta_json(?,?,?,?,?,?,?)";
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
        }
        return null;
    }

}
