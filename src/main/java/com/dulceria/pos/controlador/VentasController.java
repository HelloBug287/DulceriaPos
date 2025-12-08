package com.dulceria.pos.controlador;

import com.dulceria.pos.DAO.ProductoDAO;
import com.dulceria.pos.DAO.VentaDAO;
import com.dulceria.pos.modelo.Producto;
import com.dulceria.pos.modelo.Venta;
import com.dulceria.pos.modelo.DetalleVenta;
import java.util.List;
import java.util.Optional;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.InputStream;

/**
 * Controlador para la vista de Ventas.
 *
 * Responsabilidades principales:
 * - Mostrar tarjetas de productos (carga desde `ProductoDAO`).
 * - Permitir buscar productos en tiempo real.
 * - Gestionar un carrito local (añadir/quitar/cantidad).
 * - Validar stock y procesar cobros: invocar `VentaDAO.crearVentaConProcedure` o fallback transaccional.
 * - Actualizar indicadores (subtotal, impuestos, total) en la UI.
 *
 * Interacción con la BD:
 * - Lee `Productos` mediante `ProductoDAO`.
 * - Crea Ventas/Detalle_Ventas mediante `VentaDAO` (procedure o transaccional).
 * - No escribe stock directamente (los triggers en BD manejan ajustes de stock al insertar detalle).
 */
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

    // VARIABLES DE INSTANCIA

    private ProductoDAO productoDAO;
    private VentaDAO ventaDAO;

    private ObservableList<ItemCarrito> carrito;
    private List<Producto> productosDisponibles;

    private static final double IVA = 0.16;
    private int idUsuarioActual = 1; // Se recibe de principalController

    //  INICIALIZACIÓN

    @FXML
    public void initialize() {
        try {
            // Crear instancias de DAOs
            productoDAO = new ProductoDAO();
            ventaDAO = new VentaDAO();

            // Inicializar carrito
            carrito = FXCollections.observableArrayList();

            // Configurar componentes
            configurarTablaCarrito();

            // Configurar GridPane para que tenga 3 columnas flexibles
            configurarGridProductosColumns(3);

            cargarProductos();
            generarNuevoFolio();
            configurarBusquedaTiempoReal();
            configurarBotones();

            // Inicializar totales
            actualizarTotales();

        } catch (Exception e) {
            System.err.println("Error inicializando VentasController: " + e.getMessage());
            mostrarAlerta("Error", "Error al inicializar el módulo de ventas: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Añadir metodo para configurar columnas del GridPane
    private void configurarGridProductosColumns(int columnas) {
        gridProductos.getColumnConstraints().clear();
        for (int i = 0; i < columnas; i++) {
            javafx.scene.layout.ColumnConstraints cc = new javafx.scene.layout.ColumnConstraints();
            cc.setPercentWidth(100.0 / columnas);
            cc.setHgrow(javafx.scene.layout.Priority.ALWAYS);
            gridProductos.getColumnConstraints().add(cc);
        }
    }

    // CONFIGURACIÓN

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
        // Buscar por botón
        if (btnBuscar != null) {
            btnBuscar.setOnAction(e -> filtrarProductos(txtBuscarProducto.getText()));
        }

    }

    // CARGA DE PRODUCTOS

    /**
     * Carga los productos activos desde el DAO y genera tarjetas en el GridPane.
     * Nota: las tarjetas se crean dinámicamente via `crearTarjetaProducto`.
     */
    private void cargarProductos() {
        try {

            productosDisponibles = productoDAO.listarProductosActivos();

            gridProductos.getChildren().clear();

            int columna = 0;
            int fila = 0;
            int productosPorFila = 3;
            int productosActivos = 0;

            for (Producto p : productosDisponibles) {
                // Ya vienen filtrados por el DAO, pero validación extra
                if (!p.isActivo()) {
                    continue;
                }

                VBox tarjeta = crearTarjetaProducto(p);
                gridProductos.add(tarjeta, columna, fila);
                javafx.scene.layout.GridPane.setHgrow(tarjeta, javafx.scene.layout.Priority.ALWAYS);
                javafx.scene.layout.GridPane.setVgrow(tarjeta, javafx.scene.layout.Priority.ALWAYS);
                productosActivos++;

                columna++;
                if (columna >= productosPorFila) {
                    columna = 0;
                    fila++;
                }
            }

        } catch (Exception e) {
            System.err.println("Error cargando productos: " + e.getMessage());
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudieron cargar los productos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Crea una tarjeta visual (VBox) que representa un producto.
     * Se usa en el GridPane principal.
     */
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
        tarjeta.setMinHeight(220);
        tarjeta.setMaxWidth(Double.MAX_VALUE);
        tarjeta.setMaxHeight(Double.MAX_VALUE);

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
            agregarAlCarrito(producto);
        });

        tarjeta.getChildren().addAll(imagen, lblNombre, lblPrecio, btnAgregar);

        // Asegurar que la tarjeta pueda crecer verticalmente dentro del GridPane

        // Añadir margen para separar
        GridPane.setMargin(tarjeta, new javafx.geometry.Insets(8));

        // Tooltip con precio
        Tooltip.install(tarjeta, new Tooltip("Precio: " + String.format("$ %.2f", producto.getPrecio())));

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
            // Intentar cargar imagen por id del producto con varias extensiones (.png, .jpg, .jpeg)
            String basePath = "/imagenes/productos/" + producto.getIdProducto();
            InputStream is = null;
            String[] exts = new String[]{".png", ".jpg", ".jpeg"};
            for (String ext : exts) {
                is = getClass().getResourceAsStream(basePath + ext);
                if (is != null) break;
            }

            if (is == null) {
                // Intentar cargar imagen por nombre de categoría (probando extensiones)
                String nombreCategoria = (producto.getNombreCategoria() == null) ? "" : producto.getNombreCategoria()
                        .toLowerCase()
                        .replace(" ", "_")
                        .replace("á", "a")
                        .replace("é", "e")
                        .replace("í", "i")
                        .replace("ó", "o")
                        .replace("ú", "u");

                String baseCat = "/imagenes/productos/" + nombreCategoria;
                for (String ext : exts) {
                    is = getClass().getResourceAsStream(baseCat + ext);
                    if (is != null) break;
                }
            }

            if (is == null) {
                // intentar imagen por defecto (png o jpg)
                is = getClass().getResourceAsStream("/imagenes/productos/default.png");
                if (is == null) is = getClass().getResourceAsStream("/imagenes/productos/default.jpg");
            }

            if (is != null) {
                Image imagen = new Image(is);
                imageView.setImage(imagen);
            } else {
                // No hay imágenes en recursos: dejar ImageView vacío
                System.err.println("No se encontró placeholder de imagen en recursos para producto: " + producto.getNombreProducto());
            }

        } catch (Exception e) {
            System.err.println("No se pudo cargar imagen para: " + producto.getNombreProducto());
            // Dejar el ImageView vacío o con placeholder
        }

        return imageView;
    }

    // BÚSQUEDA EN TIEMPO REAL

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


        List<Producto> productosFiltrados = productoDAO.buscarPorNombre(textoBusqueda.trim(), true);

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

    // GESTIÓN DEL CARRITO

    /**
     * Añade un producto al carrito local.
     * Si el producto ya existe en el carrito incrementa su cantidad.
     */
    private void agregarAlCarrito(Producto producto) {
        try {
            ItemCarrito itemExistente = buscarEnCarrito(producto.getIdProducto());

            if (itemExistente != null) {
                itemExistente.setCantidad(itemExistente.getCantidad() + 1);
                tablaCarrito.refresh();
            } else {
                ItemCarrito nuevoItem = new ItemCarrito(
                        producto.getIdProducto(),
                        producto.getNombreProducto(),
                        producto.getPrecio(),
                        1
                );
                carrito.add(nuevoItem);
            }

            actualizarTotales();

        } catch (Exception e) {
            System.err.println("Error agregando al carrito: " + e.getMessage());
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
    }

    // ACCIONES DE BOTONES

    @FXML
    private void onCobrarClick() {

        try {
            // Validar carrito
            if (carrito.isEmpty()) {
                mostrarAlerta("Carrito Vacío", "Agrega productos antes de cobrar",Alert.AlertType.WARNING);
                return;
            }

            // Preguntar metodo de pago
            String metodoPago = preguntarMetodoPago();
            if (metodoPago == null) {
                return;
            }

            // Calcular totales de forma confiable a partir del carrito (no parsear labels)
            double subtotal = 0.0;
            for (ItemCarrito item : carrito) {
                subtotal += item.getImporteTotal();
            }
            double impuestos = subtotal * IVA;
            double total = subtotal + impuestos;


            // VALIDACIÓN PREVIA: verificar stock disponible para cada item en el carrito
            for (ItemCarrito item : carrito) {
                Producto prod = productoDAO.buscarPorId(item.getIdProducto());
                if (prod == null) {
                    mostrarAlerta("Error", "Producto no existe: ID " + item.getIdProducto(), Alert.AlertType.ERROR);
                    return;
                }
                if (prod.getStock() < item.getCantidad()) {
                    mostrarAlerta("Stock insuficiente", "Stock insuficiente para '" + prod.getNombreProducto() + "' (Disponible: " + prod.getStock() + ", Pedido: " + item.getCantidad() + ")", Alert.AlertType.WARNING);
                    return;
                }
            }

            // Construir JSON de items para el procedimiento almacenado
            String itemsJson = buildItemsJson();

            // Crear venta + detalles en la BD usando el procedimiento almacenado (transaccional en BD)
            Venta venta = ventaDAO.crearVentaConProcedure(idUsuarioActual, metodoPago, subtotal, impuestos, total, itemsJson);

            if (venta == null) {
                String detalle = ventaDAO.getLastError();
                if (detalle == null || detalle.trim().isEmpty()) {
                    detalle = "No se pudo registrar la venta en la base de datos.";
                }
                // Intentar fallback con transacción JDBC
                mostrarAlerta("Aviso", "Error al insertar con procedimiento: " + detalle + "\nIntentando fallback transaccional...", Alert.AlertType.INFORMATION);

                // Construir lista de DetalleVenta desde el carrito
                List<DetalleVenta> detalles = new java.util.ArrayList<>();
                for (ItemCarrito it : carrito) {
                    DetalleVenta d = new DetalleVenta(0, 0, it.getIdProducto(), it.getCantidad(), it.getPrecioUnitario(), it.getImporteTotal());
                    detalles.add(d);
                }

                Venta fallback = ventaDAO.crearVentaTransaccional(idUsuarioActual, metodoPago, subtotal, impuestos, total, detalles);
                if (fallback == null) {
                    String detalle2 = ventaDAO.getLastError();
                    if (detalle2 == null || detalle2.trim().isEmpty()) detalle2 = "Error desconocido en fallback.";
                    mostrarAlerta("Error", "No se pudo registrar la venta (fallback): " + detalle2, Alert.AlertType.ERROR);
                    return;
                } else {
                    venta = fallback; // continuar con éxito
                }
            }

            // Si llegamos aquí, el procedimiento y los triggers en BD deberían haber creado los detalles y actualizado stock.
            mostrarAlerta("Venta Exitosa",
                    "Ticket #" + venta.getIdVenta() + "\nTotal: $" + String.format("%.2f", total) +
                            "\nMétodo: " + metodoPago,
                    Alert.AlertType.INFORMATION);


            // Generar ticket PDF automáticamente
            // (Generación de ticket deshabilitada) - funcionalidad removida temporalmente.

            limpiarVenta();

        } catch (Exception e) {
            System.err.println("❌ Error procesando venta: " + e.getMessage());

            mostrarAlerta("Error Crítico", "Error al procesar la venta: " + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    /**
     * Construye un JSON simple (array) con los items del carrito para enviar al procedimiento almacenado.
     * Formato: [{"idProducto":1,"cantidad":2.0,"precio":10.5}, ...]
     * No requiere librerías externas.
     */
    private String buildItemsJson() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < carrito.size(); i++) {
            ItemCarrito it = carrito.get(i);
            sb.append('{');
            sb.append("\"idProducto\":").append(it.getIdProducto()).append(',');
            sb.append("\"cantidad\":").append(it.getCantidad()).append(',');
            sb.append("\"precio\":").append(it.getPrecioUnitario());
            sb.append('}');
            if (i < carrito.size() - 1) sb.append(',');
        }
        sb.append(']');
        return sb.toString();
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

        carrito.remove(seleccionado);
        actualizarTotales();
    }

    private void limpiarVenta() {
        carrito.clear();
        actualizarTotales();
        generarNuevoFolio();
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
    }

    // UTILIDADES

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    //  CLASE INTERNA

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
