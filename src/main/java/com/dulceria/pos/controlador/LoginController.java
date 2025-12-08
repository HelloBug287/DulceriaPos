package com.dulceria.pos.controlador;

import com.dulceria.pos.Main; // Importamos Main para usarlo como referencia en la ruta
import com.dulceria.pos.DAO.UsuarioDAO;
import com.dulceria.pos.modelo.Usuario;

import com.dulceria.pos.util.Conexion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

// Importamos la librería de seguridad
import org.mindrot.jbcrypt.BCrypt;

/**
 * Controlador de Login.
 * Usos principales:
 * Validar que la BD esté accesible antes de intentar autenticar.
 * Verificar credenciales usando BCrypt (comparar con hash almacenado en BD).
 * En caso exitoso, abrir la ventana principal y pasar el objeto Usuario al PrincipalController.
 */
public class LoginController {

    @FXML
    private Button login;

    @FXML
    private TextField user;

    @FXML
    private PasswordField password;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    //Creamos un metodo para el boton Login
    @FXML
    protected void onLoginClick() {

        //llamamos a nuestro metodo probarConexion para intentar conectar con el servidor
        if (!Conexion.probarConexion()) {
            mostrarAlerta("Error de Conexión", "No se puede conectar al servidor. Intentalo mas tarde.", Alert.AlertType.ERROR);
            return;
        }
        //Obtenemos los datos ingresados de los campos de user y password
        String usernameIngresado = user.getText();
        String passwordIngresado = password.getText();

        //Validamos si los campos estan vacíos
        if (usernameIngresado.isEmpty() || passwordIngresado.isEmpty()) {
            mostrarAlerta("Datos Faltantes", "Por favor, escribe usuario y contraseña.", Alert.AlertType.WARNING);
            return;
        }

        // Buscamos al usuario en la BD con el metodo de  buscarPorUsername
        Usuario usuarioEncontrado = usuarioDAO.buscarPorUsername(usernameIngresado);

        // Verificamos si el usuario existe
        if (usuarioEncontrado != null) {

            // Validamos la contraseña usando BCrypt
            // Compara texto plano (passwordIngresado) vs Hash de la BD (usuarioEncontrado.getPassword())
            boolean passwordCorrecta = BCrypt.checkpw(passwordIngresado, usuarioEncontrado.getPassword());

            if (passwordCorrecta) {
                // si la contraseñas coinciden continuamos

                // Verificamos si el usuario esta activo, de lo contrario no le permitimos el acceso
                if (usuarioEncontrado.isActivo()){
                    mostrarAlerta("Bienvenido", "Hola, " + usuarioEncontrado.getNombreCompleto(), Alert.AlertType.INFORMATION);

                    //  Cambio de ventana
                    try {
                        // Cargar el diseño de la ventana Principal
                        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/dulceria/pos/Principal.fxml"));
                        Parent root = loader.load();

                        // Obtener el controlador de la nueva ventana y pasarle el usuario
                        PrincipalController controlador = loader.getController();
                        controlador.initData(usuarioEncontrado);

                        // Preparar y mostrar la nueva ventana
                        Stage stage = new Stage();
                        stage.setTitle("Sistema Punto de Venta - Dulcería");
                        stage.setScene(new Scene(root));
                        stage.setResizable(true);
                        stage.show();

                        // Cerrar la ventana de Login actual
                        Stage loginStage = (Stage) login.getScene().getWindow();
                        loginStage.close();

                    } catch (IOException ex) {
                        mostrarAlerta("Error de Sistema", "No se pudo cargar la ventana principal: " + ex.getMessage(), Alert.AlertType.ERROR);
                    }
                } else {
                    // El usuario está inactivo
                    mostrarAlerta("Acceso Denegado","Usuario Inactivo", Alert.AlertType.ERROR);
                }

            } else {
                // si el usuario existe, pero ingreso mal sus credenciales lanzamos una alerta
                mostrarAlerta("Error de Acceso", "Usuario o contraseña incorrectos.", Alert.AlertType.ERROR);
            }

        } else {
            //el usuario no existe
            mostrarAlerta("Error de Acceso", "El usuario Ingresado no existe.", Alert.AlertType.ERROR);
        }
    }
    // Metodo auxiliar para mostar alertas
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}