package com.dulceria.pos.modelo;

public class Categoria {
    private int idCategoria;
    private String nombreCategoria;
    private boolean activo;

    public Categoria(int idCategoria, String nombreCategoria,boolean activo) {
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
        this.activo = activo;
    }

    public Categoria() {
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public boolean isActivo() {
        return activo; }

    public void setActivo(boolean activo) {
        this.activo = activo; }
}
