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
import javafx.scene.control.cell.PropertyValueFactory;

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
    private @FXML ComboBox<String> comboFiltroCategoria;
    private @FXML Label lblTotalProductos;

    // Variables de instancia
    private ProductoDAO productoDAO;
    private CategoriaDAO categoriaDAO;
    private MarcaDAO marcaDAO;

    //Variable que nos ayudara en nuestros listeners para saber si es que seleccionamos un producto
    private Producto productoSeleccionado;
    private List<Categoria> categoriaLista;
    private List<Marca> marcaLista;

    @FXML
    public void initialize(){
        productoDAO = new ProductoDAO();
        categoriaDAO = new CategoriaDAO();
        marcaDAO = new MarcaDAO();

        configurarTabla();
        cargarProductos();
        //cargarCategorias();
        //cargarMarcas();
        //cargarUnidades();
        //cargarSeleccionTabla

    }

    public void configurarTabla(){
        colId.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("nombreCategoria"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("nombreMarca"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colUnidad.setCellValueFactory(new PropertyValueFactory<>("unidadMedida"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));

    }

    public void cargarProductos(){
        List<Producto> list = productoDAO.listarProductos();
        tablaProductos.getItems().clear();
        tablaProductos.getItems().addAll(list);
    }

    // Eventos

    private void configurarSeleccionTablas() {


        tablaProductos.getSelectionModel().selectedItemProperty().addListener(
                (observable, valorAnterior, valorNuevo) -> {
                    if (valorNuevo != null) {
                        productoSeleccionado = valorNuevo;
                        txtNombre.setText(valorNuevo.getNombreProducto());
                        comboCategoria.setValue(valorNuevo.getNombreCategoria());
                        comboMarca.setValue(valorNuevo.getNombreMarca());
                        txtStock.set(valorNuevo.getStock());
                        comboUnidad.se

                    }
                }
        );
}
