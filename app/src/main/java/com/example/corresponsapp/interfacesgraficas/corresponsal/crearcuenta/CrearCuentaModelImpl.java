package com.example.corresponsapp.interfacesgraficas.corresponsal.crearcuenta;

import android.content.Context;

import com.example.corresponsapp.basedatos.BaseDatos;
import com.example.corresponsapp.entidades.CuentaBancaria;
import com.example.corresponsapp.utilidades.Constantes;
import com.example.corresponsapp.utilidades.Sesion;

public class CrearCuentaModelImpl implements CrearCuentaMVP.Model {

    private BaseDatos baseDatos;
    private CrearCuentaMVP.Presenter presenter;

    public CrearCuentaModelImpl(CrearCuentaMVP.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void crearCuenta(Context context, CuentaBancaria cuentaBancaria) {
        baseDatos = BaseDatos.getInstance(context);
        //Consultar el id del cliente apartir del documento para saber si ya se encuentra registrado
        long idCliente = baseDatos.consultarIdCliente(cuentaBancaria.getCliente().getDocumento());
        if (idCliente <= 0) {
            //Guardamos el registro del cliente nuevo
            long respuestaCliente = baseDatos.crearCliente(cuentaBancaria.getCliente());
            if (respuestaCliente > 0) {
                //Le asignamos una tarjeta
                long respuestaTarjeta = baseDatos.crearTarjeta(cuentaBancaria.getTarjeta());
                if (respuestaTarjeta > 0) {
                    //Creamos la cuenta
                    cuentaBancaria.getTarjeta().setId((int) respuestaTarjeta);
                    cuentaBancaria.getCliente().setId((int) respuestaCliente);
                    long respuestaCrearCuenta = baseDatos.crearCuenta(cuentaBancaria);
                    if (respuestaCrearCuenta > 0) {
                        //Cuando se registre el cliente se le debe sumar 10000 al corresponsal
                        long respuestaComision = baseDatos.registrarComision(Sesion.corresponsalSesion.getId(), Constantes.COMISION_CUENTA_NUEVA);
                        if (respuestaComision > 0) {
                            presenter.mostrarResultado("Cuenta creada correctamente");
                        } else {
                            presenter.mostrarError("¡Error! No se pudo registrar la comisión");
                        }
                    } else {
                        presenter.mostrarError("¡Error! No se creó la cuenta");
                    }
                } else {
                    presenter.mostrarError("¡Error! No se pudo asignar la tarjeta");
                }
            } else {
                presenter.mostrarError("¡Error! No se pudo crear el cliente");
            }
        } else {
            presenter.mostrarError("¡Error! Número de documento previamente registrado");
        }
    }
}
