package com.dulceria.pos.controlador;

import com.dulceria.pos.modelo.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class PrincipalController {

    @FXML private Label lblUsuario;
    @FXML private StackPane contentArea;

    @FXML private Button btnInicio;
    @FXML private Button btnVentas;
    @FXML private Button btnProductos;
    @FXML private Button btnUsuarios;
    @FXML private Button btnReportes;
    @FXML private Button btnCatalogos;

    private Usuario usuarioActual;

    public void initData(Usuario usuario) {
        this.usuarioActual = usuario;
        lblUsuario.setText("Usuario: " + usuario.getNombreCompleto());

        if (usuario.getIdRol() == 2) {
            if(btnUsuarios != null) btnUsuarios.setDisable(true);
            if(btnProductos != null) btnProductos.setDisable(true);
            if(btnCatalogos != null) btnCatalogos.setDisable(true);
            if(btnReportes != null) btnReportes.setDisable(true);
            if(btnInicio != null) btnInicio.setDisable(true);
            onVentasClick();
        }else {
            onInicioClick();
        }
    }

    private void cargarVista(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent vista = loader.load();

            // Pasar usuario a Ventas
            if (fxml.contains("Ventas.fxml")) {
                VentasController ventasCtrl = loader.getController();
                ventasCtrl.setUsuarioActual(usuarioActual.getIdUsuario());
            }

            contentArea.getChildren().clear();
            contentArea.getChildren().add(vista);

        } catch (IOException ex) {
            System.err.println("Error cargando vista: " + fxml);
            ex.printStackTrace();
        }
    }

    @FXML
    protected void onInicioClick() {
        cargarVista("/com/dulceria/pos/Inicio.fxml");
    }

    @FXML
    protected void onVentasClick() {
        cargarVista("/com/dulceria/pos/Ventas.fxml");
    }

    @FXML
    protected void onProductosClick() {
        cargarVista("/com/dulceria/pos/Productos.fxml");
    }

    @FXML
    protected void onUsuariosClick() {
        cargarVista("/com/dulceria/pos/Usuarios.fxml");
    }

    @FXML
    protected void onReportesClick() {
        cargarVista("/com/dulceria/pos/Reportes.fxml");
    }

    @FXML
    protected void onCatalogosClick() {
        cargarVista("/com/dulceria/pos/Catalogos.fxml");
    }

    @FXML
    protected void onSalirClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dulceria/pos/Login.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new javafx.scene.Scene(root));
            stage.setResizable(false);
            stage.show();

            Stage myStage = (Stage) lblUsuario.getScene().getWindow();
            myStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}