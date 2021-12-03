package com.example.corresponsapp.interfacesgraficas.corresponsal.depositar;

import android.content.Context;

import com.example.corresponsapp.entidades.Deposito;

public interface DepositarMVP {
    interface View{
        void mostrarRespueta(String respuesta);
        void mostrarError(String error);
    }
    interface Presenter{
        void mostrarRespueta(String respuesta);
        void mostrarError(String error);
        void depositarDinero(Context context, Deposito deposito);
    }
    interface Model{
        void depositarDinero(Context context, Deposito deposito);
    }
}
