package com.dulceria.pos.DAO;

import com.dulceria.pos.modelo.Rol;
import com.dulceria.pos.util.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla `Roles`.
 * Uso: principalmente para llenar el combo de roles en el modulo de Usuarios.
 */
public class RolDAO {

    /**
     * Lista todos los roles.
     */
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

}
