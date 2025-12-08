package com.dulceria.pos.DAO;

import com.dulceria.pos.modelo.Proveedor;
import com.dulceria.pos.util.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * DAO para la tabla `Proveedores`.
 * Su uso principal es en el menú de catálogos (gestión de proveedores).
 */
public class ProveedorDAO {

    /*
     * Lista todos los proveedores.
     */
    public List<Proveedor> listarProveedores(){
        List<Proveedor> lista = new ArrayList<>();
        String SQL = "Select * From Proveedores;";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQL);
             ResultSet rs = pstm.executeQuery()){
            while (rs.next()){
                int idProveedor = rs.getInt("id_proveedor");
                String nombreProveedor = rs.getString("nombre_proveedor");
                String telefono = rs.getString("telefono");
                String email = rs.getString("email");
                boolean activo = rs.getBoolean("activo");
                Proveedor p = new Proveedor(idProveedor,nombreProveedor,telefono,email,activo);
                lista.add(p);
            }
            return lista;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        // En caso de error devolvemos lista vacía
        return lista;
    }

    /**
     * Crear un nuevo proveedor. Retorna el proveedor creado con id (o null si falla).
     */
    public Proveedor crearProveedor(String nombreProveedor, String telefono, String email, boolean activo){
        String SQL = "INSERT INTO Proveedores(nombre_proveedor, telefono, email, activo) VALUES(?,?,?,?);";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQL,PreparedStatement.RETURN_GENERATED_KEYS)){
            pstm.setString(1,nombreProveedor);
            pstm.setString(2,telefono);
            pstm.setString(3,email);
            pstm.setBoolean(4,activo);
            int filas = pstm.executeUpdate();
            if (filas > 0){
                ResultSet keys = pstm.getGeneratedKeys();
                if (keys.next()){
                    int idProveedor = keys.getInt(1);
                    return new Proveedor(idProveedor,nombreProveedor,telefono,email,activo);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Actualiza un proveedor existente. Retorna true si la actualización tuvo éxito.
     */
    public boolean actualizarProveedor(Proveedor p){
        String SQL = "UPDATE Proveedores " +
                "SET nombre_proveedor =?," +
                "telefono =?," +
                "email =?," +
                "activo =? " +
                "WHERE id_proveedor =?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm  = con.prepareStatement(SQL)){
            pstm.setString(1,p.getNombreProveedor());
            pstm.setString(2,p.getTelefono());
            pstm.setString(3,p.getEmail());
            pstm.setBoolean(4,p.isActivo());
            pstm.setInt(5,p.getIdProveedor());
            int fila = pstm.executeUpdate();
            if (fila > 0){
                return true;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}