package com.example.corresponsapp.interfacesgraficas.corresponsal.consultarsaldo;

import android.content.Context;

import com.example.corresponsapp.entidades.CuentaBancaria;

public class ConsultarSaldoPresenterImpl implements ConsultarSaldoMVP.Presenter{

    private final ConsultarSaldoMVP.Model model;
    private final ConsultarSaldoMVP.View view;

    public ConsultarSaldoPresenterImpl(ConsultarSaldoMVP.View view) {
        this.view = view;
        model = new ConsultarSaldoModelImpl(this);
    }

    @Override
    public void mostrarSaldo(String saldo) {
        if(view != null){
            view.mostrarSaldo(saldo);
        }
    }

    @Override
    public void mostrarError(String error) {
        if(view != null){
            view.mostrarError(error);
        }
    }

    @Override
    public void consultarSaldo(Context context, CuentaBancaria cuentaBancaria) {
        if(model != null){
            model.consultarSaldo(context, cuentaBancaria);
        }
    }
}
