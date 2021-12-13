package com.example.corresponsapp.interfacesgraficas.corresponsal.consultarsaldo;

import android.content.Context;

import com.example.corresponsapp.basedatos.BaseDatos;
import com.example.corresponsapp.entidades.CuentaBancaria;
import com.example.corresponsapp.utilidades.Constantes;
import com.example.corresponsapp.utilidades.Sesion;

public class ConsultarSaldoModelImpl implements ConsultarSaldoMVP.Model{

    private ConsultarSaldoMVP.Presenter presenter;
    private BaseDatos baseDatos;

    public ConsultarSaldoModelImpl(ConsultarSaldoMVP.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void consultarSaldo(Context context, CuentaBancaria cuentaBancaria) {
        baseDatos = BaseDatos.getInstance(context);
        //Validar que el cliente exista
        int idCliente = baseDatos.consultarIdCliente(cuentaBancaria.getCliente().getDocumento());
        if (idCliente > 0) {
            //Validar que el PIN de la cuenta sea igual al ingresado
            if (cuentaBancaria.getPIN().equals(baseDatos.consultarPINCuenta(cuentaBancaria.getCliente().getDocumento()))) {
                //Validar que el cliente tenga asignada una cuenta bancaria
                int idCuenta = baseDatos.consultarIdCuentaDocumento(cuentaBancaria.getCliente().getDocumento());
                if (idCuenta > 0) {
                    //Realizar el retiro de la comisión a la cuenta bancaria del cliente
                    long respuestaRetiro = baseDatos.retirarDinero(idCuenta, 0, Constantes.COMISION_CONSULTAR_SALDO);
                    if (respuestaRetiro > 0) {
                        //Validar que se registre la comisión al corresponsal
                        if (baseDatos.registrarComision(Sesion.corresponsalSesion.getId(), Constantes.COMISION_CONSULTAR_SALDO) > 0) {
                            presenter.mostrarSaldo(String.valueOf(baseDatos.consultarSaldoCliente(cuentaBancaria)));
                        } else {
                            presenter.mostrarError("Error al registrar la comisión");
                        }
                    } else {
                        presenter.mostrarError("¡ERROR! Saldo insuficiente");
                    }
                } else {
                    presenter.mostrarError("¡Error! Cuenta del cliente no registrada");
                }
            } else {
                presenter.mostrarError("¡Error! Número PIN no coincide");
            }
        } else {
            presenter.mostrarError("¡Error! Cliente no registrado");
        }
    }
}
