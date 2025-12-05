package com.dulceria.pos.DAO;

import com.dulceria.pos.modelo.DetalleVenta;
import com.dulceria.pos.util.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetalleVentaDAO {

    /**
     * Este metodo se llama por cada producto que está en el carrito al momento de cobrar
     */
    public DetalleVenta crearDetalleVenta(int idVenta, int idProducto, double cantidad,
                                          double precioUnitarioCobrado, double importeTotal) {
        String SQL = "INSERT INTO Detalle_Ventas(id_venta, id_producto, cantidad, precio_unitario_cobrado, importe_total) " +
                "VALUES(?,?,?,?,?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pstm.setInt(1, idVenta);
            pstm.setInt(2, idProducto);
            pstm.setDouble(3, cantidad);
            pstm.setDouble(4, precioUnitarioCobrado);
            pstm.setDouble(5, importeTotal);

            int filas = pstm.executeUpdate();
            if (filas > 0) {
                ResultSet keys = pstm.getGeneratedKeys();
                if (keys.next()) {
                    int idDetalle = keys.getInt(1);
                    return new DetalleVenta(idDetalle, idVenta, idProducto, cantidad,
                            precioUnitarioCobrado, importeTotal);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lista todos los detalles (productos) de una venta específica
     * Útil para mostrar el ticket completo o para reportes
     */
    public List<DetalleVenta> listarDetallesPorVenta(int idVenta) {
        List<DetalleVenta> lista = new ArrayList<>();
        String SQL = "SELECT * FROM Detalle_Ventas WHERE id_venta = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQL)) {

            pstm.setInt(1, idVenta);

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    int idDetalle = rs.getInt("id_detalle_venta");
                    int idProducto = rs.getInt("id_producto");
                    double cantidad = rs.getDouble("cantidad");
                    double precioUnitario = rs.getDouble("precio_unitario_cobrado");
                    double importeTotal = rs.getDouble("importe_total");

                    DetalleVenta detalle = new DetalleVenta(idDetalle, idVenta, idProducto,
                            cantidad, precioUnitario, importeTotal);
                    lista.add(detalle);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}