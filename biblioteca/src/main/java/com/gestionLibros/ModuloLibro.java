package com.gestionLibros;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Módulo de gestión de libros.
 * Contiene la clase interna Libro y operaciones sobre la colección en memoria.
 */
public class ModuloLibro {

    private final List<Libro> libros = new ArrayList<>();

    /** Crea y añade un libro a la colección. */
    public Libro crearLibro(String titulo, String autor, int anio, String isbn) {
        Libro l = new Libro(titulo, autor, anio, isbn);
        libros.add(l);
        return l;
    }

    /** Lista todos los libros (copia de la lista interna). */
    public List<Libro> listarLibros() {
        return new ArrayList<>(libros);
    }

    /** Busca el primer libro por título (ignora mayúsculas/minúsculas). */
    public Optional<Libro> buscarPorTitulo(String titulo) {
        if (titulo == null) return Optional.empty();
        String clave = titulo.trim().toLowerCase();
        return libros.stream()
                .filter(l -> l.getTitulo() != null && l.getTitulo().trim().toLowerCase().equals(clave))
                .findFirst();
    }

    /** Elimina el primer libro que coincida con el título. Retorna true si se eliminó. */
    public boolean eliminarPorTitulo(String titulo) {
        Optional<Libro> opt = buscarPorTitulo(titulo);
        if (opt.isPresent()) {
            libros.remove(opt.get());
            return true;
        }
        return false;
    }

    /** Marca como prestado el primer libro que coincida con el título. Devuelve true si se pudo prestar. */
    public boolean prestarLibro(String titulo) {
        Optional<Libro> opt = buscarPorTitulo(titulo);
        if (opt.isPresent()) {
            Libro l = opt.get();
            if (!l.isPrestado()) {
                l.prestar();
                return true;
            }
        }
        return false;
    }

    /** Marca como devuelto el primer libro que coincida con el título. Devuelve true si se pudo devolver. */
    public boolean devolverLibro(String titulo) {
        Optional<Libro> opt = buscarPorTitulo(titulo);
        if (opt.isPresent()) {
            Libro l = opt.get();
            if (l.isPrestado()) {
                l.devolver();
                return true;
            }
        }
        return false;
    }

    /** Elimina todos los libros de la colección. */
    public void eliminarTodos() {
        libros.clear();
    }

    /** Indica si la colección está vacía. */
    public boolean estaVacia() {
        return libros.isEmpty();
    }
}