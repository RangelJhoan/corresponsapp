package com.example.corresponsapp.interfacesgraficas.corresponsal.consultarsaldo;

import android.content.Context;

import com.example.corresponsapp.basedatos.BaseDatos;
import com.example.corresponsapp.entidades.Cliente;
import com.example.corresponsapp.entidades.CuentaBancaria;

public class ConsultarSaldoModelImpl implements ConsultarSaldoMVP.Model{

    private ConsultarSaldoMVP.Presenter presenter;
    private BaseDatos baseDatos;

    public ConsultarSaldoModelImpl(ConsultarSaldoMVP.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void consultarSaldo(Context context, CuentaBancaria cuentaBancaria) {
        baseDatos = BaseDatos.getInstance(context);
        long respuesta = baseDatos.consultarSaldoCliente(cuentaBancaria);
        if(respuesta > 0){
            presenter.mostrarSaldo(String.valueOf(respuesta));
        }else if(respuesta == -1){
            presenter.mostrarError("Cliente no encontrado");
        }else if(respuesta == -2){
            presenter.mostrarError("Consulta no realizada");
        }
    }
}
