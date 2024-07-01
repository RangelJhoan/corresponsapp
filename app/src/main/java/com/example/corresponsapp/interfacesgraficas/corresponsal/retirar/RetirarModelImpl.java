package com.example.corresponsapp.interfacesgraficas.corresponsal.retirar;

import android.content.Context;

import com.example.corresponsapp.basedatos.BaseDatos;
import com.example.corresponsapp.entidades.Retiro;
import com.example.corresponsapp.utilidades.Constantes;
import com.example.corresponsapp.utilidades.Sesion;

public class RetirarModelImpl implements RetirarMVP.Model {

    private final RetirarMVP.Presenter presenter;

    public RetirarModelImpl(RetirarMVP.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void retirarDinero(Context context, Retiro retiro) {
        BaseDatos baseDatos = BaseDatos.getInstance(context);
        //Consultar que el cliente exista
        int idCliente = baseDatos.consultarIdCliente(retiro.getCuentaBancaria().getCliente().getDocumento());
        if (idCliente > 0) {
            //Consultar que el PIN ingresado sea igual al del cliente
            if (retiro.getCuentaBancaria().getPin().equals(baseDatos.consultarPINCuenta(retiro.getCuentaBancaria().getCliente().getDocumento()))) {
                //Consultar que el cliente tenga asignada una cuenta bancaria
                int idCuenta = baseDatos.consultarIdCuentaDocumento(retiro.getCuentaBancaria().getCliente().getDocumento());
                if (idCuenta > 0) {
                    //Validamos que se le reste el valor al corresponsal
                    if (baseDatos.retirarCorresponsal(Sesion.corresponsalSesion.getId(), retiro.getMonto()) > 0) {
                        //Retirar el monto de la cuenta del cliente
                        long resultadoRetiroCliente = baseDatos.retirarDinero(idCuenta, retiro.getMonto(), Constantes.COMISION_RETIRAR);
                        if (resultadoRetiroCliente > 0) {
                            //Verificamos que se realice el registro de la comisión
                            long respuestaComision = baseDatos.registrarComision(Sesion.corresponsalSesion.getId(), Constantes.COMISION_RETIRAR);
                            if (respuestaComision > 0) {
                                retiro.getCuentaBancaria().getCliente().setId(idCliente);
                                long respuestaRetiro = baseDatos.crearRetiro(retiro);
                                if (respuestaRetiro > 0) {
                                    presenter.mostrarResultado("Dinero retirado correctamente");
                                } else {
                                    presenter.mostrarError("¡Error! No se pudo registrar el retiro");
                                }
                            } else {
                                presenter.mostrarError("¡Error! No se pudo registrar la comisión");
                            }
                        } else {
                            presenter.mostrarError("¡Error! Saldo insuficiente para el retiro");
                        }
                    } else {
                        presenter.mostrarError("¡Error! Saldo insuficiente en el corresponsal");
                    }
                } else {
                    presenter.mostrarError("¡Error! Cliente sin una cuenta bancaria asociada");
                }
            } else {
                presenter.mostrarError("¡Error! Código PIN incorrecto");
            }
        } else {
            presenter.mostrarError("¡Error! Cliente no registrado");
        }
    }
}
