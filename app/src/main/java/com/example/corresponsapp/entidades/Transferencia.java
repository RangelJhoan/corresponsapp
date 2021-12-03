package com.example.corresponsapp.entidades;

public class Transferencia {

    private int id;
    private Cliente cliente_recibe;
    private Cliente cliente_transfiere;
    private double monto;
    private String fecha;

    public Transferencia() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente_recibe() {
        return cliente_recibe;
    }

    public void setCliente_recibe(Cliente cliente_recibe) {
        this.cliente_recibe = cliente_recibe;
    }

    public Cliente getCliente_transfiere() {
        return cliente_transfiere;
    }

    public void setCliente_transfiere(Cliente cliente_transfiere) {
        this.cliente_transfiere = cliente_transfiere;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
