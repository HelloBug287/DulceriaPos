package com.dulceria.pos.DAO;

import com.dulceria.pos.modelo.Rol;
import com.dulceria.pos.util.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RolDAO {

    public List<Rol> listarRoles(){
        List<Rol> lista = new ArrayList<>();
        String SQL = "SELECT * FROM Roles;";
        try(Connection con = Conexion.getConnection();
            PreparedStatement pstm = con.prepareStatement(SQL);
            ResultSet rs = pstm.executeQuery()){
            while (rs.next()){
                int idRol = rs.getInt("id_rol");
                String nombreRol = rs.getString("nombre_rol");
                Rol r = new Rol(idRol,nombreRol);
                lista.add(r);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return lista;
    }

    public Rol crearRol(String nombreRol){
        String SQL = "INSERT INTO Roles(nombre_rol) VALUES(?);";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQL,PreparedStatement.RETURN_GENERATED_KEYS)){
            pstm.setString(1,nombreRol);
            int filas = pstm.executeUpdate();
            if (filas > 0){
                ResultSet keys = pstm.getGeneratedKeys();
                if (keys.next()){
                    int idRol = keys.getInt(1);
                    return new Rol(idRol,nombreRol);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean actualizarRoles(Rol rol){
        String SQL = "UPDATE Roles " +
                "SET nombre_rol = ? " +
                "WHERE id_rol = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQL)){
            pstm.setString(1,rol.getNombreRol());
            pstm.setInt(2,rol.getIdRol());
            int filas =pstm.executeUpdate();
            if (filas > 0){
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //public
}
