package com.example.corresponsapp.interfacesgraficas.corresponsal.historialtransacciones;

import android.content.Context;

import com.example.corresponsapp.basedatos.BaseDatos;

public class HistorialModelImpl implements HistorialMVP.Model{

    private final HistorialMVP.Presenter presenter;

    public HistorialModelImpl(HistorialMVP.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void consultarHistorialTransacciones(Context context) {
        BaseDatos baseDatos = BaseDatos.getInstance(context);

        presenter.mostrarRetiros(baseDatos.consultarRetiros());
        presenter.mostrarDepositos(baseDatos.consultarDepositos());
        presenter.mostrarPagosTarjeta(baseDatos.consultarPagosTarjeta());
        presenter.mostrarTransferencias(baseDatos.consultarTransferencias());
        presenter.mostrarCuentasCreadas(baseDatos.consultarCuentasCreadas());
        presenter.mostrarConsultasSaldo(baseDatos.consultarConsultasSaldo());

    }
}
