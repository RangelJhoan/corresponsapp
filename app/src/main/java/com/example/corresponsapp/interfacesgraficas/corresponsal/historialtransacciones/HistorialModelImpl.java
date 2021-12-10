package com.example.corresponsapp.interfacesgraficas.corresponsal.historialtransacciones;

import android.content.Context;

import com.example.corresponsapp.basedatos.BaseDatos;

public class HistorialModelImpl implements HistorialMVP.Model{

    private HistorialMVP.Presenter presenter;
    private BaseDatos baseDatos;

    public HistorialModelImpl(HistorialMVP.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void consultarHistorialTransacciones(Context context) {
        baseDatos = BaseDatos.getInstance(context);

        presenter.mostrarRetiros(baseDatos.consultarRetiros());
        presenter.mostrarDepositos(baseDatos.consultarDepositos());
        presenter.mostrarPagosTarjeta(baseDatos.consultarPagosTarjeta());
        presenter.mostrarTransferencias(baseDatos.consultarTransferencias());

    }
}
