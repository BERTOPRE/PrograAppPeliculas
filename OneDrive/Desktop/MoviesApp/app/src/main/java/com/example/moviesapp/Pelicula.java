package com.example.moviesapp;

public class Pelicula {
    private int codigo;
    private String nombre_articulo;
    private int existencias;
    private double precio_costo;
    private double precio_venta;
    private String categoria;

    // Constructor
    public Pelicula(int codigo, String nombre_articulo, int existencias,
                    double precio_costo, double precio_venta, String categoria) {
        this.codigo = codigo;
        this.nombre_articulo = nombre_articulo;
        this.existencias = existencias;
        this.precio_costo = precio_costo;
        this.precio_venta = precio_venta;
        this.categoria = categoria;
    }

    // Getters
    public int getCodigo() { return codigo; }
    public String getNombre_articulo() { return nombre_articulo; }
    public int getExistencias() { return existencias; }
    public double getPrecio_costo() { return precio_costo; }
    public double getPrecio_venta() { return precio_venta; }
    public String getCategoria() { return categoria; }
}
