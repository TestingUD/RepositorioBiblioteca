package com;

import com.gestionUsuarios.Gestion_de_Usuarios;
// este modulo trae los recursos de gestionUsuarios
public class ModuloUsuarios {

    public ModuloUsuarios() {
        Gestion_de_Usuarios gestion = new Gestion_de_Usuarios();
        gestion.mostrarMenu();
    }

}
