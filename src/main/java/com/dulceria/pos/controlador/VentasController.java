package com.dulceria.pos.controlador;

import com.dulceria.pos.DAO.ProductoDAO;
import com.dulceria.pos.DAO.VentaDAO;
import com.dulceria.pos.DAO.DetalleVentaDAO;
import com.dulceria.pos.modelo.Producto;
import com.dulceria.pos.modelo.Venta;
import com.dulceria.pos.modelo.DetalleVenta;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;
import java.util.Optional;

public class VentasController {

    // ===================== INYECCIÓN FXML =====================

    @FXML private TextField txtBuscarProducto;
    @FXML private Button btnBuscar;
    @FXML private GridPane gridProductos;

    @FXML private TableView<ItemCarrito> tablaCarrito;
    @FXML private TableColumn<ItemCarrito, String> colProducto;
    @FXML private TableColumn<ItemCarrito, Double> colPrecio;
    @FXML private TableColumn<ItemCarrito, Integer> colCantidad;
    @FXML private TableColumn<ItemCarrito, Double> colImporte;

    @FXML private Label lblFolio;
    @FXML private Label lblSubtotal;
    @FXML private Label lblImpuestos;
    @FXML private Label lblTotal;

    @FXML private Button btnCobrar;
    @FXML private Button btnCancelar;
    @FXML private Button btnQuitar;

    // ===================== VARIABLES DE INSTANCIA =====================

    private ProductoDAO productoDAO;
    private VentaDAO ventaDAO;
    private DetalleVentaDAO detalleVentaDAO;

    private ObservableList<ItemCarrito> carrito;
    private List<Producto> productosDisponibles;

    private static final double IVA = 0.16;
    private int idUsuarioActual = 1; // Deberías recibirlo de PrincipalController

    // ===================== INICIALIZACIÓN =====================

    @FXML
    public void initialize() {
        System.out.println("🚀 Inicializando VentasController...");

        try {
            // Crear instancias de DAOs
            productoDAO = new ProductoDAO();
            ventaDAO = new VentaDAO();
            detalleVentaDAO = new DetalleVentaDAO();

            // Inicializar carrito
            carrito = FXCollections.observableArrayList();

            // Configurar componentes
            configurarTablaCarrito();
            cargarProductos();
            generarNuevoFolio();
            configurarBusquedaTiempoReal();
            configurarBotones();

            // Inicializar totales
            actualizarTotales();

            System.out.println("✅ VentasController inicializado correctamente");

        } catch (Exception e) {
            System.err.println("❌ Error inicializando VentasController: " + e.getMessage());
            e.printStackTrace();
            mostrarAlerta("Error", "Error al inicializar el módulo de ventas: " + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    // ===================== CONFIGURACIÓN =====================

    private void configurarTablaCarrito() {
        colProducto.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNombreProducto()));

        colPrecio.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getPrecioUnitario()).asObject());

        colCantidad.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getCantidad()).asObject());

        colImporte.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getImporteTotal()).asObject());

        tablaCarrito.setItems(carrito);

        System.out.println("✅ Tabla del carrito configurada");
    }

    private void configurarBotones() {
        // Asegurarse de que los botones tengan sus eventos configurados
        if (btnCobrar != null) {
            btnCobrar.setOnAction(event -> onCobrarClick());
        }
        if (btnCancelar != null) {
            btnCancelar.setOnAction(event -> onCancelarClick());
        }
        if (btnQuitar != null) {
            btnQuitar.setOnAction(event -> onQuitarClick());
        }

        System.out.println("✅ Botones configurados con eventos");
    }

    // ===================== CARGA DE PRODUCTOS =====================

    private void cargarProductos() {
        try {
            productosDisponibles = productoDAO.listarProductos();
            System.out.println("📦 Productos cargados: " + productosDisponibles.size());

            gridProductos.getChildren().clear();

            int columna = 0;
            int fila = 0;
            int productosPorFila = 3;
            int productosActivos = 0;

            for (Producto p : productosDisponibles) {
                if (!p.isActivo()) {
                    continue;
                }

                VBox tarjeta = crearTarjetaProducto(p);
                gridProductos.add(tarjeta, columna, fila);
                productosActivos++;

                columna++;
                if (columna >= productosPorFila) {
                    columna = 0;
                    fila++;
                }
            }

            System.out.println("✅ " + productosActivos + " productos activos mostrados");

        } catch (Exception e) {
            System.err.println("❌ Error cargando productos: " + e.getMessage());
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudieron cargar los productos: " + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    private VBox crearTarjetaProducto(Producto producto) {
        VBox tarjeta = new VBox(8);
        tarjeta.setAlignment(Pos.CENTER);
        tarjeta.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 10; " +
                "-fx-padding: 15; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        tarjeta.setPrefWidth(180);
        tarjeta.setPrefHeight(240);

        // Imagen del producto
        ImageView imagen = cargarImagenProducto(producto);
        imagen.setFitWidth(100);
        imagen.setFitHeight(100);
        imagen.setPreserveRatio(true);

        // Nombre
        Label lblNombre = new Label(producto.getNombreProducto());
        lblNombre.setFont(Font.font("System", FontWeight.BOLD, 12));
        lblNombre.setWrapText(true);
        lblNombre.setMaxWidth(160);
        lblNombre.setAlignment(Pos.CENTER);
        lblNombre.setTextFill(Color.web("#333333"));
        lblNombre.setMaxHeight(35);

        // Precio
        Label lblPrecio = new Label(String.format("$%.2f", producto.getPrecio()));
        lblPrecio.setFont(Font.font("System", FontWeight.BOLD, 16));
        lblPrecio.setTextFill(Color.web("#27ae60"));

        // Botón agregar
        Button btnAgregar = new Button("➕ Agregar");
        btnAgregar.setStyle("-fx-background-color: #3498db; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-cursor: hand; " +
                "-fx-background-radius: 5;");
        btnAgregar.setPrefWidth(140);
        btnAgregar.setPrefHeight(28);

        btnAgregar.setOnAction(event -> {
            System.out.println("🛒 Agregando al carrito: " + producto.getNombreProducto());
            agregarAlCarrito(producto);
        });

        tarjeta.getChildren().addAll(imagen, lblNombre, lblPrecio, btnAgregar);

        // Efectos hover
        tarjeta.setOnMouseEntered(e ->
                tarjeta.setStyle("-fx-background-color: #ecf0f1; " +
                        "-fx-background-radius: 10; " +
                        "-fx-padding: 15; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 3);"));

        tarjeta.setOnMouseExited(e ->
                tarjeta.setStyle("-fx-background-color: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-padding: 15; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"));

        return tarjeta;
    }

    private ImageView cargarImagenProducto(Producto producto) {
        ImageView imageView = new ImageView();

        try {
            // Intentar cargar imagen por categoría
            String nombreCategoria = producto.getNombreCategoria()
                    .toLowerCase()
                    .replace(" ", "_")
                    .replace("á", "a")
                    .replace("é", "e")
                    .replace("í", "i")
                    .replace("ó", "o")
                    .replace("ú", "u");

            String rutaImagen = "/imagenes/productos/" + nombreCategoria + ".png";

            Image imagen = new Image(getClass().getResourceAsStream(rutaImagen));

            if (imagen.isError()) {
                // Si falla, usar imagen por defecto
                imagen = new Image(getClass().getResourceAsStream("/imagenes/productos/default.png"));
            }

            imageView.setImage(imagen);

        } catch (Exception e) {
            System.err.println("⚠️ No se pudo cargar imagen para: " + producto.getNombreProducto());
            // Dejar el ImageView vacío o con placeholder
        }

        return imageView;
    }

    // ===================== BÚSQUEDA EN TIEMPO REAL =====================

    private void configurarBusquedaTiempoReal() {
        txtBuscarProducto.textProperty().addListener(
                (observable, valorAnterior, valorNuevo) -> {
                    filtrarProductos(valorNuevo);
                }
        );
    }

    private void filtrarProductos(String textoBusqueda) {
        gridProductos.getChildren().clear();

        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            cargarProductos();
            return;
        }

        List<Producto> productosFiltrados = productoDAO.buscarPorNombre(textoBusqueda.trim());

        int columna = 0;
        int fila = 0;
        int productosPorFila = 3;

        for (Producto p : productosFiltrados) {
            if (!p.isActivo()) continue;

            VBox tarjeta = crearTarjetaProducto(p);
            gridProductos.add(tarjeta, columna, fila);

            columna++;
            if (columna >= productosPorFila) {
                columna = 0;
                fila++;
            }
        }
    }

    // ===================== GESTIÓN DEL CARRITO =====================

    private void agregarAlCarrito(Producto producto) {
        try {
            ItemCarrito itemExistente = buscarEnCarrito(producto.getIdProducto());

            if (itemExistente != null) {
                itemExistente.setCantidad(itemExistente.getCantidad() + 1);
                tablaCarrito.refresh();
                System.out.println("➕ Cantidad incrementada: " + producto.getNombreProducto());
            } else {
                ItemCarrito nuevoItem = new ItemCarrito(
                        producto.getIdProducto(),
                        producto.getNombreProducto(),
                        producto.getPrecio(),
                        1
                );
                carrito.add(nuevoItem);
                System.out.println("✅ Producto agregado: " + producto.getNombreProducto());
            }

            actualizarTotales();

        } catch (Exception e) {
            System.err.println("❌ Error agregando al carrito: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private ItemCarrito buscarEnCarrito(int idProducto) {
        for (ItemCarrito item : carrito) {
            if (item.getIdProducto() == idProducto) {
                return item;
            }
        }
        return null;
    }

    private void actualizarTotales() {
        double subtotal = 0.0;

        for (ItemCarrito item : carrito) {
            subtotal += item.getImporteTotal();
        }

        double impuestos = subtotal * IVA;
        double total = subtotal + impuestos;

        lblSubtotal.setText(String.format("$ %.2f", subtotal));
        lblImpuestos.setText(String.format("$ %.2f", impuestos));
        lblTotal.setText(String.format("$ %.2f", total));

        System.out.println("💰 Totales actualizados - Subtotal: $" + String.format("%.2f", subtotal) +
                " Total: $" + String.format("%.2f", total));
    }

    // ===================== ACCIONES DE BOTONES =====================

    @FXML
    private void onCobrarClick() {
        System.out.println("💳 Iniciando proceso de cobro...");

        try {
            // Validar carrito
            if (carrito.isEmpty()) {
                mostrarAlerta("Carrito Vacío", "Agrega productos antes de cobrar",
                        Alert.AlertType.WARNING);
                return;
            }

            // Preguntar método de pago
            String metodoPago = preguntarMetodoPago();
            if (metodoPago == null) {
                System.out.println("⚠️ Usuario canceló selección de método de pago");
                return;
            }

            System.out.println("💵 Método de pago: " + metodoPago);

            // Calcular totales
            double subtotal = Double.parseDouble(lblSubtotal.getText().replace("$ ", ""));
            double impuestos = Double.parseDouble(lblImpuestos.getText().replace("$ ", ""));
            double total = Double.parseDouble(lblTotal.getText().replace("$ ", ""));

            System.out.println("📊 Totales - Subtotal: " + subtotal + ", IVA: " + impuestos + ", Total: " + total);

            // Crear venta principal
            Venta venta = ventaDAO.crearVenta(idUsuarioActual, metodoPago, subtotal, impuestos, total);

            if (venta == null) {
                mostrarAlerta("Error", "No se pudo registrar la venta en la base de datos",
                        Alert.AlertType.ERROR);
                return;
            }

            System.out.println("✅ Venta creada con ID: " + venta.getIdVenta());

            // Guardar detalles
            boolean exito = true;
            for (ItemCarrito item : carrito) {
                DetalleVenta detalle = detalleVentaDAO.crearDetalleVenta(
                        venta.getIdVenta(),
                        item.getIdProducto(),
                        item.getCantidad(),
                        item.getPrecioUnitario(),
                        item.getImporteTotal()
                );

                if (detalle == null) {
                    exito = false;
                    System.err.println("❌ Error guardando detalle para producto ID: " + item.getIdProducto());
                } else {
                    System.out.println("✅ Detalle guardado: " + item.getNombreProducto());
                }
            }

            if (exito) {
                mostrarAlerta("Venta Exitosa",
                        "Ticket #" + venta.getIdVenta() + "\nTotal: $" + String.format("%.2f", total) +
                                "\nMétodo: " + metodoPago,
                        Alert.AlertType.INFORMATION);

                System.out.println("🎉 Venta procesada exitosamente");

                limpiarVenta();
            } else {
                mostrarAlerta("Advertencia",
                        "La venta se registró pero hubo problemas con algunos detalles",
                        Alert.AlertType.WARNING);
            }

        } catch (Exception e) {
            System.err.println("❌ Error procesando venta: " + e.getMessage());
            e.printStackTrace();
            mostrarAlerta("Error Crítico", "Error al procesar la venta: " + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    private String preguntarMetodoPago() {
        ChoiceDialog<String> dialogo = new ChoiceDialog<>("Efectivo", "Efectivo", "Tarjeta");
        dialogo.setTitle("Método de Pago");
        dialogo.setHeaderText("Seleccione el método de pago:");
        dialogo.setContentText("Método:");

        Optional<String> resultado = dialogo.showAndWait();
        return resultado.orElse(null);
    }

    @FXML
    private void onCancelarClick() {
        if (carrito.isEmpty()) {
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Cancelar Venta");
        confirmacion.setHeaderText("¿Estás seguro?");
        confirmacion.setContentText("Se perderán todos los productos del carrito");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            System.out.println("🗑️ Venta cancelada por usuario");
            limpiarVenta();
        }
    }

    @FXML
    private void onQuitarClick() {
        ItemCarrito seleccionado = tablaCarrito.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta("Sin Selección", "Selecciona un producto del carrito para quitar",
                    Alert.AlertType.WARNING);
            return;
        }

        System.out.println("🗑️ Quitando producto: " + seleccionado.getNombreProducto());
        carrito.remove(seleccionado);
        actualizarTotales();
    }

    private void limpiarVenta() {
        carrito.clear();
        actualizarTotales();
        generarNuevoFolio();
        System.out.println("🧹 Carrito limpiado");
    }

    private void generarNuevoFolio() {
        try {
            Venta ultima = ventaDAO.obtenerUltimaVenta();
            if (ultima != null) {
                int siguienteFolio = ultima.getIdVenta() + 1;
                lblFolio.setText("#" + String.format("%04d", siguienteFolio));
            } else {
                lblFolio.setText("#0001");
            }
        } catch (Exception e) {
            lblFolio.setText("#0000");
        }
    }

    public void setUsuarioActual(int idUsuario) {
        this.idUsuarioActual = idUsuario;
        System.out.println("👤 Usuario actual configurado: ID " + idUsuario);
    }

    // ===================== UTILIDADES =====================

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    // ===================== CLASE INTERNA =====================

    public static class ItemCarrito {
        private int idProducto;
        private String nombreProducto;
        private double precioUnitario;
        private int cantidad;
        private double importeTotal;

        public ItemCarrito(int idProducto, String nombreProducto, double precioUnitario, int cantidad) {
            this.idProducto = idProducto;
            this.nombreProducto = nombreProducto;
            this.precioUnitario = precioUnitario;
            this.cantidad = cantidad;
            calcularImporte();
        }

        public void calcularImporte() {
            this.importeTotal = this.precioUnitario * this.cantidad;
        }

        // Getters y Setters
        public int getIdProducto() { return idProducto; }
        public String getNombreProducto() { return nombreProducto; }
        public double getPrecioUnitario() { return precioUnitario; }
        public int getCantidad() { return cantidad; }
        public double getImporteTotal() { return importeTotal; }

        public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
            calcularImporte();
        }
    }
}