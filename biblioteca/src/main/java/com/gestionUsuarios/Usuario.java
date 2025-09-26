package com.gestionUsuarios;

public class Usuario {

    private String tipoUsuario;
    private String nombre;
    private String codigoInstitucional;
    private String correo;
    private String universidad;
    private String telefono;
    private String direccion;

    public Usuario(String tipoUsuario, String nombre, String codigoInstitucional,
            String correo, String universidad, String telefono, String direccion) {

        this.tipoUsuario = tipoUsuario;
        this.nombre = nombre;
        this.codigoInstitucional = codigoInstitucional;
        this.correo = correo;
        this.universidad = universidad;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigoInstitucional() {
        return codigoInstitucional;
    }

    public void setCodigoInstitucional(String codigoInstitucional) {
        this.codigoInstitucional = codigoInstitucional;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUniversidad() {
        return universidad;
    }

    public void setUniversidad(String universidad) {
        this.universidad = universidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return String.format("%-10s | %-15s | %-12s | %-25s | %-15s | %-10s | %-20s",
                tipoUsuario, nombre, codigoInstitucional, correo, universidad, telefono, direccion);
    }

}
