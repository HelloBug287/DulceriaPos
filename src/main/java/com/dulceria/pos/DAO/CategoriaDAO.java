package com.dulceria.pos.DAO;
import com.dulceria.pos.modelo.Categoria;
import com.dulceria.pos.util.Conexion;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public List<Categoria> listarCategorias(){
        List<Categoria> lista = new ArrayList<>();
        String SQL = "SELECT * FROM Categorias;";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQL);
             ResultSet rs = pstm.executeQuery()){
            while (rs.next()){
                int idCategoria = rs.getInt("id_categoria");
                String nombreCategoria = rs.getString("nombre_categoria");
                Categoria cat = new Categoria(idCategoria,nombreCategoria);
                lista.add(cat);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return lista;
    }

    public Categoria crearCategoria(String nombreCategoria){
        String SQL = "INSERT INTO Categorias (nombre_categoria) VALUES(?); ";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)){
             pstm.setString(1,nombreCategoria);
            int filas = pstm.executeUpdate();
            if (filas > 0){
                ResultSet keys = pstm.getGeneratedKeys();
                if (keys.next()){
                    int idCategoria = keys.getInt(1);
                    return new Categoria(idCategoria,nombreCategoria);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean actualizarCategoria(Categoria cat){
        String SQL = "UPDATE Categorias " +
                "SET nombre_categoria = ? " +
                "WHERE id_categoria = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQL)){
            pstm.setString(1,cat.getNombreCategoria());
            pstm.setInt(2,cat.getIdCategoria());
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
