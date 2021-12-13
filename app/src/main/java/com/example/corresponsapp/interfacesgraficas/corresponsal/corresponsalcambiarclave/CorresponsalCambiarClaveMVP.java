package com.example.corresponsapp.interfacesgraficas.corresponsal.corresponsalcambiarclave;

import android.content.Context;

import com.example.corresponsapp.entidades.Corresponsal;

public interface CorresponsalCambiarClaveMVP {
    interface View{
        void mostrarResultado(String resultado);
        void mostrarError(String error);
    }
    interface Presenter{
        void mostrarResultado(String resultado);
        void mostrarError(String error);
        void cambiarClave(Context context, Corresponsal corresponsal);
    }
    interface Model{
        void cambiarClave(Context context, Corresponsal corresponsal);
    }
}
