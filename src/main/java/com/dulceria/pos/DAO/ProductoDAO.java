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
    public Producto crearProducto(int idCategoria, int idMarca, String nombreProducto, double precio, double stock, String unidadMedida, boolean activo){
        String SQL = "INSERT INTO Productos(id_categoria, id_marca, nombre_producto, precio, stock, unidad_medida, activo) VALUES(?,?,?,?,?,?,?)"; //sentencia sql, no pedimos el id ya que la BD lo genera al ser auto incrementable
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)){  // le decimos a nuestro prepared statemen que cuando se ejecute la sentencia SQL nos devuelva las claves (id_producto)
            // preparamos el statement para la insercion de los datos
            pstm.setInt(1, idCategoria);
            pstm.setInt(2, idMarca);
            pstm.setString(3, nombreProducto);
            pstm.setDouble(4,precio);
            pstm.setDouble(5, stock);
            pstm.setString(6, unidadMedida);
            pstm.setBoolean(7,activo);

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
                    return new Producto(id_producto, idCategoria, idMarca, nombreProducto, precio, stock, unidadMedida, activo);
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
                int idProducto= rs.getInt("id_producto");
                int idCategoria = rs.getInt("id_categoria");
                int idMarca = rs.getInt("id_marca");
                String nombreProducto = rs.getString("nombre_producto");
                double precio = rs.getDouble("precio");
                double stock = rs.getDouble("stock");
                String unidadMedida = rs.getString("unidad_medida");
                boolean activo = rs.getBoolean("activo");
                String nombreCategoria = rs.getString("nombre_categoria");
                String nombreMarca = rs.getString("nombre_marca");
                Producto p = new Producto(idProducto, idCategoria, idMarca, nombreProducto, precio, stock, unidadMedida, activo);
                p.setNombreCategoria(nombreCategoria);
                p.setNombreMarca(nombreMarca);
                lista.add(p);

            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return lista;
    }

    public Producto buscarPorId(int id_producto){
        String SQL = "SELECT * FROM vista_productos WHERE id_producto = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQL)) {
            pstm.setInt(1,id_producto);
            try (ResultSet rs = pstm.executeQuery()){
                if (rs.next()){
                    int idCategoria = rs.getInt("id_categoria");
                    int idMarca = rs.getInt("id_marca");
                    String nombreProducto = rs.getString("nombre_producto");
                    double precio = rs.getDouble("precio");
                    double stock = rs.getDouble("stock");
                    String unidadMedida = rs.getString("unidad_medida");
                    boolean activo = rs.getBoolean("activo");
                    return new Producto(id_producto, idCategoria, idMarca, nombreProducto,precio , stock, unidadMedida, activo);
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
        String SQL = "SELECT * FROM vista_productos WHERE nombre_producto LIKE ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQL)){
            pstm.setString(1,"%" + nombre_producto + "%");
            try ( ResultSet rs = pstm.executeQuery();){
                while (rs.next()){
                    int idProducto = rs.getInt("id_producto");
                    int idCategoria = rs.getInt("id_categoria");
                    int idMarca = rs.getInt("id_marca");
                    String nombreProducto = rs.getString("nombre_producto");
                    double precio = rs.getDouble("precio");
                    double stock = rs.getDouble("stock");
                    String unidadMedida = rs.getString("unidad_medida");
                    boolean activo = rs.getBoolean("activo");
                    Producto p = new Producto(idProducto,idCategoria, idMarca,nombreProducto,precio , stock, unidadMedida,activo );
                    listaProductos.add(p);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return listaProductos;
    }
    public boolean actualizarProducto(Producto producto){
        String SQL = "UPDATE Productos " +
                "SET id_categoria=?," +
                " id_marca=?," +
                " nombre_producto=?," +
                " precio=?," +
                " stock=?," +
                " unidad_medida=?," +
                " activo=? " +
                "WHERE id_producto = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQL)){
            pstm.setInt(1,producto.getIdCategoria());
            pstm.setInt(2,producto.getIdMarca());
            pstm.setString(3,producto.getNombreProducto());
            pstm.setDouble(4,producto.getPrecio());
            pstm.setDouble(5,producto.getStock());
            pstm.setString(6,producto.getUnidadMedida());
            pstm.setBoolean(7,producto.isActivo());
            pstm.setInt(8,producto.getIdProducto());
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


