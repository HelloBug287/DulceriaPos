package com.dulceria.pos.DAO;

import com.dulceria.pos.modelo.Producto;
import com.dulceria.pos.util.Conexion;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    //metodo para crear un nuevo producto
    public Producto crearProducto(int id_categoria, int id_marca, String nombre_producto, double stock,String unidad_medida){
        String SQL = "INSERT INTO productos(id_categoria, id_marca, nombre_producto, stock, unidad_medida) VALUES(?,?,?,?,?)"; //sentencia sql, no pedimos el id ya que la BD lo genera al ser auto incrementable
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)){  // le decimos a nuestro prepared statemen que cuando se ejecute la sentencia SQL nos devuelva las claves (id_producto)
            // preparamos el statement para la insercion de los datos
            pstm.setInt(1,id_categoria);
            pstm.setInt(2,id_marca);
            pstm.setString(3,nombre_producto);
            pstm.setDouble(4,stock);
            pstm.setString(5,unidad_medida);
            //metemos la ejecucion de la insercion en una variable
            int filas = pstm.executeUpdate();
            //verificamos si la ejecucion fue exitosa esto mediante la variable filas, si lo fue deberia de ser mayor a 0 esto por que Si el INSERT falló, executeUpdate() retorna 0
            if (filas > 0){
                //si la ejecucion fue exitosa obtenemos las claves generadas
                ResultSet keys = pstm.getGeneratedKeys();
                //leer el id_generado
                if (keys.next()){
                    int id_producto = keys.getInt(1);
                    //creamos nuestro objeto Producto
                    return new Producto(id_producto, id_categoria, id_marca, nombre_producto, stock, unidad_medida);
                }
            }
        //Manejamos la excepcion
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Producto> listarProductos(){
        List<Producto> lista = new ArrayList<>();
        String SQL = "SELECT * FROM vista_productos";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQL);
             ResultSet rs = pstm.executeQuery()) {
            while (rs.next()){
                int id_producto= rs.getInt("id_producto");
                int id_categoria = rs.getInt("id_categoria");
                int id_marca = rs.getInt("id_marca");
                String nombre_producto = rs.getString("nombre_producto");
                double stock = rs.getDouble("stock");
                String unidad_medida = rs.getString("unidad_medida");
                Producto p = new Producto(id_producto,id_categoria,id_marca,nombre_producto,stock,unidad_medida);
                lista.add(p);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return lista;
    }

    public Producto buscarPorId(int id_producto){
        String SQL = "SELECT * FROM productos WHERE id_producto = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQL)) {
            pstm.setInt(1,id_producto);
            try (ResultSet rs = pstm.executeQuery()){
                if (rs.next()){
                    int id_categoria = rs.getInt("id_categoria");
                    int id_marca = rs.getInt("id_marca");
                    String nombre_producto = rs.getString("nombre_producto");
                    double stock = rs.getDouble("stock");
                    String unidad_medida = rs.getString("unidad_medida");
                    return new Producto(id_producto,id_categoria,id_marca,nombre_producto,stock,unidad_medida);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }catch (SQLException e){
            e.printStackTrace();
    }
        return null;
    }

    public List<Producto> buscarPorNombre(String nombre_producto){
        List <Producto> listaProductos = new ArrayList<>();
        String SQL = "SELECT * FROM PRODUCTOS WHERE nombre_producto LIKE ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQL)){
            pstm.setString(1,"%" + nombre_producto + "%");
            try ( ResultSet rs = pstm.executeQuery();){
                while (rs.next()){
                    int id_producto= rs.getInt("id_producto");
                    int id_categoria = rs.getInt("id_categoria");
                    int id_marca = rs.getInt("id_marca");
                    String nombreProducto = rs.getString("nombre_producto");
                    double stock = rs.getDouble("stock");
                    String unidad_medida = rs.getString("unidad_medida");
                    Producto p = new Producto(id_producto,id_categoria,id_marca,nombreProducto,stock,unidad_medida);
                    listaProductos.add(p);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return listaProductos;
    }
    //public boolean actualizarProducto(Producto producto){
      //  String SQL = "UPDATE productos SET id_categoria=?, id_marca=?, nombre_producto=?, stock=?, "
      //  try {
    //  }
    //}
}


