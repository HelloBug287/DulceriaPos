package com.dulceria.pos.controlador;
//Importamos los DAO´s que ocuparemos en esta
import com.dulceria.pos.DAO.CategoriaDAO;
import com.dulceria.pos.DAO.MarcaDAO;
import com.dulceria.pos.DAO.ProveedorDAO;

// importamos modelos
import com.dulceria.pos.modelo.Categoria;
import com.dulceria.pos.modelo.Marca;
import com.dulceria.pos.modelo.Proveedor;

//componentes FX
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

/**
 * Controlador para la sección de Catálogos (Categorías, Marcas, Proveedores).
 *
 * Responsabilidades:
 * - Mostrar tablas con registros de Categorías/Marcas/Proveedores.
 * - Proveer CRUD básico (crear/editar) desde formularios simples.
 * - Actualizar las tablas inmediatamente tras cambios.
 *
 * Nota: este controlador delega la persistencia a los DAOs correspondientes.
 */
public class CatalogosController {
    // Inyección de componentes FXML
    // Tab de Categorías
    @FXML private TextField txtNombreCategoria;
    @FXML private CheckBox checkActivoCategoria;
    @FXML private TableView<Categoria> tablaCategorias;
    @FXML private TableColumn<Categoria, Integer> colIdCategoria;
    @FXML private TableColumn<Categoria, String> colNombreCategoria;
    @FXML private TableColumn<Categoria, Boolean> colActivoCategoria;

    // Tab de Marcas
    @FXML private TextField txtNombreMarca;
    @FXML private CheckBox checkActivoMarca;
    @FXML private TableView<Marca> tablaMarcas;
    @FXML private TableColumn<Marca, Integer> colIdMarca;
    @FXML private TableColumn<Marca, String> colNombreMarca;
    @FXML private TableColumn<Marca, Boolean> colActivoMarca;

    // Tab de Proveedores
    @FXML private TextField txtProvNombre;
    @FXML private TextField txtProvTelefono;
    @FXML private TextField txtProvEmail;
    @FXML private CheckBox checkActivoProveedor;
    @FXML private TableView<Proveedor> tablaProveedores;
    @FXML private TableColumn<Proveedor, Integer> colProvId;
    @FXML private TableColumn<Proveedor, String> colProvNombre;
    @FXML private TableColumn<Proveedor, String> colProvTel;
    @FXML private TableColumn<Proveedor, String> colProvEmail;
    @FXML private TableColumn<Proveedor, Boolean> colProvActivo;

    // DAOs
    private CategoriaDAO categoriaDAO;
    private MarcaDAO marcaDAO;
    private ProveedorDAO proveedorDAO;

    // Variables para saber si estamos editando
    private Categoria categoriaSeleccionada = null;
    private Marca marcaSeleccionada = null;
    private Proveedor proveedorSeleccionado = null;

    //Este metodo nos sirve para cuando demos click en la vista, automáticamente carga todas las propiedades necesarias para poder interactuar con ella
    @FXML
    public void initialize() {
        //Creamos aqui nuestras instancias de los DAO´s para que creen los objetos y de esa forma tener acceso a todos sus metodos (de los daos) para que nuestros metodos creados en este controlador puedan funcionar
        categoriaDAO = new CategoriaDAO();
        marcaDAO = new MarcaDAO();
        proveedorDAO = new ProveedorDAO();

        // estos son los metodos del controlador
        configurarTablaCategorias();
        configurarTablaMarcas();
        configurarTablaProveedores();
        cargarCategorias();
        cargarMarcas();
        cargarProveedores();
        configurarSeleccionTablas();
    }

    /* CONFIGURAR TABLAS
    * Le proporcionamos a cada una de las columnas de cada tabla que es lo que se le asignara
    * con setCellValueFactory le decimos que propiedad contendra y con el PropertyValueFactory
    * se lo asignara buscandolo en su modelo correspondiente, este metodo es "dependiente" de los metodos "cargar" ya que estos
    * solamente se les esta diciendo que tipo de dato van a contener y en donde los buscara
    * */
    private void configurarTablaCategorias() {
        colIdCategoria.setCellValueFactory(new PropertyValueFactory<>("idCategoria"));
        colNombreCategoria.setCellValueFactory(new PropertyValueFactory<>("nombreCategoria"));
        colActivoCategoria.setCellValueFactory(new PropertyValueFactory<>("activo"));
    }

    private void configurarTablaMarcas() {
        colIdMarca.setCellValueFactory(new PropertyValueFactory<>("idMarca"));
        colNombreMarca.setCellValueFactory(new PropertyValueFactory<>("nombreMarca"));
        colActivoMarca.setCellValueFactory(new PropertyValueFactory<>("activo"));
    }

    private void configurarTablaProveedores() {
        colProvId.setCellValueFactory(new PropertyValueFactory<>("idProveedor"));
        colProvNombre.setCellValueFactory(new PropertyValueFactory<>("nombreProveedor"));
        colProvTel.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colProvEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colProvActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));
    }

    /*CARGAR DATOS
    * Estos son los metodos encargados de cargar los datos segun el tab seleccionado
    * cada dao tiene un metodo para listar todos sus datos. */

    private void cargarCategorias() {
        List<Categoria> lista = categoriaDAO.listarCategorias(); //creamos una lista que contendra todas las categorias gracias a nuestro metodo
        tablaCategorias.getItems().clear(); // limpiamos la tabla para limpiar los datos viejos y setear los nuevos
        tablaCategorias.getItems().addAll(lista); // aqui añadimos a la tabla todos los elementos que contiene nuestra lista, por eso la creamos arriba
    }

    // funciona de la misma manera que cargar categorias
    private void cargarMarcas() {
        List<Marca> lista = marcaDAO.listarMarcas();
        tablaMarcas.getItems().clear();
        tablaMarcas.getItems().addAll(lista);
    }

    // funciona de la misma manera que cargar categorias
    private void cargarProveedores() {
        List<Proveedor> lista = proveedorDAO.listarProveedores();
        tablaProveedores.getItems().clear();
        if (lista != null) {
            tablaProveedores.getItems().addAll(lista);
        }
    }

    // Eventos (Listener)
    private void configurarSeleccionTablas() {

        /*
        * A cada una de las tablas les agregaremos un Evento
        * estos eventos detectan cuando el usuario hace clic en una fila de la tabla
        * su objetivo es tomar los datos del objeto seleccionado y pasarlos al
        * formulario  para que puedan ser editados.
        */

        // Categorías
        tablaCategorias.getSelectionModel().selectedItemProperty().addListener(
                (observable, valorAnterior, valorNuevo) -> {
                    if (valorNuevo != null) {
                        categoriaSeleccionada = valorNuevo;
                        txtNombreCategoria.setText(valorNuevo.getNombreCategoria());
                        checkActivoCategoria.setSelected(valorNuevo.isActivo());
                    }
                }
        );

        // Marcas
        tablaMarcas.getSelectionModel().selectedItemProperty().addListener(
                (observable, valorAnterior, valorNuevo) -> {
                    if (valorNuevo != null) {
                        marcaSeleccionada = valorNuevo;
                        txtNombreMarca.setText(valorNuevo.getNombreMarca());
                        checkActivoMarca.setSelected(valorNuevo.isActivo());
                    }
                }
        );

        // Proveedores
        tablaProveedores.getSelectionModel().selectedItemProperty().addListener(
                (observable, valorAnterior, valorNuevo) -> {
                    if (valorNuevo != null) {
                        proveedorSeleccionado = valorNuevo;
                        txtProvNombre.setText(valorNuevo.getNombreProveedor());
                        txtProvTelefono.setText(valorNuevo.getTelefono());
                        txtProvEmail.setText(valorNuevo.getEmail());
                        checkActivoProveedor.setSelected(valorNuevo.isActivo());
                    }
                }
        );
    }

    //  Metodos para botones de categorias

    @FXML
    private void onGuardarCategoria() {
        // obtenemos los datos del textfield y el checkBox
        String nombre = txtNombreCategoria.getText().trim();
        boolean activo = checkActivoCategoria.isSelected();

        //verificacion por si el campo del nombre de la categoria esta vacio
        if (nombre.isEmpty()) {
            mostrarAlerta("ADVERTENCIA", "Escribe un nombre para la categoría", Alert.AlertType.WARNING); //lanzamos una alerta
            return;
        }

        // si la categoria seleccionada es null quiere decir que estamos creando una nueva categoria
        if (categoriaSeleccionada == null) {

            // creamos una variable y mandamos a llamar a nuestro metodo del DAO
            Categoria nueva = categoriaDAO.crearCategoria(nombre);
            if (nueva != null) {
                // Actualizar el estado activo después de crear
                nueva.setActivo(activo);
                categoriaDAO.actualizarCategoria(nueva);
                mostrarAlerta("Éxito", "Categoría creada", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error", "No se pudo crear la categoría", Alert.AlertType.ERROR);
            }
        } else {
            // si la variable de categoria seleccionada es diferente de null la enviamos al formulario para editarla
            categoriaSeleccionada.setNombreCategoria(nombre);
            categoriaSeleccionada.setActivo(activo);

            boolean exito = categoriaDAO.actualizarCategoria(categoriaSeleccionada);
            if (exito) {
                mostrarAlerta("Éxito", "Categoría actualizada", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error", "No se pudo actualizar", Alert.AlertType.ERROR);
            }
        }
        //llamamos al metodo para el mpmento de crearla podamos verla reflejada en la tabla inmediatamente y limpiamos el formulario despues de la insercion
        cargarCategorias();
        limpiarFormularioCategoria();
    }

    @FXML
    private void onLimpiarCategoria() {
        limpiarFormularioCategoria();
    }

    private void limpiarFormularioCategoria() {
        txtNombreCategoria.clear();
        checkActivoCategoria.setSelected(true);
        categoriaSeleccionada = null;
        tablaCategorias.getSelectionModel().clearSelection();
    }

    // MARCAS para botones de acciones
    //la logica es la misma que al crear una categoria
    @FXML
    private void onGuardarMarca() {
        String nombre = txtNombreMarca.getText().trim();
        boolean activo = checkActivoMarca.isSelected();

        if (nombre.isEmpty()) {
            mostrarAlerta("Cuidado", "Escribe un nombre para la marca", Alert.AlertType.WARNING);
            return;
        }

        if (marcaSeleccionada == null) {
            // CREAR
            Marca nueva = marcaDAO.crearMarca(nombre, activo);
            if (nueva != null) {
                mostrarAlerta("Éxito", "Marca creada", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error", "No se pudo crear la marca", Alert.AlertType.ERROR);
            }
        } else {
            // EDITAR
            marcaSeleccionada.setNombreMarca(nombre);
            marcaSeleccionada.setActivo(activo);

            boolean exito = marcaDAO.actualizarMarca(marcaSeleccionada);
            if (exito) {
                mostrarAlerta("Éxito", "Marca actualizada", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error", "No se pudo actualizar", Alert.AlertType.ERROR);
            }
        }

        cargarMarcas();
        limpiarFormularioMarca();
    }

    @FXML
    private void onLimpiarMarca() {
        limpiarFormularioMarca();
    }

    private void limpiarFormularioMarca() {
        txtNombreMarca.clear();
        checkActivoMarca.setSelected(true);
        marcaSeleccionada = null;
        tablaMarcas.getSelectionModel().clearSelection();
    }


    //Proveedores
    //la misma logica que los 2 anteriores
    @FXML
    private void onGuardarProveedor() {
        String nombre = txtProvNombre.getText().trim();
        String telefono = txtProvTelefono.getText().trim();
        String email = txtProvEmail.getText().trim();
        boolean activo = checkActivoProveedor.isSelected();

        if (nombre.isEmpty()) {
            mostrarAlerta("Cuidado", "El nombre es obligatorio", Alert.AlertType.WARNING);
            return;
        }

        if (proveedorSeleccionado == null) {
            // CREAR
            Proveedor nuevo = proveedorDAO.crearProveedor(nombre, telefono, email, activo);
            if (nuevo != null) {
                mostrarAlerta("Éxito", "Proveedor creado", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error", "No se pudo crear el proveedor", Alert.AlertType.ERROR);
            }
        } else {
            // EDITAR
            proveedorSeleccionado.setNombreProveedor(nombre);
            proveedorSeleccionado.setTelefono(telefono);
            proveedorSeleccionado.setEmail(email);
            proveedorSeleccionado.setActivo(activo);

            boolean exito = proveedorDAO.actualizarProveedor(proveedorSeleccionado);
            if (exito) {
                mostrarAlerta("Éxito", "Proveedor actualizado", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error", "No se pudo actualizar", Alert.AlertType.ERROR);
            }
        }

        cargarProveedores();
        limpiarFormularioProveedor();
    }

    @FXML
    private void onLimpiarProveedor() {
        limpiarFormularioProveedor();
    }

    private void limpiarFormularioProveedor() {
        txtProvNombre.clear();
        txtProvTelefono.clear();
        txtProvEmail.clear();
        checkActivoProveedor.setSelected(true);
        proveedorSeleccionado = null;
        tablaProveedores.getSelectionModel().clearSelection();
    }

    // Metodo auxiliar para mostrar alertas
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}