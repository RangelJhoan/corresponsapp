package com.example.corresponsapp.interfacesgraficas.corresponsal.transferir;

import android.content.Context;

import com.example.corresponsapp.entidades.Transferencia;

public interface TransferirMVP {
    interface View{
        void mostrarResultado(String resultado);
        void mostrarError(String error);
    }
    interface Presenter{
        void mostrarResultado(String resultado);
        void mostrarError(String error);
        void transferirDinero(Context context, Transferencia transferencia);
    }
    interface Model{
        void transferirDinero(Context context, Transferencia transferencia);
    }
}
