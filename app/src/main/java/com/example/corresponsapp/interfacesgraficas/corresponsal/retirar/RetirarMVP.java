package com.example.corresponsapp.interfacesgraficas.corresponsal.retirar;

import android.content.Context;

import com.example.corresponsapp.entidades.Retiro;

public interface RetirarMVP {
    interface View{
        void mostrarResultado(String resultado);
        void mostrarError(String error);
    }
    interface Presenter{
        void mostrarResultado(String resultado);
        void mostrarError(String error);
        void retirarDinero(Context context, Retiro retiro);
    }
    interface Model{
        void retirarDinero(Context context, Retiro retiro);
    }
}
