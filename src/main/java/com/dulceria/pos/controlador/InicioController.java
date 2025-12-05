package com.dulceria.pos.controlador;

import com.dulceria.pos.DAO.VentaDAO;
import com.dulceria.pos.DAO.ProductoDAO;
import com.dulceria.pos.DAO.CategoriaDAO;
import com.dulceria.pos.modelo.Venta;
import com.dulceria.pos.modelo.Producto;
import com.dulceria.pos.modelo.Categoria;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.util.Date;
import java.util.List;

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
    }

    // Metodos que utilizaremos para cargarse automaticamente cuando se abra la vista

    private void cargarEstadisticas() {
        // Obtener fecha de hoy
        Date hoy = new Date();

        // Ventas del día
        List<Venta> ventasHoy = ventaDAO.listarVentasPorFecha(hoy, hoy);

        double totalVentas = 0.0;
        int numTransacciones = 0;

        if (ventasHoy != null) {
            numTransacciones = ventasHoy.size();
            for (Venta v : ventasHoy) {
                totalVentas += v.getTotal();
            }
        }

        // Mostrar en labels
        lblVentasDia.setText(String.format("$ %.2f", totalVentas));
        lblNumVentas.setText(String.valueOf(numTransacciones));

        // Contar productos con stock bajo
        int productosStockBajo = contarProductosStockBajo();
        lblStockBajo.setText(String.valueOf(productosStockBajo));
    }

    private int contarProductosStockBajo() {
        List<Producto> productos = productoDAO.listarProductos();
        int contador = 0;

        for (Producto p : productos) {
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
        serie.setName("Ventas por Categoría");

        // Obtener categorías
        List<Categoria> categorias = categoriaDAO.listarCategorias();
        List<Producto> productos = productoDAO.listarProductos();

        // Para cada categoría, contar productos (como ejemplo simple)
        // En un sistema real, esto sería ventas por categoría
        for (Categoria cat : categorias) {
            int cantidad = 0;
            for (Producto p : productos) {
                if (p.getIdCategoria() == cat.getIdCategoria()) {
                    cantidad++;
                }
            }
            serie.getData().add(new XYChart.Data<>(cat.getNombreCategoria(), cantidad));
        }

        chartVentas.getData().add(serie);
    }

    // ACCIONES

    @FXML
    private void onActualizarClick() {
        cargarEstadisticas();
        cargarGrafica();
    }
}