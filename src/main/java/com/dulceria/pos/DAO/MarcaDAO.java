package com.dulceria.pos.DAO;

import com.dulceria.pos.modelo.Marca;
import com.dulceria.pos.util.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla `Marcas`.
 * Funcionalidad: listar, crear y actualizar marcas.
 * Uso: utilizado en el módulo de Productos y Catálogos.
 */
public class MarcaDAO {

    /**
     * Lista todas las marcas existentes.
     */
    public List<Marca> listarMarcas(){
        List<Marca> list = new ArrayList<>();
        String SQL = "SELECT * FROM Marcas;";
        try(Connection con = Conexion.getConnection();
            PreparedStatement pstm = con.prepareStatement(SQL);
            ResultSet rs = pstm.executeQuery()) {
            while (rs.next()){
                int idMarca = rs.getInt("id_marca");
                String nombreMarca = rs.getString("nombre_marca");
                boolean activo = rs.getBoolean("activo");
                Marca m = new Marca(idMarca,nombreMarca,activo);
                list.add(m);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Crea una nueva marca. Retorna la marca creada con id o null si falla.
     */
    public Marca crearMarca (String nombreMarca,boolean activo){
        String SQL = "INSERT INTO Marcas (nombre_marca,activo) VALUES(?,?);";
        try (Connection con = Conexion.getConnection();
            PreparedStatement pstm = con.prepareStatement(SQL,PreparedStatement.RETURN_GENERATED_KEYS)){
            pstm.setString(1,nombreMarca);
            pstm.setBoolean(2,activo);
            int filas = pstm.executeUpdate();
            if (filas > 0){
                ResultSet keys = pstm.getGeneratedKeys();
                if (keys.next()){
                    int idMarca = keys.getInt(1);
                    return new Marca(idMarca,nombreMarca,activo);
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Actualiza una marca existente. Retorna true si la actualización afectó filas.
     */
    public boolean actualizarMarca(Marca marca){
        String SQL = "UPDATE Marcas " +
                "SET nombre_marca =?, " +
                "activo = ? " +
                "WHERE id_marca =?;";
        try (Connection con = Conexion.getConnection();
            PreparedStatement pstm = con.prepareStatement(SQL)){
            pstm.setString(1,marca.getNombreMarca());
            pstm.setBoolean(2,marca.isActivo());
            pstm.setInt(3,marca.getIdMarca());
            int filas = pstm.executeUpdate();
            if (filas > 0){
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
