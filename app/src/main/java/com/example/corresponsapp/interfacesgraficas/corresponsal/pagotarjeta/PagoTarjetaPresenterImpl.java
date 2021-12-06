package com.example.corresponsapp.interfacesgraficas.corresponsal.pagotarjeta;

import android.content.Context;

import com.example.corresponsapp.entidades.PagoTarjeta;

public class PagoTarjetaPresenterImpl implements PagoTarjetaMVP.Presenter{

    private PagoTarjetaMVP.View view;
    private PagoTarjetaMVP.Model model;

    public PagoTarjetaPresenterImpl(PagoTarjetaMVP.View view) {
        this.view = view;
        model = new PagoTarjetaModelImpl(this);
    }

    @Override
    public void mostrarResultado(String resultado) {
        if(view != null){
            view.mostrarResultado(resultado);
        }
    }

    @Override
    public void mostrarError(String error) {
        if(view != null){
            view.mostrarError(error);
        }
    }

    @Override
    public void pagarTarjeta(Context context, PagoTarjeta pagoTarjeta) {
        if(model != null){
            model.pagarTarjeta(context, pagoTarjeta);
        }
    }
}
