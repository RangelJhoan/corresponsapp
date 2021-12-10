package com.example.corresponsapp.entidades;

public class Transferencia {

    private int id;
    private CuentaBancaria cuentaRecibe;
    private CuentaBancaria cuentaTransfiere;
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

    public CuentaBancaria getCuentaRecibe() {
        return cuentaRecibe;
    }

    public void setCuentaRecibe(CuentaBancaria cuentaRecibe) {
        this.cuentaRecibe = cuentaRecibe;
    }

    public CuentaBancaria getCuentaTransfiere() {
        return cuentaTransfiere;
    }

    public void setCuentaTransfiere(CuentaBancaria cuentaTransfiere) {
        this.cuentaTransfiere = cuentaTransfiere;
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
