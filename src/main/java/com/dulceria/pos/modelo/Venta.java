package com.dulceria.pos.modelo;

import java.util.Date;

/**
 * Modelo Venta
 */
public class Venta {
    private  int idVenta;
    private  int idUsuario;
    private Date fechaHora;
    private String metodoPago;
    private double subtotal;
    private double impuestos;
    private double total;

    public Venta() {

    }
    // con este contructor lo podemos utilizar para listar las ventas y asi poder obtener las Horas
    public Venta(int idVenta, int id_Usuario, Date fechaHora, String metodoPago, double subtotal, double impuestos, double total) {
        this.idVenta = idVenta;
        this.idUsuario = id_Usuario;
        this.fechaHora = fechaHora;
        this.metodoPago = metodoPago;
        this.subtotal = subtotal;
        this.impuestos = impuestos;
        this.total = total;
    }
    public Venta(int idVenta, int idUsuario, String metodoPago, double subtotal, double impuestos, double total) {
        this.idVenta = idVenta;
        this.idUsuario = idUsuario;
        this.metodoPago = metodoPago;
        this.subtotal = subtotal;
        this.impuestos = impuestos;
        this.total = total;

        //  Creo un nuevo constructor ya que en uno de los metodos de venta DAO si solo utilizo el contructor de arriba me pedira la fecha con este, no se pasa fechaHora porque la BD la crea
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
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
        if (subtotal < 0) this.subtotal = 0.0; else this.subtotal = subtotal;
    }

    public double getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(double impuestos) {
        if (impuestos < 0) this.impuestos = 0.0; else this.impuestos = impuestos;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        if (total < 0) this.total = 0.0; else this.total = total;
    }


}
