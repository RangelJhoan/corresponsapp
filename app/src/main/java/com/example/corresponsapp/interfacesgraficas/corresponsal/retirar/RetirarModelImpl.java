package com.example.corresponsapp.interfacesgraficas.corresponsal.retirar;

import android.content.Context;

import com.example.corresponsapp.basedatos.BaseDatos;
import com.example.corresponsapp.entidades.Retiro;

public class RetirarModelImpl implements RetirarMVP.Model{

    private RetirarMVP.Presenter presenter;
    private BaseDatos baseDatos;

    public RetirarModelImpl(RetirarMVP.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void retirarDinero(Context context, Retiro retiro) {
        baseDatos = BaseDatos.getInstance(context);
        long resultadoRetiro = baseDatos.creaRetiro(retiro);
        if(resultadoRetiro > 0){
            presenter.mostrarResultado("Retiro realizado correctamente");
        }else if(resultadoRetiro == -1){
            presenter.mostrarError("¡ERROR! No se encontró el cliente");
        }
        else if(resultadoRetiro == -2){
            presenter.mostrarError("¡ERROR! No se encontró la cuenta bancaria");
        }
        else if(resultadoRetiro == -3){
            presenter.mostrarError("¡ERROR! No se realizó el retiro");
        }
        else if(resultadoRetiro == -4){
            presenter.mostrarError("¡ERROR! No se guardó el retiro");
        }else{
            presenter.mostrarError("¡ERROR!");
        }
    }
}
