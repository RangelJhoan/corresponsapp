package com.example.corresponsapp.interfacesgraficas.corresponsal.consultarsaldo;

import android.content.Context;

import com.example.corresponsapp.entidades.CuentaBancaria;

public interface ConsultarSaldoMVP {
    interface View{
        void mostrarSaldo(String saldo);
        void mostrarError(String error);
    }
    interface Presenter{
        void mostrarSaldo(String saldo);
        void mostrarError(String error);
        void consultarSaldo(Context context, CuentaBancaria cuentaBancaria);
    }
    interface Model{
        void consultarSaldo(Context context, CuentaBancaria cuentaBancaria);
    }
}
