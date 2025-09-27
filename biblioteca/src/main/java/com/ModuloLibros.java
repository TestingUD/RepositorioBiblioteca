package com;

import com.gestionLibros.*;
import java.util.Scanner;

public class ModuloLibros {
    private final Scanner scanner = new Scanner(System.in);
    private final ModuloLibro gestor = new ModuloLibro();

    public ModuloLibros() {
        mostrarMenu();
    }

    private void mostrarMenu() {
        int opcion;
        do {
            System.out.println("\n📚 GESTIÓN DE LIBROS");
            System.out.println("1. Agregar libro");
            System.out.println("2. Listar libros");
            System.out.println("3. Buscar libro por título");
            System.out.println("4. Eliminar libro");
            System.out.println("5. Prestar libro");
            System.out.println("6. Devolver libro");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // limpiar buffer

            switch (opcion) {
                case 1 -> agregarLibro();
                case 2 -> listarLibros();
                case 3 -> buscarLibro();
                case 4 -> eliminarLibro();
                case 5 -> prestarLibro();
                case 6 -> devolverLibro();
                case 0 -> System.out.println("↩️ Volviendo al menú principal...");
                default -> System.out.println("❌ Opción inválida.");
            }
        } while (opcion != 0);
    }

    private void agregarLibro() {
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Autor: ");
        String autor = scanner.nextLine();
        System.out.print("Año: ");
        int anio = scanner.nextInt();
        scanner.nextLine();
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();

        Libro libro = gestor.crearLibro(titulo, autor, anio, isbn);
        System.out.println("✅ Libro agregado: " + libro);
    }

    private void listarLibros() {
        if (gestor.estaVacia()) {
            System.out.println("⚠️ No hay libros registrados.");
        } else {
            System.out.println("📖 Lista de libros:");
            for (Libro libro : gestor.listarLibros()) {
                System.out.println(libro);
            }
        }
    }

    private void buscarLibro() {
        System.out.print("Ingrese el título a buscar: ");
        String titulo = scanner.nextLine();
        gestor.buscarPorTitulo(titulo)
              .ifPresentOrElse(
                  l -> System.out.println("🔎 Encontrado: " + l),
                  () -> System.out.println("❌ Libro no encontrado.")
              );
    }

    private void eliminarLibro() {
        System.out.print("Ingrese el título a eliminar: ");
        String titulo = scanner.nextLine();
        if (gestor.eliminarPorTitulo(titulo)) {
            System.out.println("🗑️ Libro eliminado.");
        } else {
            System.out.println("❌ No se encontró el libro.");
        }
    }

    private void prestarLibro() {
        System.out.print("Ingrese el título del libro a prestar: ");
        String titulo = scanner.nextLine();
        if (gestor.prestarLibro(titulo)) {
            System.out.println("📕 Libro prestado con éxito.");
        } else {
            System.out.println("❌ No se pudo prestar el libro (no existe o ya está prestado).");
        }
    }

    private void devolverLibro() {
        System.out.print("Ingrese el título del libro a devolver: ");
        String titulo = scanner.nextLine();
        if (gestor.devolverLibro(titulo)) {
            System.out.println("📗 Libro devuelto con éxito.");
        } else {
            System.out.println("❌ No se pudo devolver el libro (no existe o ya estaba disponible).");
        }
    }
}
