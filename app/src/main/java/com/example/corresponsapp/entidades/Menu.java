package com.example.corresponsapp.entidades;

public class Menu {
    private String nombre;
    private int identificadorRecurso;

    public Menu() {
    }

    public Menu(String nombre, int identificadorRecurso) {
        this.nombre = nombre;
        this.identificadorRecurso = identificadorRecurso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdentificadorRecurso() {
        return identificadorRecurso;
    }

    public void setIdentificadorRecurso(int identificadorRecurso) {
        this.identificadorRecurso = identificadorRecurso;
    }
}
