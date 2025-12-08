package com.dulceria.pos.controlador;

import com.dulceria.pos.DAO.VentaDAO;
import com.dulceria.pos.DAO.ProductoDAO;
import com.dulceria.pos.DAO.CategoriaDAO;
import com.dulceria.pos.modelo.Venta;
import com.dulceria.pos.modelo.Producto;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.Cursor;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.util.Date;
import java.util.List;

/**
 * Controlador para la vista de Inicio / Dashboard.
 *
 * Responsabilidades:
 * Mostrar indicadores clave total ventas del día, número de transacciones y alertas de stock.
 * - Renderizar gráfica de ventas por categoría (datos obtenidos del DAO y la vista en BD).
 *
 * La variable stockMinimo define el umbral para considerar un producto "en alerta".
 */
public class InicioController {

    //Inyeccion de componentes FXML

    @FXML private Label lblVentasDia;
    @FXML private Label lblNumVentas;
    @FXML private Label lblStockBajo;
    @FXML private BarChart<String, Number> chartVentas;

    // Variables de Instancia

    private VentaDAO ventaDAO;
    private ProductoDAO productoDAO;
    private CategoriaDAO categoriaDAO;

    //variable para definir cuando mostraremos cuando un producto lance alerta de stock (en este caso 10)
    private static final double stockMinimo = 10.0;

    @FXML
    public void initialize() {
        ventaDAO = new VentaDAO();
        productoDAO = new ProductoDAO();
        categoriaDAO = new CategoriaDAO();

        cargarEstadisticas();
        cargarGrafica();

        // Hacer label clicable, navegar al panel de Productos mostrando solo stock bajo
        if (lblStockBajo != null) {
            lblStockBajo.setCursor(Cursor.HAND);
            lblStockBajo.setOnMouseClicked(e -> {
                abrirProductosStockBajo();
            });
        }
    }

    // Metodos que utilizaremos para cargarse automaticamente cuando se abra la vista

    private void cargarEstadisticas() {
        // Usar metodos optimizados del DAO para evitar cargar todas las ventas en memoria
        double totalVentas = ventaDAO.obtenerTotalVentasHoy();
        int numTransacciones = ventaDAO.contarVentasHoy();

        // Mostrar en labels
        lblVentasDia.setText(String.format("$ %.2f", totalVentas));
        lblNumVentas.setText(String.valueOf(numTransacciones));

        // Contar productos con stock bajo
        int productosStockBajo = contarProductosStockBajo();
        lblStockBajo.setText(String.valueOf(productosStockBajo));
    }

    /**
     * Cuenta productos cuyo stock está por debajo del umbral stockMinimo y que estén activos.
     * Retorna el número entero de productos en alerta.
     */
    private int contarProductosStockBajo() {
        List<Producto> productos = productoDAO.listarProductos();
        if (productos == null) return 0;
        int contador = 0;

        for (Producto p : productos) {
            if (p == null) continue;
            if (p.getStock() < stockMinimo && p.isActivo()) {
                contador++;
            }
        }

        return contador;
    }


    private void cargarGrafica() {
        // Limpiar gráfica anterior
        chartVentas.getData().clear();

        // Crear serie de datos
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Ventas por Categoría (Hoy)");

        // Obtener datos de ventas por categoría desde el DAO (consulta optimizada en BD)
        List<Object[]> ventasPorCategoria = ventaDAO.obtenerVentasPorCategoria();

        if (ventasPorCategoria == null || ventasPorCategoria.isEmpty()) {
            // No hay ventas hoy: mostrar serie vacía
            chartVentas.getData().add(serie);
            return;
        }

        // Cada elemento es Object[]{categoria, total}
        for (Object[] row : ventasPorCategoria) {
            String categoria = row[0] != null ? row[0].toString() : "Sin categoría";
            Number total = 0;
            if (row[1] instanceof Number) {
                total = (Number) row[1];
            } else if (row[1] != null) {
                try {
                    total = Double.parseDouble(row[1].toString());
                } catch (NumberFormatException ex) {
                    total = 0;
                }
            }
            serie.getData().add(new XYChart.Data<>(categoria, total));
        }

        chartVentas.getData().add(serie);
    }

    // ACCIONES

    @FXML
    private void onActualizarClick() {
        cargarEstadisticas();
        cargarGrafica();
    }

    /**
     * Intenta cargar Productos.fxml en el centro del BorderPane principal y aplicar filtro de stock bajo.
     * Si no encuentra el contenedor principal, abre una ventana nueva con la vista de productos filtrada.
     */
    private void abrirProductosStockBajo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dulceria/pos/Productos.fxml"));
            Parent productosRoot = loader.load();

            // Obtener el controlador de productos para aplicar filtro
            Object controller = loader.getController();

            // Intentar insertar en el center del BorderPane principal
            if (lblStockBajo != null && lblStockBajo.getScene() != null) {
                Scene scene = lblStockBajo.getScene();
                Object root = scene.getRoot();
                if (root instanceof BorderPane) {
                    BorderPane bp = (BorderPane) root;
                    // El centro en Principal.fxml es un StackPane (contentArea)
                    if (bp.getCenter() instanceof StackPane) {
                        StackPane content = (StackPane) bp.getCenter();
                        content.getChildren().clear();
                        content.getChildren().add(productosRoot);

                        // Si el controlador es ProductosController, aplicar filtro por stock bajo
                        if (controller instanceof ProductosController) {
                            ((ProductosController) controller).filtrarPorStockBajo(stockMinimo);
                        }
                        return;
                    }
                }
            }

            Stage stage = new Stage();
            stage.setTitle("Productos - Stock Bajo");
            stage.setScene(new Scene(productosRoot));
            stage.show();

            if (controller instanceof ProductosController) {
                ((ProductosController) controller).filtrarPorStockBajo(stockMinimo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}