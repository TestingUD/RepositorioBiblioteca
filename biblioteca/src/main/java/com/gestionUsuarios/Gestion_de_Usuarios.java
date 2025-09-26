package com.gestionUsuarios;

import java.util.Scanner;

public class Gestion_de_Usuarios {

    private Repositorio repositorio = new Repositorio();
    private Scanner scanner = new Scanner(System.in);

    public void mostrarMenu() {
        int opcion;
        do {
            System.out.println("\n=== Gestión de Usuarios ===");
            System.out.println("1. Crear usuario");
            System.out.println("2. Leer usuarios");
            System.out.println("3. Actualizar usuario");
            System.out.println("4. Eliminar usuario");
            System.out.println("5. Volver al menú principal");
            System.out.print("Elige una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> crearUsuario();
                case 2 -> leerUsuarios();
                case 3 -> actualizarUsuario();
                case 4 -> eliminarUsuario();
                case 5 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 5);
    }

    private void crearUsuario() {
        System.out.println("\n--- Crear Usuario ---");
        System.out.print("Tipo de usuario (estudiante, maestro): ");
        String tipo = scanner.nextLine();
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Código institucional: ");
        String codigo = scanner.nextLine();
        System.out.print("Correo: ");
        String correo = scanner.nextLine();
        System.out.print("Universidad: ");
        String universidad = scanner.nextLine();
        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine();
        System.out.print("Dirección: ");
        String direccion = scanner.nextLine();

        Usuario usuario = new Usuario(tipo, nombre, codigo, correo, universidad, telefono, direccion);
        repositorio.crearUsuario(usuario);
        System.out.println("Usuario creado con éxito.");
    }

    private void leerUsuarios() {
        System.out.println("\n--- Leer Usuarios ---");
        System.out.println("1. Ver todos");
        System.out.println("2. Buscar por código");
        System.out.print("Elige una opción: ");
        int opcion = scanner.nextInt();
        scanner.nextLine();

        if (opcion == 1) {
            System.out.println(
                    "\nTIPO       | NOMBRE          | CÓDIGO       | CORREO                    | UNIVERSIDAD     | TELÉFONO   | DIRECCIÓN");
            System.out.println(
                    "---------------------------------------------------------------------------------------------------------------");
            for (Usuario u : repositorio.obtenerTodos().values()) {
                System.out.println(u);
            }
        } else if (opcion == 2) {
            System.out.print("Ingrese el código del usuario: ");
            String codigo = scanner.nextLine();
            Usuario usuario = repositorio.obtenerUsuario(codigo);
            if (usuario != null) {
                System.out.println("\n" + usuario);
            } else {
                System.out.println(" Usuario no encontrado.");
            }
        }
    }

    private void actualizarUsuario() {
        System.out.print("\nIngrese el código del usuario a actualizar: ");
        String codigo = scanner.nextLine();
        Usuario existente = repositorio.obtenerUsuario(codigo);

        if (existente != null) {
            System.out.print("Nuevo nombre: ");
            String nombre = scanner.nextLine();
            existente.setNombre(nombre);

            repositorio.actualizarUsuario(codigo, existente);
            System.out.println("Usuario actualizado.");
        } else {
            System.out.println("Usuario no encontrado.");
        }
    }

    private void eliminarUsuario() {
        System.out.print("\nIngrese el código del usuario a eliminar: ");
        String codigo = scanner.nextLine();
        repositorio.eliminarUsuario(codigo);
        System.out.println("Usuario eliminado.");
    }

}
