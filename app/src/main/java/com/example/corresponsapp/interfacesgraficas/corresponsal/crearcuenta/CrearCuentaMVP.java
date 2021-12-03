package com.example.corresponsapp.interfacesgraficas.corresponsal.crearcuenta;

import android.content.Context;

import com.example.corresponsapp.entidades.Cliente;
import com.example.corresponsapp.entidades.CuentaBancaria;

public interface CrearCuentaMVP {
    interface View{
        void mostrarResultado(String resultado);
        void mostrarError(String error);
    }
    interface Presenter{
        void mostrarResultado(String resultado);
        void mostrarError(String error);
        void crearCuenta(Context context, CuentaBancaria cuentaBancaria);
    }
    interface Model{
        void crearCuenta(Context context, CuentaBancaria cuentaBancaria);
    }
}
