package com.example.corresponsapp.interfacesgraficas.login;

import android.content.Context;

public class LoginPresenterImpl implements LoginMVP.Presenter {

    private LoginMVP.Model model;
    private LoginMVP.View view;

    public LoginPresenterImpl(LoginMVP.View view) {
        this.view = view;
        this.model = new LoginModelImpl(this);
    }

    @Override
    public void mostrarResultado(String resultado) {
        if (view != null) {
            view.mostrarResultado(resultado);
        }
    }

    @Override
    public void mostrarError(String error) {
        if (view != null) {
            view.mostrarError(error);
        }
    }

    @Override
    public void iniciarSesion(Context context, String correo, String clave) {
        if (model != null) {
            model.iniciarSesion(context, correo, clave);
        }
    }
}
