package com.dulceria.pos.modelo;

/**
 * Modelo Usuario
 */
public class Usuario {


    private int idUsuario;
    private int idRol;
    private String username;
    private String password;
    private String nombreCompleto;
    private boolean activo;
    private String nombreRol;

    public Usuario() {
    }


    public Usuario(int idUsuario, int idRol, String username, String password, String nombreCompleto, boolean activo) {
        this.idUsuario = idUsuario;
        this.idRol = idRol;
        this.username = username;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
        this.activo = activo;
    }



    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = (username == null) ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password; // no se aplica hashing aquí; hacerlo en capa de seguridad
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = (nombreCompleto == null) ? null : nombreCompleto.trim();
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }


}