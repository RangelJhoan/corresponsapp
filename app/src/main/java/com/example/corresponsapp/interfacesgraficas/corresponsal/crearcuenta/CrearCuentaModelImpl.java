package com.example.corresponsapp.interfacesgraficas.corresponsal.crearcuenta;

import android.content.Context;

import com.example.corresponsapp.basedatos.BaseDatos;
import com.example.corresponsapp.entidades.CuentaBancaria;

public class CrearCuentaModelImpl implements CrearCuentaMVP.Model{

    private BaseDatos baseDatos;
    private CrearCuentaMVP.Presenter presenter;

    public CrearCuentaModelImpl(CrearCuentaMVP.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void crearCuenta(Context context, CuentaBancaria cuentaBancaria) {
        baseDatos = BaseDatos.getInstance(context);
        long respuesta = baseDatos.crearCuenta(cuentaBancaria);
        if(respuesta > 0){
            presenter.mostrarResultado("Cuenta creada correctamente");
        }else if(respuesta == -4){
            presenter.mostrarError("¡ERROR! Documento ya registrado");
        }else{
            presenter.mostrarError("¡ERROR! Cuenta no creada");
        }
    }
}
