package com.dulceria.pos.controlador;

import com.dulceria.pos.DAO.RolDAO;
import com.dulceria.pos.DAO.UsuarioDAO;
import com.dulceria.pos.modelo.Rol;
import com.dulceria.pos.modelo.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

/**
 * Controlador para la gestión de usuarios (CRUD).
 *
 * Responsabilidades:
 * - Mostrar usuarios en una tabla con su rol.
 * - Crear y actualizar usuarios (controla la encriptación de contraseñas con BCrypt).
 * - Validar campos antes de enviar al DAO.
 *
 * Seguridad:
 * - NO se muestra el hash de la contraseña en el formulario.
 * - Si el campo contraseña se deja vacío en edición, se mantiene la contraseña anterior.
 */
public class UsuariosController {

// Cada uno de estos, son inyecciones de componentes que se encuentran en nuestro archivo FXML

    // Campos del formulario (FXML)
    @FXML private TextField txtNombre;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> comboRol;
    @FXML private CheckBox checkActivo;

    // Botones
    @FXML private Button btnGuardar;
    @FXML private Button btnLimpiar;

    // Tabla y Columnas
    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, Integer> colId;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colUsuario;
    @FXML private TableColumn<Usuario, String> colRol;
    @FXML private TableColumn<Usuario, Boolean> colActivo;


    // Instancias de nuestros DAOS para manejar lógica SQL
    private UsuarioDAO usuarioDAO;
    private RolDAO rolDAO;

    // Variable para saber si estamos editando.
    // Si es null estamos creando un nuevo usuario, si tiene datos estamos editando uno seleccionado de la tabla.
    private Usuario usuarioSeleccionado;

    @FXML
    public void initialize(){
        // Inicializamos los objetos DAO
        usuarioDAO = new UsuarioDAO();
        rolDAO = new RolDAO();

        // Configuraciones iniciales al cargar la vista
        configurarTabla();
        cargarUsuarios();
        cargarRoles();
        configurarSeleccionTabla();
    }

    private void configurarTabla() {
        // Enlazamos las columnas con los atributos de la clase Usuario
        colId.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("username"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("nombreRol"));
        colActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));
    }

    /**
     * Carga usuarios desde la BD y los muestra en la tabla.
     */
    private void cargarUsuarios() {
        try {

            List<Usuario> usuarios = usuarioDAO.listarUsuarios();
            tablaUsuarios.getItems().clear();
            tablaUsuarios.getItems().addAll(usuarios);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al cargar usuarios.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Carga los roles desde la BD para poblar el combo (lista de nombres de roles).
     */
    private void cargarRoles() {
        try {
            List<Rol> roles = rolDAO.listarRoles();
            comboRol.getItems().clear();
            for (Rol rol : roles) {
                comboRol.getItems().add(rol.getNombreRol());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Metodo que detecta click en un usuario de la tabla
    private void configurarSeleccionTabla() {
        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        cargarUsuarioEnFormulario(newValue);
                    }
                }
        );
    }

    /**
     * Pasa los datos del usuario seleccionado al formulario para permitir edición.
     */
    private void cargarUsuarioEnFormulario(Usuario usuario) {
        usuarioSeleccionado = usuario; // Guardamos la referencia para saber a quién editar

        txtNombre.setText(usuario.getNombreCompleto());
        txtUsuario.setText(usuario.getUsername());

        txtPassword.setText("");

        checkActivo.setSelected(usuario.isActivo());
        comboRol.setValue(usuario.getNombreRol());
    }

    @FXML
    private void onGuardarClick() {
        // Validamos antes de procesar
        if (!validarCampos()) {
            return;
        }

        // 2. Obtenemos datos de la vista
        String nombre = txtNombre.getText().trim();
        String username = txtUsuario.getText().trim();
        String password = txtPassword.getText();
        String rolSeleccionado = comboRol.getValue();
        boolean activo = checkActivo.isSelected();

        // Convertimos el String del Combo al ID numérico
        int idRol = obtenerIdRol(rolSeleccionado);

        try {
            if (usuarioSeleccionado == null) {
                // MODO CREAR
                crearNuevoUsuario(idRol, username, password, nombre, activo);
            } else {
                // MODO EDITAR
                actualizarUsuarioExistente(idRol, username, password, nombre, activo);
            }

            // Refrescamos tabla y limpiamos
            cargarUsuarios();
            limpiarFormulario();

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al guardar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty()) {
            mostrarAlerta("Cuidado", "El nombre es obligatorio", Alert.AlertType.WARNING);
            return false;
        }
        if (txtUsuario.getText().trim().isEmpty()) {
            mostrarAlerta("Cuidado", "El usuario es obligatorio", Alert.AlertType.WARNING);
            return false;
        }
        if (comboRol.getValue() == null) {
            mostrarAlerta("Cuidado", "Debe seleccionar un rol", Alert.AlertType.WARNING);
            return false;
        }

        // Si el usuario es nuevo la contraseña es obligatoria.
        if (usuarioSeleccionado == null && txtPassword.getText().isEmpty()) {
            mostrarAlerta("Cuidado", "La contraseña es obligatoria para nuevos usuarios", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private void crearNuevoUsuario(int idRol, String username, String password, String nombre, boolean activo) {
        // Verificamos duplicados
        if (usuarioDAO.buscarPorUsername(username) != null) {
            mostrarAlerta("Error", "El usuario ya existe", Alert.AlertType.ERROR);
            return;
        }


        // Usamos BCrypt para generar el hash. gensalt() crea la semilla aleatoria.
        String passwordEncriptada = BCrypt.hashpw(password, BCrypt.gensalt());

        // Enviamos la contraseña YA encriptada al DAO
        Usuario nuevo = usuarioDAO.crearUsuario(idRol, username, passwordEncriptada, nombre, activo);

        if (nuevo != null) {
            mostrarAlerta("Éxito", "Usuario creado correctamente", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error", "No se pudo crear en base de datos", Alert.AlertType.ERROR);
        }
    }

    private void actualizarUsuarioExistente(int idRol, String username, String password, String nombre, boolean activo) {
        // 1. Actualizamos los datos normales (Nombre, Rol, Usuario, Activo)
        usuarioSeleccionado.setIdRol(idRol);
        usuarioSeleccionado.setUsername(username);
        usuarioSeleccionado.setNombreCompleto(nombre);
        usuarioSeleccionado.setActivo(activo);

        // LÓGICA DE LA CONTRASEÑA
        if (!password.isEmpty()) {

            /// Si escribió algo hay qye encriptarlo de nuevo
            String nuevaPasswordEncriptada = BCrypt.hashpw(password, BCrypt.gensalt());

            // Guardamos la contraseña encriptada en el objeto
            usuarioSeleccionado.setPassword(nuevaPasswordEncriptada);

        }
        // Si el campo estaba vacío
        // NO tocamos la contraseña. El objeto usuarioSeleccionado mantiene
        // la contraseña vieja (el hash) que ya traía de la base de datos.

        // Finalmente mandamos a guardar a la BD
        boolean exito = usuarioDAO.actualizarUsuario(usuarioSeleccionado);

        if (exito) {
            mostrarAlerta("Éxito", "Usuario actualizado correctamente", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error", "No se pudo actualizar el usuario", Alert.AlertType.ERROR);
        }
    }
    private int obtenerIdRol(String nombreRol) {
        switch (nombreRol) {
            case "Administrador": return 1;
            case "Vendedor": return 2;
            default: return 2;
        }
    }

    @FXML
    private void onLimpiarClick() {
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtUsuario.clear();
        txtPassword.clear();
        comboRol.setValue(null);
        checkActivo.setSelected(true);
        usuarioSeleccionado = null; // Volvemos a modo "Crear"
        tablaUsuarios.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}