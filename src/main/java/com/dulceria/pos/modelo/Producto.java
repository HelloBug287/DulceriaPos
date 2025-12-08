package com.dulceria.pos.modelo;

/**
 * Modelo Producto: representa un producto en el sistema.
 */
public class Producto {
    private int idProducto;
    private int idCategoria;
    private int idMarca;
    private String nombreProducto;
    private double precio;
    private double stock;
    private String unidadMedida;
    private boolean activo;

    private String nombreCategoria;
    private String nombreMarca;

    public Producto() {
    }

    public Producto(int idProducto, int idCategoria, int idMarca, String nombreProducto,double precio, double stock, String unidadMedida, boolean activo) {
        this.idProducto = idProducto;
        this.idCategoria = idCategoria;
        this.idMarca = idMarca;
        this.nombreProducto = nombreProducto;
        this.precio = precio;
        this.stock = stock;
        this.unidadMedida = unidadMedida;
        this.activo = activo;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        if (nombreProducto == null) {
            this.nombreProducto = null;
            return;
        }
        this.nombreProducto = nombreProducto.trim();
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        if (stock < 0) {
            this.stock = 0.0;
        } else {
            this.stock = stock;
        }
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getNombreMarca() {
        return nombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        if (precio < 0) {
            this.precio = 0.0;
        } else {
            this.precio = precio;
        }
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }


}
