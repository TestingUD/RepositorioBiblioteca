package com.gestionLibros;

public class Libro {

    private String titulo;
    private String autor;
    private int anio;
    private String isbn;
    private boolean prestado;

    public Libro(String titulo, String autor, int anio, String isbn) {
        this.titulo = titulo;
        this.autor = autor;
        this.anio = anio;
        this.isbn = isbn;
        this.prestado = false;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public int getAnio() {
        return anio;
    }

    public String getIsbn() {
        return isbn;
    }

    public boolean isPrestado() {
        return prestado;
    }

    public void prestar() {
        this.prestado = true;
    }

    public void devolver() {
        this.prestado = false;
    }

    @Override
    public String toString() {
        String estado = prestado ? "Prestado" : "Disponible";
        StringBuilder sb = new StringBuilder();
        sb.append("Título: ").append(titulo)
                .append(" | Autor: ").append(autor)
                .append(" | Año: ").append(anio);
        if (isbn != null && !isbn.isEmpty()) {
            sb.append(" | ISBN: ").append(isbn);
        }
        sb.append(" | Estado: ").append(estado);
        return sb.toString();
    }

}
