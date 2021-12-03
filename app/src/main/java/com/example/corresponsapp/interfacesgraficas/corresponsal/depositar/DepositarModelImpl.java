package com.example.corresponsapp.interfacesgraficas.corresponsal.depositar;

import android.content.Context;

import com.example.corresponsapp.basedatos.BaseDatos;
import com.example.corresponsapp.entidades.Deposito;

public class DepositarModelImpl implements DepositarMVP.Model {

    private DepositarMVP.Presenter presenter;
    private BaseDatos baseDatos;

    public DepositarModelImpl(DepositarMVP.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void depositarDinero(Context context, Deposito deposito) {
        baseDatos = BaseDatos.getInstance(context);
        long resultadoDeposito = baseDatos.crearDeposito(deposito);
        if (resultadoDeposito > 0) {
            presenter.mostrarRespueta("Deposito realizado correctamente");
        } else if (resultadoDeposito == -1) {
            presenter.mostrarError("¡Error! No se encontró el cliente");
        } else if (resultadoDeposito == -2) {
            presenter.mostrarError("¡Error! Cuenta del cliente incorrecta");
        } else if (resultadoDeposito == -3) {
            presenter.mostrarError("¡Error! No se realizó el depósito");
        } else if (resultadoDeposito == -4) {
            presenter.mostrarError("¡Error! No se guardó el depósito");
        } else {
            presenter.mostrarError("¡Error!");
        }
    }
}
