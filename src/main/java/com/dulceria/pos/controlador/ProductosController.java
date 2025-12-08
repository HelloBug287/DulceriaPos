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
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para la gestión de Productos.
 *
 * Responsabilidades:
 * - Mostrar la lista de productos en tabla (usando `vista_productos`).
 * - Buscar y filtrar productos (texto + categoría).
 * - Crear y editar productos (delegando la persistencia a `ProductoDAO`).
 * - Mantener referencias a listas de categorías y marcas para mapear nombres/ids.
 */
public class ProductosController {

    //INYECCIÓN FXML

    // Formulario
    @FXML private TextField txtNombre;
    @FXML private ComboBox<String> comboCategoria;
    @FXML private ComboBox<String> comboMarca;
    @FXML private TextField txtStock;
    @FXML private ComboBox<String> comboUnidad;
    @FXML private TextField txtPrecio;
    @FXML private CheckBox checkActivo;

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
    @FXML private ComboBox<String> comboFiltroCategoria;
    @FXML private Label lblTotalProductos;

    // VARIABLES DE INSTANCIA

    private ProductoDAO productoDAO;
    private CategoriaDAO categoriaDAO;
    private MarcaDAO marcaDAO;

    private Producto productoSeleccionado;
    private List<Categoria> categoriaLista;
    private List<Marca> marcaLista;

    // INICIALIZACIÓN

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
        configurarSeleccionTabla();

        // Configurar búsqueda en tiempo real con filtros combinados
        configurarBusquedaTiempoReal();
    }


    // CONFIGURACIÓN DE TABLA

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

    // METODOS PARA CARGAR DATOS

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

    // EVENTOS DE SELECCIÓN

    private void configurarSeleccionTabla() {
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

    // BÚSQUEDA EN TIEMPO REAL CON FILTROS COMBINADOS

    /**
     * Configura los listeners para búsqueda en tiempo real.
     * Ambos listeners (texto y categoría) llaman al mismo metodo unificado
     * para que los filtros trabajen en conjunto.
     */
    private void configurarBusquedaTiempoReal() {
        // Listener para el campo de texto - cada tecla activa el filtro
        txtBuscar.textProperty().addListener(
                (observable, valorAnterior, valorNuevo) -> {
                    aplicarFiltros();
                }
        );

        // Listener para el combo de categorías - cada cambio activa el filtro
        comboFiltroCategoria.valueProperty().addListener(
                (observable, valorAnterior, valorNuevo) -> {
                    aplicarFiltros();
                }
        );
    }

    /**
     * Metodo UNIFICADO que aplica AMBOS filtros simultáneamente.
     * Si hay texto de búsqueda → filtra por nombre usando el DAO
     * Si hay categoría seleccionada → filtra los resultados por categoría
     * Si ambos están activos → el producto debe cumplir AMBAS condiciones
     */
    private void aplicarFiltros() {
        // Obtener los valores actuales de los filtros
        String textoBusqueda = txtBuscar.getText();
        String categoriaSeleccionada = comboFiltroCategoria.getValue();

        // Obtener la lista base de productos
        List<Producto> productos;

        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            // Si no hay texto de búsqueda, traemos todos los productos
            productos = productoDAO.listarProductos();
        } else {
            // Si hay texto, usamos el método de búsqueda por nombre del DAO
            // Esto ya filtra en la BD usando LIKE '%texto%'
            productos = productoDAO.buscarPorNombre(textoBusqueda.trim());
        }

        // Aplicar filtro de categoría sobre los resultados
        tablaProductos.getItems().clear();
        int contador = 0;

        for (Producto p : productos) {
            // Determinar si debemos mostrar este producto
            boolean mostrar;

            if (categoriaSeleccionada == null || categoriaSeleccionada.equals("Todas")) {
                // No hay filtro de categoría activo, mostrar el producto
                mostrar = true;
            } else {
                // Hay filtro de categoría, verificar si coincide
                mostrar = p.getNombreCategoria() != null && p.getNombreCategoria().equals(categoriaSeleccionada);
            }

            // Si pasa el filtro, agregarlo a la tabla
            if (mostrar) {
                tablaProductos.getItems().add(p);
                contador++;
            }
        }

        // Actualizar el contador de productos mostrados
        lblTotalProductos.setText(String.valueOf(contador));
    }

    // MÉTODOS AUXILIARES

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

    // ACCIONES DE BOTONES

    @FXML
    private void onGuardarClick() {
        // Validar campos
        if (!validarCampos()) {
            return;
        }

        // Obtener datos del formulario
        String nombre = txtNombre.getText().trim();
        int idCategoria = obtenerIdCategoria(comboCategoria.getValue());
        int idMarca = obtenerIdMarca(comboMarca.getValue());
        double stock = Double.parseDouble(txtStock.getText().trim());
        String unidad = comboUnidad.getValue();
        double precio = Double.parseDouble(txtPrecio.getText().trim());
        boolean activo = checkActivo.isSelected();

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

        // Refrescar tabla y limpiar formulario
        cargarProductos();
        limpiarFormulario();

        // Limpiar también los filtros para mostrar todos los productos
        txtBuscar.clear();
        comboFiltroCategoria.setValue("Todas");
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

    // UTILIDADES

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    /* metodo publico para filtrar productos por stock bajo
     * Filtra la tabla mostrando únicamente los productos cuyo stock sea menor que el umbral determinado
     * Si el umbral es negativo o cero, no aplica filtro (muestra todos).
     */
    public void filtrarPorStockBajo(double umbral) {
        if (umbral <= 0) {
            cargarProductos();
            return;
        }

        List<Producto> todos = productoDAO.listarProductos();
        List<Producto> filtrados = new ArrayList<>();
        for (Producto p : todos) {
            if (p != null && p.isActivo() && p.getStock() < umbral) {
                filtrados.add(p);
            }
        }

        tablaProductos.getItems().clear();
        tablaProductos.getItems().addAll(filtrados);
        lblTotalProductos.setText(String.valueOf(filtrados.size()));
    }
}