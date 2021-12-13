package com.example.corresponsapp.interfacesgraficas.login;

import android.content.Context;

import com.example.corresponsapp.entidades.Corresponsal;

public interface LoginMVP {
    interface View{
        void mostrarResultado(Corresponsal corresponsal, String resultado);
        void mostrarError(String error);
    }
    interface Presenter{
        void mostrarResultado(Corresponsal corresponsal, String resultado);
        void mostrarError(String error);
        void iniciarSesion(Context context, String correo, String clave);
    }
    interface Model{
        void iniciarSesion(Context context, String correo, String clave);
    }
}
