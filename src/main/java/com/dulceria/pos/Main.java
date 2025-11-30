package com.dulceria.pos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        //  Buscamos el archivo FXML
        URL fxmlLocation = Main.class.getResource("/com/dulceria/pos/Login.fxml");

        // Validación rápida por si el archivo no esta
        if (fxmlLocation == null) {
            System.err.println(" ERROR CRÍTICO: No se pudo encontrar Login.fxml");
            System.err.println("Verifica que el archivo esté en: src/main/resources/com/dulceria/pos/Login.fxml");
            return;
        }

        // Cargamos la interfaz del login
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);

        // Creamos la escena (el contenido de la ventana)
        Scene scene = new Scene(fxmlLoader.load(), 700, 400);

        // Configuramos la ventana principal
        stage.setTitle("Dulcería POS - Inicio de Sesión");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        // Este metodo lanza la aplicación JavaFX
        launch();
    }
}
