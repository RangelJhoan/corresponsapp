package com.example.corresponsapp.interfacesgraficas.corresponsal.depositar;

import android.content.Context;

import com.example.corresponsapp.entidades.Deposito;

public class DepositarPresenterImpl implements DepositarMVP.Presenter{

    private DepositarMVP.Model model;
    private DepositarMVP.View view;

    public DepositarPresenterImpl(DepositarMVP.View view) {
        this.view = view;
        model = new DepositarModelImpl(this);
    }

    @Override
    public void mostrarRespueta(String respuesta) {
        if(view != null){
            view.mostrarRespueta(respuesta);
        }
    }

    @Override
    public void mostrarError(String error) {
        if(view != null){
            view.mostrarError(error);
        }
    }

    @Override
    public void depositarDinero(Context context, Deposito deposito) {
        if(model != null){
            model.depositarDinero(context, deposito);
        }
    }
}
