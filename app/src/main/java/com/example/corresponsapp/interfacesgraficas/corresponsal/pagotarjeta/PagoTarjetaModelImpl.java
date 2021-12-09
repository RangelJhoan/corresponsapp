package com.example.corresponsapp.interfacesgraficas.corresponsal.pagotarjeta;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.corresponsapp.basedatos.BaseDatos;
import com.example.corresponsapp.entidades.PagoTarjeta;
import com.example.corresponsapp.utilidades.Sesion;
import com.example.corresponsapp.utilidades.UtilidadesBD;

public class PagoTarjetaModelImpl implements PagoTarjetaMVP.Model {

    private PagoTarjetaMVP.Presenter presenter;
    private BaseDatos baseDatos;

    public PagoTarjetaModelImpl(PagoTarjetaMVP.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void pagarTarjeta(Context context, PagoTarjeta pagoTarjeta) {
        baseDatos = BaseDatos.getInstance(context);

        //Verificar que el número de cuenta sea correcto
        int idCuenta = baseDatos.consultarIdCuentaNumero(pagoTarjeta.getCuentaBancaria().getNumero_cuenta());
        if (idCuenta > 0) {
            //Verificar que el código CVV es igual al digitado
            String respuestaCVV = baseDatos.consultarCVVCuenta(pagoTarjeta.getCuentaBancaria().getNumero_cuenta());
            if(pagoTarjeta.getCuentaBancaria().getTarjeta().getCvv().equals(respuestaCVV)){
                //Retiramos el dinero que pagó el cliente
                long respuestaRetiro = baseDatos.retirarDinero(idCuenta, pagoTarjeta.getValor(), 0);
                if(respuestaRetiro > 0){
                    //Le enviamos el dinero al corresponsal
                    long respuestaComision = baseDatos.registrarComision(Sesion.corresponsalSesion.getId(), pagoTarjeta.getValor());
                    if(respuestaComision > 0){
                        pagoTarjeta.getCuentaBancaria().setId(idCuenta);
                        //Registramos el pago al corresponsal
                        long respuestaPagoTarjeta = baseDatos.crearPagoTarjeta(pagoTarjeta);
                        if (respuestaPagoTarjeta > 0) {
                            presenter.mostrarResultado("Pago realizado correctamente");
                        }else{
                            presenter.mostrarError("¡Error! Pago no registrado");
                        }
                    }else{
                        presenter.mostrarError("¡Error! No se pudo pagar el dinero");
                    }
                }else{
                    presenter.mostrarError("¡Error! Saldo no disponible para el pago");
                }
            }else{
                presenter.mostrarError("¡Error! Código CVV incorrecto");
            }
        } else {
            presenter.mostrarError("¡Error! Cuenta bancaria no registrada");
        }
    }
}
