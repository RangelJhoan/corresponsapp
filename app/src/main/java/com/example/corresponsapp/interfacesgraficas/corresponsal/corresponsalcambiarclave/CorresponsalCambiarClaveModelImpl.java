package com.example.corresponsapp.interfacesgraficas.corresponsal.corresponsalcambiarclave;

import android.content.Context;

import com.example.corresponsapp.basedatos.BaseDatos;
import com.example.corresponsapp.entidades.Corresponsal;

public class CorresponsalCambiarClaveModelImpl implements CorresponsalCambiarClaveMVP.Model{

    private CorresponsalCambiarClaveMVP.Presenter presenter;
    private BaseDatos baseDatos;

    public CorresponsalCambiarClaveModelImpl(CorresponsalCambiarClaveMVP.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void cambiarClave(Context context, Corresponsal corresponsal) {
        baseDatos = BaseDatos.getInstance(context);
        //Se valida que el correo electrónico ingresado sea igual al correo del corresponsal que inició sesión
        if(baseDatos.consultarCorreoCorresponsal(corresponsal.getId()).equals(corresponsal.getCorreo())){
            long respuestaCambioClave =  baseDatos.cambiarClave(corresponsal.getId(), corresponsal.getClave());
            //Se valida que se haya realizado el cambio de clave
            if(respuestaCambioClave > 0){
                presenter.mostrarResultado("Clave cambiada correctamente");
            }else{
                presenter.mostrarError("Error al cambiar la clave");
            }
        }else{
            presenter.mostrarError("Correo electrónico incorrecto");
        }
    }
}
