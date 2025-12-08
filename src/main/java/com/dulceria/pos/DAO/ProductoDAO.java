package com.dulceria.pos.DAO;

import com.dulceria.pos.modelo.Producto;
import com.dulceria.pos.util.Conexion;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsable de las operaciones CRUD sobre la entidad `Productos`.
 */
public class ProductoDAO {

    public Producto crearProducto(int idCategoria, int idMarca, String nombreProducto,
                                  double precio, double stock, String unidadMedida, boolean activo){
        String SQL = "INSERT INTO Productos(id_categoria, id_marca, nombre_producto, precio, stock, unidad_medida, activo) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)){
            pstm.setInt(1, idCategoria);
            pstm.setInt(2, idMarca);
            pstm.setString(3, nombreProducto);
            pstm.setDouble(4,precio);
            pstm.setDouble(5, stock);
            pstm.setString(6, unidadMedida);
            pstm.setBoolean(7,activo);

            int filas = pstm.executeUpdate();
            if (filas > 0){
                ResultSet keys = pstm.getGeneratedKeys();
                if (keys.next()){
                    int id_producto = keys.getInt(1);
                    return new Producto(id_producto, idCategoria, idMarca, nombreProducto, precio, stock, unidadMedida, activo);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }


    // Muestra todos los productos incluso de categorías inactivas

    public List<Producto> listarProductos(){
        List<Producto> lista = new ArrayList<>();

        // este metodo es util para mostrar todos los productos incluso si estan inactivos
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


    // Solo muestra productos activos Y de categorías activas
    public List<Producto> listarProductosActivos(){
        List<Producto> lista = new ArrayList<>();

        // este es util para mostrar los productos solo para el cajero, es decir para el modulo de ventas
        String SQL = "SELECT * FROM vista_productos " +
                "WHERE activo = 1 " +
                "AND id_categoria IN (SELECT id_categoria FROM Categorias WHERE activo = 1)";

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


    // Búsqueda por nombre
    // Acepta parámetro para filtrar o no por categorías activas
    public List<Producto> buscarPorNombre(String nombre_producto){
        return buscarPorNombre(nombre_producto, false);
    }

    /**
     * Busca productos por nombre con opción de filtrar categorías activas
     */
    public List<Producto> buscarPorNombre(String nombre_producto, boolean soloCategoriasActivas){
        List <Producto> listaProductos = new ArrayList<>();

        String SQL;
        if (soloCategoriasActivas) {
            // Para ventas filtrar categorías inactivas
            SQL = "SELECT * FROM vista_productos " +
                    "WHERE nombre_producto LIKE ? " +
                    "AND activo = 1 " +
                    "AND id_categoria IN (SELECT id_categoria FROM Categorias WHERE activo = 1)";
        } else {
            // Para productos mostrar todos
            SQL = "SELECT * FROM vista_productos " +
                    "WHERE nombre_producto LIKE ?";
        }

        try (Connection con = Conexion.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQL)){
            pstm.setString(1,nombre_producto + "%");
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
                    String nombreCategoria = rs.getString("nombre_categoria");
                    String nombreMarca = rs.getString("nombre_marca");
                    Producto p = new Producto(idProducto,idCategoria, idMarca,nombreProducto,precio , stock, unidadMedida,activo );
                    p.setNombreCategoria(nombreCategoria);
                    p.setNombreMarca(nombreMarca);
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