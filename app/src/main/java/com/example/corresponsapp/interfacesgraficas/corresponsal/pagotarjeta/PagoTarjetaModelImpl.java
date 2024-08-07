package com.example.corresponsapp.interfacesgraficas.corresponsal.pagotarjeta;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.corresponsapp.basedatos.BaseDatos;
import com.example.corresponsapp.entidades.PagoTarjeta;
import com.example.corresponsapp.utilidades.Sesion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PagoTarjetaModelImpl implements PagoTarjetaMVP.Model {

    private final PagoTarjetaMVP.Presenter presenter;

    public PagoTarjetaModelImpl(PagoTarjetaMVP.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void pagarTarjeta(Context context, PagoTarjeta pagoTarjeta) {
        BaseDatos baseDatos = BaseDatos.getInstance(context);

        //Verificar que el número de cuenta sea correcto
        int idCuenta = baseDatos.consultarIdCuentaNumero(pagoTarjeta.getCuentaBancaria().getNumeroCuenta());
        if (idCuenta > 0) {
            //Verificar que el código CVV es igual al digitado
            String respuestaCVV = baseDatos.consultarCVVCuenta(pagoTarjeta.getCuentaBancaria().getNumeroCuenta());
            if (pagoTarjeta.getCuentaBancaria().getTarjeta().getCvv().equals(respuestaCVV)) {
                //Verificamos que el nombre ingresado sea correcto
                if (pagoTarjeta.getCuentaBancaria().getCliente().getNombreCompleto().equals(baseDatos.consultarNombreCliente(pagoTarjeta.getCuentaBancaria().getNumeroCuenta()))) {
                    //Verificamos que la fecha ingresada sea correcta
                    String fechaTarjeta = baseDatos.consultarFechaExpiracion(pagoTarjeta.getCuentaBancaria().getNumeroCuenta());
                    if (fechaTarjeta.equals(pagoTarjeta.getCuentaBancaria().getTarjeta().getFechaExpiracion())) {
                        //Verificamos que la fecha de expiración sea mayor a la actual
                        if (verificarFecha(pagoTarjeta.getCuentaBancaria().getTarjeta().getFechaExpiracion())) {
                            //Retiramos el dinero que pagó el cliente
                            long respuestaRetiro = baseDatos.retirarDinero(idCuenta, pagoTarjeta.getValor(), 0);
                            if (respuestaRetiro > 0) {
                                //Le enviamos el dinero al corresponsal
                                long respuestaComision = baseDatos.registrarComision(Sesion.corresponsalSesion.getId(), pagoTarjeta.getValor());
                                if (respuestaComision > 0) {
                                    pagoTarjeta.getCuentaBancaria().setId(idCuenta);
                                    //Registramos el pago al corresponsal
                                    long respuestaPagoTarjeta = baseDatos.crearPagoTarjeta(pagoTarjeta);
                                    if (respuestaPagoTarjeta > 0) {
                                        presenter.mostrarResultado("Pago realizado correctamente");
                                    } else {
                                        presenter.mostrarError("¡Error! Pago no registrado");
                                    }
                                } else {
                                    presenter.mostrarError("¡Error! No se pudo pagar el dinero");
                                }
                            } else {
                                presenter.mostrarError("¡Error! Saldo no disponible para el pago");
                            }
                        } else {
                            presenter.mostrarError("¡Error! La tarjeta se encuentra expirada");
                        }
                    } else {
                        presenter.mostrarError("¡Error! Fecha incorrecta");
                    }
                } else {
                    presenter.mostrarError("¡Error! Nombre incorrecto");
                }
            } else {
                presenter.mostrarError("¡Error! Código CVV incorrecto");
            }
        } else {
            presenter.mostrarError("¡Error! Cuenta bancaria no registrada");
        }
    }

    private boolean verificarFecha(String fecha_expiracion) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date srtDate = sdf.parse(fecha_expiracion);
            return !new Date().after(srtDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
