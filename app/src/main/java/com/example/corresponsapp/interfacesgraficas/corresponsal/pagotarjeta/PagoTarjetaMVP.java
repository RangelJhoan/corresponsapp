package com.example.corresponsapp.interfacesgraficas.corresponsal.pagotarjeta;

import android.content.Context;

import com.example.corresponsapp.entidades.PagoTarjeta;

public interface PagoTarjetaMVP {
    interface View{
        void mostrarResultado(String resultado);
        void mostrarError(String error);
    }
    interface Presenter{
        void mostrarResultado(String resultado);
        void mostrarError(String error);
        void pagarTarjeta(Context context, PagoTarjeta pagoTarjeta);
    }
    interface Model{
        void pagarTarjeta(Context context, PagoTarjeta pagoTarjeta);
    }
}
