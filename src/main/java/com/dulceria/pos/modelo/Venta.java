package com.dulceria.pos.modelo;

import java.util.Date;

public class Venta {
    private  int idVenta;
    private  int id_Usuario;
    private Date fechaHora;
    private String metodoPago;
    private double subtotal;
    private double impuestos;
    private double total;

    public Venta() {

    }

    public Venta(int idVenta, int id_Usuario, Date fechaHora, String metodoPago, double subtotal, double impuestos, double total) {
        this.idVenta = idVenta;
        this.id_Usuario = id_Usuario;
        this.fechaHora = fechaHora;
        this.metodoPago = metodoPago;
        this.subtotal = subtotal;
        this.impuestos = impuestos;
        this.total = total;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getId_Usuario() {
        return id_Usuario;
    }

    public void setId_Usuario(int id_Usuario) {
        this.id_Usuario = id_Usuario;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(double impuestos) {
        this.impuestos = impuestos;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
