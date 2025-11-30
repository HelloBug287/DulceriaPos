package com.dulceria.pos.modelo;

public class Marca {
    private int idMarca;
    private String nombreMarca;
    private boolean activo;

    public Marca() {
    }

    public Marca(int idMarca, String nombreMarca) {
        this.idMarca = idMarca;
        this.nombreMarca = nombreMarca;
    }

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public String getNombreMarca() {
        return nombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }

    public boolean isActivo() {
        return activo; }

    public void setActivo(boolean activo) {
        this.activo = activo; }
}

