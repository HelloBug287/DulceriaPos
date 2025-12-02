package com.dulceria.pos.controlador;

// Daos y modelos necesarios para manejar el controlador
import com.dulceria.pos.DAO.ProductoDAO;
import com.dulceria.pos.DAO.CategoriaDAO;
import com.dulceria.pos.DAO.MarcaDAO;
import com.dulceria.pos.modelo.Categoria;
import com.dulceria.pos.modelo.Producto;
import com.dulceria.pos.modelo.Marca;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import java.util.List;


public class ProductosController {

    //Inyeccion de componentes FXML

    // Componentes del formulario
    private @FXML TextField txtNombre;
    private @FXML ComboBox<String> comboCategoria;
    private @FXML ComboBox<String> comboMarca;
    private @FXML TextField txtStock;
    private @FXML ComboBox<String> comboUnidad;
    private @FXML TextField txtPrecio;
    private @FXML CheckBox checkActivo;
    private @FXML Button btnGuardar;
    private @FXML Button btnLimpiar;

    //componentes de la tabla
    private @FXML TableView<Producto> tablaProductos;
    private @FXML TableColumn<Producto, Integer> colId;
    private @FXML TableColumn<Producto, String> colNombre;
    private @FXML TableColumn<Producto, String> colCategoria;
    private @FXML TableColumn<Producto, String> colMarca;
    private @FXML TableColumn<Producto, Double> colStock;
    private @FXML TableColumn<Producto, String> colUnidad;
    private @FXML TableColumn<Producto, Double> colPrecio;
    private @FXML TableColumn<Producto, Boolean> colActivo;

    //Componentes de busqueda
    private @FXML TextField txtBuscar;
    private @FXML Button btnBuscar;
    private @FXML ComboBox<String> comboFiltrarCategoria;

}
