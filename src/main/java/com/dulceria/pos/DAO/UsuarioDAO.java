package com.dulceria.pos.DAO;
/*
*Importamos las librerias necesarias para poder crear nuestro DAO
* entre ellas la clase conexion que nos ayudara a establecer conexion con la BD
* la clase Usuarios que es nuestro POJO que nos ayudara a crear objetos de tipo 'Usuario'
*/
import com.dulceria.pos.util.Conexion;
import com.dulceria.pos.modelo.Usuario;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones sobre la tabla `Usuarios`.
 * Responsabilidades:
 * Crear, listar y actualizar usuarios.
 * Buscar usuario por username (útil para login).
 */
public class UsuarioDAO {

    /**
     * Crea un usuario nuevo en la BD.
     */
    public Usuario crearUsuario(int idRol, String username, String password, String nombreCompleto, boolean activo){
        String SQL = "INSERT INTO Usuarios (id_rol, username, password, nombre_completo, activo) VALUES(?,?,?,?,?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQL,PreparedStatement.RETURN_GENERATED_KEYS)){
            pstm.setInt(1, idRol);
            pstm.setString(2,username);
            pstm.setString(3,password);
            pstm.setString(4, nombreCompleto);
            pstm.setBoolean(5,activo);

            int filas = pstm.executeUpdate();
            if (filas > 0){
                ResultSet keys = pstm.getGeneratedKeys();
                if (keys.next()){
                    int idUsuario = keys.getInt(1);
                    return new Usuario(idUsuario, idRol,username,password, nombreCompleto,activo);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lista todos los usuarios junto con el nombre del rol
     * Retorna lista (vacía si no hay resultados o si ocurre un error).
     */
    public List<Usuario> listarUsuarios(){
        List <Usuario> listaU = new ArrayList<>();
        String SQL = "SELECT u.*, r.nombre_rol AS 'Nombre Rol' " +
                "FROM Usuarios AS u " +
                "INNER JOIN Roles AS r ON u.id_rol = r.id_rol";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQL);
             ResultSet rs = pstm.executeQuery()){

            while (rs.next()){
                int idUsuario = rs.getInt("id_usuario");
                int idRol = rs.getInt("id_rol");
                String nombreRol = rs.getString("Nombre Rol");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String nombreCompleto = rs.getString("nombre_completo");
                boolean activo = rs.getBoolean("activo");
                Usuario u = new Usuario(idUsuario, idRol, username, password, nombreCompleto,activo);
                u.setNombreRol(nombreRol);
                listaU.add(u);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return listaU;
    }

    /**
     * Actualiza un usuario existente. Se espera que el objeto Usuario contenga el id.
     * Retorna true si la actualización afectó filas.
     */
    public boolean actualizarUsuario(Usuario u){
        String SQL = "UPDATE Usuarios " +
                "SET id_rol = ?," +
                "username = ?," +
                "password = ?," +
                "nombre_completo = ?," +
                "activo = ? " +
                "WHERE id_usuario = ?;";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQL)){
            pstm.setInt(1,u.getIdRol());
            pstm.setString(2,u.getUsername());
            pstm.setString(3,u.getPassword());
            pstm.setString(4,u.getNombreCompleto());
            pstm.setBoolean(5,u.isActivo());
            pstm.setInt(6,u.getIdUsuario());
            int filas = pstm.executeUpdate();
            if (filas > 0){
                return true;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
   }

   /**
    * Busca un usuario por su username para usarlo en la autenticacion
    * Retorna el objeto Usuario o null si no existe.
    */
   public Usuario buscarPorUsername(String username){
        String SQL = "SELECT * FROM Usuarios WHERE username = ? ;";
        try (Connection con = Conexion.getConnection();
            PreparedStatement pstm = con.prepareStatement(SQL)){
            pstm.setString(1,username);
            try ( ResultSet rs = pstm.executeQuery()){
                if (rs.next()){
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setIdRol(rs.getInt("id_rol"));
                    usuario.setUsername(rs.getString("username"));
                    usuario.setPassword(rs.getString("password"));
                    usuario.setNombreCompleto(rs.getString("nombre_completo"));
                    usuario.setActivo(rs.getBoolean("activo"));

                    return usuario;
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
   }

}