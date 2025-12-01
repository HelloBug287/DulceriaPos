package com.dulceria.pos.modelo;

public class Marca {
    private int idMarca;
    private String nombreMarca;
    private boolean activo;

    public Marca() {
    }

    public Marca(int idMarca, String nombreMarca,boolean activo) {
        this.idMarca = idMarca;
        this.nombreMarca = nombreMarca;
        this.activo = activo;
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

