package com.example.corresponsapp.interfacesgraficas.login;

import android.content.Context;

public interface LoginMVP {
    interface View{
        void mostrarResultado(String resultado);
        void mostrarError(String error);
    }
    interface Presenter{
        void mostrarResultado(String resultado);
        void mostrarError(String error);
        void iniciarSesion(Context context, String correo, String clave);
    }
    interface Model{
        void iniciarSesion(Context context, String correo, String clave);
    }
}
