package com.dulceria.pos.modelo;

/**
 * Modelo Proveedor
 */
public class Proveedor {
    private int idProveedor;
    private String nombreProveedor;
    private String telefono;
    private String email;
    private boolean activo;

    public Proveedor() {
    }

    public Proveedor(int idProveedor, String nombreProveedor, String telefono, String email, boolean activo) {
        this.idProveedor = idProveedor;
        this.nombreProveedor = nombreProveedor;
        this.telefono = telefono;
        this.email = email;
        this.activo = activo;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        if (nombreProveedor == null) {
            this.nombreProveedor = null;
            return;
        }
        this.nombreProveedor = nombreProveedor.trim();
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = (email == null) ? null : email.trim();
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }


}
