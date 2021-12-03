package com.example.corresponsapp.interfacesgraficas.corresponsal.crearcuenta;

import android.content.Context;

import com.example.corresponsapp.entidades.Cliente;
import com.example.corresponsapp.entidades.CuentaBancaria;

public class CrearCuentaPresenterImpl implements CrearCuentaMVP.Presenter{

    private CrearCuentaMVP.Model model;
    private CrearCuentaMVP.View view;

    public CrearCuentaPresenterImpl(CrearCuentaMVP.View view) {
        this.view = view;
        model = new CrearCuentaModelImpl(this);
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
    public void crearCuenta(Context context, CuentaBancaria cuentaBancaria) {
        if(model != null){
            model.crearCuenta(context, cuentaBancaria);
        }
    }
}
