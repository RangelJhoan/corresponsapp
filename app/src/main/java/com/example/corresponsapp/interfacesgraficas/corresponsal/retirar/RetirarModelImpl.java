package com.example.corresponsapp.interfacesgraficas.corresponsal.retirar;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.corresponsapp.basedatos.BaseDatos;
import com.example.corresponsapp.entidades.Retiro;
import com.example.corresponsapp.utilidades.Constantes;
import com.example.corresponsapp.utilidades.Sesion;
import com.example.corresponsapp.utilidades.UtilidadesBD;

public class RetirarModelImpl implements RetirarMVP.Model {

    private RetirarMVP.Presenter presenter;
    private BaseDatos baseDatos;

    public RetirarModelImpl(RetirarMVP.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void retirarDinero(Context context, Retiro retiro) {
        baseDatos = BaseDatos.getInstance(context);
        //Consultar que el cliente exista
        int idCliente = baseDatos.consultarIdCliente(retiro.getCuentaBancaria().getCliente().getDocumento());
        if (idCliente > 0) {
            //Consultar que el PIN ingresado sea igual al del cliente
            if (retiro.getCuentaBancaria().getPIN().equals(baseDatos.consultarPINCuenta(retiro.getCuentaBancaria().getCliente().getDocumento()))) {
                //Consultar que el cliente tenga asignada una cuenta bancaria
                int idCuenta = baseDatos.consultarIdCuentaDocumento(retiro.getCuentaBancaria().getCliente().getDocumento());
                if (idCuenta > 0) {
                    //Retirar el monto de la cuenta del cliente
                    long resultadoRetiroCliente = baseDatos.retirarDinero(idCuenta, retiro.getMonto(), Constantes.COMISION_RETIRAR);
                    if (resultadoRetiroCliente > 0) {
                        long respuestaComision = baseDatos.registrarComision(Sesion.corresponsalSesion.getId(), Constantes.COMISION_RETIRAR);
                        if(respuestaComision > 0){
                            retiro.getCuentaBancaria().getCliente().setId(idCliente);
                            long respuestaRetiro = baseDatos.crearRetiro(retiro);
                            if (respuestaRetiro > 0) {
                                presenter.mostrarResultado("Dinero retirado correctamente");
                            }else{
                                presenter.mostrarError("¡Error! No se pudo registrar el retiro");
                            }
                        }else{
                            presenter.mostrarError("¡Error! No se pudo registrar la comisión");
                        }
                    } else {
                        presenter.mostrarError("¡Error! Saldo insuficiente para el retiro");
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
