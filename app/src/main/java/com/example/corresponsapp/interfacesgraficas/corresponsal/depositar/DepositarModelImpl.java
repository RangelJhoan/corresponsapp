package com.example.corresponsapp.interfacesgraficas.corresponsal.depositar;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.corresponsapp.basedatos.BaseDatos;
import com.example.corresponsapp.entidades.Cliente;
import com.example.corresponsapp.entidades.CuentaBancaria;
import com.example.corresponsapp.entidades.Deposito;
import com.example.corresponsapp.utilidades.Constantes;
import com.example.corresponsapp.utilidades.Sesion;
import com.example.corresponsapp.utilidades.UtilidadesBD;

public class DepositarModelImpl implements DepositarMVP.Model {

    private DepositarMVP.Presenter presenter;
    private BaseDatos baseDatos;

    public DepositarModelImpl(DepositarMVP.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void depositarDinero(Context context, Deposito deposito) {
        baseDatos = BaseDatos.getInstance(context);
        int idCliente = baseDatos.consultarIdCliente(deposito.getCuentaBancaria().getCliente().getDocumento());
        //Verificamos que el cliente esté registrado
        if (idCliente > 0) {
            //Verificamos que el cliente tenga asignada una cuenta bancaria
            int idCuenta = baseDatos.consultarIdCuentaDocumento(deposito.getCuentaBancaria().getCliente().getDocumento());
            if (idCuenta > 0) {
                //Verificamos que se haya actualizado la cuenta bancaria del cliente
                long resultadoDepositoCliente = baseDatos.depositarDinero(idCuenta, deposito.getMonto(), Constantes.COMISION_DEPOSITAR);
                if (resultadoDepositoCliente > 0) {
                    //Verificamos que el corresponsal reciba el dinero
                    double depositoCorresponsal = deposito.getMonto() - Constantes.COMISION_DEPOSITAR;
                    if(baseDatos.pagarCorresponsal(Sesion.corresponsalSesion.getId(), depositoCorresponsal) > 0){
                        deposito.getCuentaBancaria().getCliente().setId(idCliente);
                        baseDatos.crearDeposito(deposito);
                        presenter.mostrarRespueta("Depósito realizado correctamente");
                    }else{
                        presenter.mostrarError("¡Error! Corresponsal no recibió el depósito");
                    }
                } else {
                    presenter.mostrarError("¡Error! No se pudo depositar el dinero");
                }
            } else {
                presenter.mostrarError("¡Error! Cuenta bancaria no registrada");
            }
        } else {
            presenter.mostrarError("¡Error! Cliente no registrado");
        }
    }
}
