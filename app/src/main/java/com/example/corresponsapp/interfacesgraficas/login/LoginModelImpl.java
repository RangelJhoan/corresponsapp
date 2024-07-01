package com.example.corresponsapp.interfacesgraficas.login;

import android.content.Context;

import com.example.corresponsapp.basedatos.BaseDatos;
import com.example.corresponsapp.entidades.Corresponsal;

public class LoginModelImpl implements LoginMVP.Model {

    private final LoginMVP.Presenter presenter;

    public LoginModelImpl(LoginMVP.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void iniciarSesion(Context context, String correo, String clave) {
        BaseDatos baseDatos = BaseDatos.getInstance(context);
        //Se realiza la consulta del corresponsal con los datos ingresados en el login
        Corresponsal corresponsal = baseDatos.iniciarSesion(correo, clave);

        /*Si la información es correcta, se mostrará en pantalla un mensaje de bienvenida y,
        se creará el objeto que almacenará la información del corresponsal que inició la sesión.
        Si no, se devolverá un mensaje de error.*/
        if (corresponsal != null) {
            presenter.mostrarResultado(corresponsal, "¡Bienvenido!");
        } else {
            presenter.mostrarError("¡ERROR! Correo o clave incorrecta");
        }
    }
}
