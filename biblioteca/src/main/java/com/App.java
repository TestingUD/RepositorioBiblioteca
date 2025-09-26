package com;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        int modulo;
        do {
            System.out.println("\n==============================================");
            System.out.println("   📚  SISTEMA DE GESTIÓN DE BIBLIOTECA  ");
            System.out.println("==============================================");
            System.out.println("Seleccione una opción:");
            System.out.println(" 1.  Gestionar Libros");
            System.out.println(" 2.  Gestionar Usuarios");
            System.out.println(" 3.  Gestionar Préstamos");
            System.out.println(" 4.  Salir");
            System.out.print("\n👉 Opción: ");
            modulo = scanner.nextInt();

            switch (modulo) {
                case 1 -> new ModuloLibros();
                case 2 -> new ModuloUsuarios();
                case 3 -> new ModuloPrestamos();
                case 4 -> System.out.println("\n Saliendo del sistema...");
                default -> System.out.println("\n  Opción no válida. Intente de nuevo.");
            }
        } while (modulo != 4);
    }
}
