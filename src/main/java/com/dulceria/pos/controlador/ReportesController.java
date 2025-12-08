package com.dulceria.pos.controlador;

import com.dulceria.pos.DAO.VentaDAO;
import com.dulceria.pos.DAO.UsuarioDAO;
import com.dulceria.pos.modelo.Venta;
import com.dulceria.pos.modelo.Usuario;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Controlador para la sección de Reportes.
 *
 * Responsabilidades:
 * - Permitir seleccionar un rango de fechas y tipo de reporte.
 * - Consultar ventas en el rango usando `VentaDAO` y mostrar resultados en una tabla.
 * - Calcular totales y permitir filtrado por metodo de pago.
 */
public class ReportesController {

    // ===================== INYECCIÓN FXML =====================

    @FXML private DatePicker dpDesde;
    @FXML private DatePicker dpHasta;
    @FXML private ComboBox<String> comboTipoReporte;

    @FXML private TableView<Venta> tablaReportes;
    @FXML private TableColumn<Venta, String> colFecha;
    @FXML private TableColumn<Venta, String> colFolio;
    @FXML private TableColumn<Venta, String> colCajero;
    @FXML private TableColumn<Venta, String> colMetodo;
    @FXML private TableColumn<Venta, String> colTotal;

    @FXML private Label lblTotalPeriodo;

    // VARIABLES DE INSTANCIA

    private VentaDAO ventaDAO;
    private UsuarioDAO usuarioDAO;
    private List<Usuario> listaUsuarios;

    // INICIALIZACIÓN

    @FXML
    public void initialize() {
        ventaDAO = new VentaDAO();
        usuarioDAO = new UsuarioDAO();

        // Cargar usuarios para mostrar nombres
        listaUsuarios = usuarioDAO.listarUsuarios();

        configurarTabla();
        cargarTiposReporte();

        // Establecer fechas por defecto
        dpDesde.setValue(LocalDate.now());
        dpHasta.setValue(LocalDate.now());
    }

    // CONFIGURACIÓN

    private void configurarTabla() {
        // Columna Fecha - formateada
        colFecha.setCellValueFactory(cellData -> {
            Venta v = cellData.getValue();
            if (v.getFechaHora() != null) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
                return new SimpleStringProperty(sdf.format(v.getFechaHora()));
            }
            return new SimpleStringProperty("");
        });

        // Columna Folio
        colFolio.setCellValueFactory(cellData -> {
            return new SimpleStringProperty("#" + cellData.getValue().getIdVenta());
        });

        // Columna Cajero - buscar nombre por ID
        colCajero.setCellValueFactory(cellData -> {
            // getIdUsuario() es el getter estandarizado en Venta.java
            int idUsuario = cellData.getValue().getIdUsuario();
            String nombre = obtenerNombreUsuario(idUsuario);
            return new SimpleStringProperty(nombre);
        });

        // Columna Metodo de Pago
        colMetodo.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getMetodoPago());
        });

        // Columna Total - formateada
        colTotal.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(String.format("$ %.2f", cellData.getValue().getTotal()));
        });
    }

    private void cargarTiposReporte() {
        comboTipoReporte.getItems().clear();
        comboTipoReporte.getItems().addAll(
                "Ventas Generales",
                "Ventas por Efectivo",
                "Ventas por Tarjeta"
        );
        comboTipoReporte.setValue("Ventas Generales");
    }

    // MÉTODOS AUXILIARES

    private String obtenerNombreUsuario(int idUsuario) {
        if (listaUsuarios == null) return "Desconocido";
        for (Usuario u : listaUsuarios) {
            if (u.getIdUsuario() == idUsuario) {
                return u.getNombreCompleto();
            }
        }
        return "Desconocido";
    }

    private Date convertirADate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    // ACCIONES

    /**
     * Acción vinculada al botón "Generar". Valida fechas, consulta ventas y muestra resultados.
     */
    @FXML
    private void onGenerarClick() {
        // Validar fechas
        if (dpDesde.getValue() == null || dpHasta.getValue() == null) {
            mostrarAlerta("Cuidado", "Seleccione ambas fechas", Alert.AlertType.WARNING);
            return;
        }

        // Validar que fecha inicio no sea mayor que fecha fin
        if (dpDesde.getValue().isAfter(dpHasta.getValue())) {
            mostrarAlerta("Error", "La fecha inicial no puede ser mayor a la fecha final", Alert.AlertType.ERROR);
            return;
        }

        // Convertir fechas
        Date fechaInicio = convertirADate(dpDesde.getValue());
        Date fechaFin = convertirADate(dpHasta.getValue());

        // Obtener ventas del periodo
        List<Venta> ventas = ventaDAO.listarVentasPorFecha(fechaInicio, fechaFin);

        if (ventas == null || ventas.isEmpty()) {
            tablaReportes.getItems().clear();
            lblTotalPeriodo.setText("$ 0.00");
            mostrarAlerta("Información", "No hay ventas en el periodo seleccionado", Alert.AlertType.INFORMATION);
            return;
        }

        // Filtrar por tipo de reporte si es necesario
        String tipoReporte = comboTipoReporte.getValue();
        if (tipoReporte != null && !tipoReporte.equals("Ventas Generales")) {
            ventas = filtrarPorMetodoPago(ventas, tipoReporte);
        }

        // Mostrar en tabla
        tablaReportes.getItems().clear();
        tablaReportes.getItems().addAll(ventas);

        // Calcular total del periodo
        double total = 0.0;
        for (Venta v : ventas) {
            total += v.getTotal();
        }
        lblTotalPeriodo.setText(String.format("$ %.2f", total));
    }

    private List<Venta> filtrarPorMetodoPago(List<Venta> ventas, String tipoReporte) {
        String metodo = "";

        switch (tipoReporte) {
            case "Ventas por Efectivo":
                metodo = "Efectivo";
                break;
            case "Ventas por Tarjeta":
                metodo = "Tarjeta";
                break;
            default:
                return ventas;
        }

        final String metodoFinal = metodo;
        ventas.removeIf(v -> !v.getMetodoPago().equalsIgnoreCase(metodoFinal));

        return ventas;
    }

    // UTILIDADES

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}