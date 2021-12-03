package com.example.corresponsapp.interfacesgraficas.corresponsal.retirar;

import android.content.Context;

import com.example.corresponsapp.entidades.Retiro;

public class RetirarPresenterImpl implements RetirarMVP.Presenter {

    private RetirarMVP.Model model;
    private RetirarMVP.View view;

    public RetirarPresenterImpl(RetirarMVP.View view) {
        this.view = view;
        model = new RetirarModelImpl(this);
    }

    @Override
    public void mostrarResultado(String resultado) {
        if (view != null) {
            view.mostrarResultado(resultado);
        }
    }

    @Override
    public void mostrarError(String error) {
        if (view != null) {
            view.mostrarError(error);
        }
    }

    @Override
    public void retirarDinero(Context context, Retiro retiro) {
        if (model != null) {
            model.retirarDinero(context, retiro);
        }
    }
}
