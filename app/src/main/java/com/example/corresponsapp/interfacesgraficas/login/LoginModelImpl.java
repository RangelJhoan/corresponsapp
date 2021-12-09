package com.example.corresponsapp.interfacesgraficas.login;

import android.content.Context;

import com.example.corresponsapp.basedatos.BaseDatos;
import com.example.corresponsapp.entidades.Corresponsal;
import com.example.corresponsapp.utilidades.Sesion;

public class LoginModelImpl implements LoginMVP.Model{

    private BaseDatos baseDatos;
    private LoginMVP.Presenter presenter;

    public LoginModelImpl(LoginMVP.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void iniciarSesion(Context context, String correo, String clave) {
        baseDatos = BaseDatos.getInstance(context);
        Sesion.corresponsalSesion = new Corresponsal();
        Corresponsal corresponsal = baseDatos.iniciarSesion(correo, clave);

        if (corresponsal != null){
            Sesion.corresponsalSesion = corresponsal;
            presenter.mostrarResultado("¡Bienvenido!");
        }else{
            presenter.mostrarError("¡ERROR! Correo o clave incorrecta");
        }
    }
}
