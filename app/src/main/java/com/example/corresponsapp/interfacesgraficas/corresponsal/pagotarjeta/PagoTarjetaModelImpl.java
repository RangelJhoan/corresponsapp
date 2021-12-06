package com.example.corresponsapp.interfacesgraficas.corresponsal.pagotarjeta;

import android.content.Context;

import com.example.corresponsapp.basedatos.BaseDatos;
import com.example.corresponsapp.entidades.PagoTarjeta;

public class PagoTarjetaModelImpl implements PagoTarjetaMVP.Model{

    private PagoTarjetaMVP.Presenter presenter;
    private BaseDatos baseDatos;

    public PagoTarjetaModelImpl(PagoTarjetaMVP.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void pagarTarjeta(Context context, PagoTarjeta pagoTarjeta) {
        baseDatos = BaseDatos.getInstance(context);
        long respuestaPagoTarjeta = baseDatos.crearPagoTarjeta(pagoTarjeta);
        if(respuestaPagoTarjeta > 0){
            presenter.mostrarResultado("Pago realizado correctamente");
        }else{
            presenter.mostrarError("Error al realizar pago");
        }
    }
}
