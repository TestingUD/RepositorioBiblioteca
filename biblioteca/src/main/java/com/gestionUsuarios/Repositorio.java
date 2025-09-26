package com.gestionUsuarios;

import java.util.HashMap;

public class Repositorio {
    private HashMap<String, Usuario> usuarios = new HashMap<>();

    public void crearUsuario(Usuario usuario) {
        usuarios.put(usuario.getCodigoInstitucional(), usuario);
    }

    public Usuario obtenerUsuario(String codigo) {
        return usuarios.get(codigo);
    }

    public HashMap<String, Usuario> obtenerTodos() {
        return usuarios;
    }

    public void actualizarUsuario(String codigo, Usuario usuario) {
        if (usuarios.containsKey(codigo)) {
            usuarios.put(codigo, usuario);
        }
    }

    public void eliminarUsuario(String codigo) {
        usuarios.remove(codigo);
    }

}
