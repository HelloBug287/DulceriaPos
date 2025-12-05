package com.dulceria.pos.controlador;

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

    // ===================== INYECCIÓN FXML =====================

    // Formulario
    @FXML private TextField txtNombre;
    @FXML private ComboBox<String> comboCategoria;
    @FXML private ComboBox<String> comboMarca;
    @FXML private TextField txtStock;
    @FXML private ComboBox<String> comboUnidad;
    @FXML private TextField txtPrecio;
    @FXML private CheckBox checkActivo;
    @FXML private Button btnGuardar;
    @FXML private Button btnLimpiar;

    // Tabla
    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, Integer> colId;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, String> colMarca;
    @FXML private TableColumn<Producto, Double> colStock;
    @FXML private TableColumn<Producto, String> colUnidad;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TableColumn<Producto, Boolean> colActivo;

    // Búsqueda
    @FXML private TextField txtBuscar;
    @FXML private Button btnBuscar;
    @FXML private ComboBox<String> comboFiltroCategoria;
    @FXML private Label lblTotalProductos;

    // ===================== VARIABLES DE INSTANCIA =====================

    private ProductoDAO productoDAO;
    private CategoriaDAO categoriaDAO;
    private MarcaDAO marcaDAO;

    private Producto productoSeleccionado;
    private List<Categoria> categoriaLista;
    private List<Marca> marcaLista;

    // ===================== INICIALIZACIÓN =====================

    @FXML
    public void initialize() {
        // Crear instancias de DAOs
        productoDAO = new ProductoDAO();
        categoriaDAO = new CategoriaDAO();
        marcaDAO = new MarcaDAO();

        // Configuraciones iniciales
        configurarTabla();
        cargarProductos();
        cargarCategorias();
        cargarMarcas();
        cargarUnidades();
        cargarSeleccionTabla();
    }

    // ===================== CONFIGURACIÓN DE TABLA =====================

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("nombreCategoria"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("nombreMarca"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colUnidad.setCellValueFactory(new PropertyValueFactory<>("unidadMedida"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));
    }

    // ===================== MÉTODOS PARA CARGAR DATOS =====================

    private void cargarProductos() {
        List<Producto> lista = productoDAO.listarProductos();
        tablaProductos.getItems().clear();
        tablaProductos.getItems().addAll(lista);

        // Actualizar contador
        lblTotalProductos.setText(String.valueOf(lista.size()));
    }

    private void cargarCategorias() {
        categoriaLista = categoriaDAO.listarCategorias();
        comboCategoria.getItems().clear();
        for (Categoria cat : categoriaLista) {
            comboCategoria.getItems().add(cat.getNombreCategoria());
        }

        // También llenar el filtro de búsqueda
        comboFiltroCategoria.getItems().clear();
        comboFiltroCategoria.getItems().add("Todas");
        for (Categoria cat : categoriaLista) {
            comboFiltroCategoria.getItems().add(cat.getNombreCategoria());
        }
    }

    private void cargarMarcas() {
        marcaLista = marcaDAO.listarMarcas();
        comboMarca.getItems().clear();
        for (Marca m : marcaLista) {
            comboMarca.getItems().add(m.getNombreMarca());
        }
    }

    private void cargarUnidades() {
        comboUnidad.getItems().clear();
        comboUnidad.getItems().addAll("PZA", "PQTE", "CAJA", "BOLSA");
    }

    // ===================== EVENTOS DE SELECCIÓN =====================

    private void cargarSeleccionTabla() {
        tablaProductos.getSelectionModel().selectedItemProperty().addListener(
                (observable, valorAnterior, valorNuevo) -> {
                    if (valorNuevo != null) {
                        productoSeleccionado = valorNuevo;
                        txtNombre.setText(valorNuevo.getNombreProducto());
                        comboCategoria.setValue(valorNuevo.getNombreCategoria());
                        comboMarca.setValue(valorNuevo.getNombreMarca());
                        txtStock.setText(String.valueOf(valorNuevo.getStock()));
                        comboUnidad.setValue(valorNuevo.getUnidadMedida());
                        txtPrecio.setText(String.valueOf(valorNuevo.getPrecio()));
                        checkActivo.setSelected(valorNuevo.isActivo());
                    }
                }
        );
    }

    // ===================== MÉTODOS AUXILIARES =====================

    private int obtenerIdCategoria(String nombreCategoria) {
        for (Categoria cat : categoriaLista) {
            if (cat.getNombreCategoria().equals(nombreCategoria)) {
                return cat.getIdCategoria();
            }
        }
        return -1;
    }

    private int obtenerIdMarca(String nombreMarca) {
        for (Marca m : marcaLista) {
            if (m.getNombreMarca().equals(nombreMarca)) {
                return m.getIdMarca();
            }
        }
        return -1;
    }

    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty()) {
            mostrarAlerta("Cuidado", "El nombre es obligatorio", Alert.AlertType.WARNING);
            return false;
        }
        if (comboCategoria.getValue() == null) {
            mostrarAlerta("Cuidado", "Seleccione una categoría", Alert.AlertType.WARNING);
            return false;
        }
        if (comboMarca.getValue() == null) {
            mostrarAlerta("Cuidado", "Seleccione una marca", Alert.AlertType.WARNING);
            return false;
        }
        if (txtStock.getText().trim().isEmpty()) {
            mostrarAlerta("Cuidado", "El stock es obligatorio", Alert.AlertType.WARNING);
            return false;
        }
        if (comboUnidad.getValue() == null) {
            mostrarAlerta("Cuidado", "Seleccione una unidad de medida", Alert.AlertType.WARNING);
            return false;
        }
        if (txtPrecio.getText().trim().isEmpty()) {
            mostrarAlerta("Cuidado", "El precio es obligatorio", Alert.AlertType.WARNING);
            return false;
        }

        // Validar que stock y precio sean números válidos
        try {
            Double.parseDouble(txtStock.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El stock debe ser un número válido", Alert.AlertType.ERROR);
            return false;
        }

        try {
            Double.parseDouble(txtPrecio.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El precio debe ser un número válido", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    // ===================== ACCIONES DE BOTONES =====================

    @FXML
    private void onGuardarClick() {
        // 1. Validar campos
        if (!validarCampos()) {
            return;
        }

        // 2. Obtener datos del formulario
        String nombre = txtNombre.getText().trim();
        int idCategoria = obtenerIdCategoria(comboCategoria.getValue());
        int idMarca = obtenerIdMarca(comboMarca.getValue());
        double stock = Double.parseDouble(txtStock.getText().trim());
        String unidad = comboUnidad.getValue();
        double precio = Double.parseDouble(txtPrecio.getText().trim());
        boolean activo = checkActivo.isSelected();

        // 3. Decidir: ¿CREAR o EDITAR?
        if (productoSeleccionado == null) {
            // CREAR nuevo producto
            Producto nuevo = productoDAO.crearProducto(idCategoria, idMarca, nombre, precio, stock, unidad, activo);
            if (nuevo != null) {
                mostrarAlerta("Éxito", "Producto creado correctamente", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error", "No se pudo crear el producto", Alert.AlertType.ERROR);
            }
        } else {
            // EDITAR producto existente
            productoSeleccionado.setNombreProducto(nombre);
            productoSeleccionado.setIdCategoria(idCategoria);
            productoSeleccionado.setIdMarca(idMarca);
            productoSeleccionado.setStock(stock);
            productoSeleccionado.setUnidadMedida(unidad);
            productoSeleccionado.setPrecio(precio);
            productoSeleccionado.setActivo(activo);

            boolean exito = productoDAO.actualizarProducto(productoSeleccionado);
            if (exito) {
                mostrarAlerta("Éxito", "Producto actualizado correctamente", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error", "No se pudo actualizar el producto", Alert.AlertType.ERROR);
            }
        }

        // 4. Refrescar tabla y limpiar formulario
        cargarProductos();
        limpiarFormulario();
    }

    @FXML
    private void onLimpiarClick() {
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtStock.clear();
        txtPrecio.clear();
        comboCategoria.setValue(null);
        comboMarca.setValue(null);
        comboUnidad.setValue(null);
        checkActivo.setSelected(true);
        productoSeleccionado = null;
        tablaProductos.getSelectionModel().clearSelection();
    }

    @FXML
    private void onBuscarClick() {
        String termino = txtBuscar.getText().trim();

        if (termino.isEmpty()) {
            cargarProductos(); // Si está vacío, mostrar todos
            return;
        }

        List<Producto> resultados = productoDAO.buscarPorNombre(termino);
        tablaProductos.getItems().clear();
        tablaProductos.getItems().addAll(resultados);
        lblTotalProductos.setText(String.valueOf(resultados.size()));
    }

    // ===================== UTILIDADES =====================

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}

