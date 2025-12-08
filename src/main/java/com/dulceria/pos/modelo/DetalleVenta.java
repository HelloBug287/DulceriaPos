package com.dulceria.pos.modelo;

/**
 * Modelo DetalleVenta
 */
public class DetalleVenta {
    private int idDetalleVenta;
    private int idVenta;
    private int idProducto;
    private double cantidad;
    private double precioUnitarioCobrado;
    private double importeTotal;

    public DetalleVenta() {
    }

    public DetalleVenta(int idDetalleVenta, int idVenta, int idProducto, double cantidad, double precioUnitarioCobrado, double importeTotal) {
        this.idDetalleVenta = idDetalleVenta;
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitarioCobrado = precioUnitarioCobrado;
        this.importeTotal = importeTotal;
    }

    public int getIdDetalleVenta() {
        return idDetalleVenta;
    }

    public void setIdDetalleVenta(int idDetalleVenta) {
        this.idDetalleVenta = idDetalleVenta;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        if (cantidad < 0) {
            this.cantidad = 0.0;
        } else {
            this.cantidad = cantidad;
        }
    }

    public double getPrecioUnitarioCobrado() {
        return precioUnitarioCobrado;
    }

    public void setPrecioUnitarioCobrado(double precioUnitarioCobrado) {
        if (precioUnitarioCobrado < 0) {
            this.precioUnitarioCobrado = 0.0;
        } else {
            this.precioUnitarioCobrado = precioUnitarioCobrado;
        }
    }

    public double getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(double importeTotal) {
        this.importeTotal = importeTotal;
    }


}
