package com.dulceria.pos.DAO;

import com.dulceria.pos.modelo.Marca;
import com.dulceria.pos.util.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MarcaDAO {

    public List<Marca> listarMarcas(){
        List<Marca> list = new ArrayList<>();
        String SQL = "SELECT * FROM Marcas;";
        try(Connection con = Conexion.getConnection();
            PreparedStatement pstm = con.prepareStatement(SQL);
            ResultSet rs = pstm.executeQuery()) {
            while (rs.next()){
                int idMarca = rs.getInt("id_marca");
                String nombreMarca = rs.getString("nombre_marca");
                Marca m = new Marca(idMarca,nombreMarca);
                list.add(m);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }

//    public Marca crearMarca (String nombreMarca){
//        String SQL = "INSERT INTO Marcas (nombre_marca) VALUES(?);";
//        try (Connection con = Conexion.getConnection();
//            PreparedStatement pstm = con.prepareStatement(SQL)){
//
//        }
//    }
}
