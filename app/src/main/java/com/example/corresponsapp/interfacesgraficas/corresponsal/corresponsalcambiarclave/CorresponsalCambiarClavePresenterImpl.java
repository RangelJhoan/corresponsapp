package com.example.corresponsapp.interfacesgraficas.corresponsal.corresponsalcambiarclave;

import android.content.Context;

import com.example.corresponsapp.entidades.Corresponsal;

public class CorresponsalCambiarClavePresenterImpl implements CorresponsalCambiarClaveMVP.Presenter {

    private CorresponsalCambiarClaveMVP.View view;
    private CorresponsalCambiarClaveMVP.Model model;

    public CorresponsalCambiarClavePresenterImpl(CorresponsalCambiarClaveMVP.View view) {
        this.view = view;
        model = new CorresponsalCambiarClaveModelImpl(this);
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
    public void cambiarClave(Context context, Corresponsal corresponsal) {
        if (model != null) {
            model.cambiarClave(context, corresponsal);
        }
    }
}
