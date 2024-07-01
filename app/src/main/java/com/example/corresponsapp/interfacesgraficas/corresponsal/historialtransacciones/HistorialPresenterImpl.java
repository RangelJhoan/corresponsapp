package com.example.corresponsapp.interfacesgraficas.corresponsal.historialtransacciones;

import android.content.Context;

import com.example.corresponsapp.entidades.ConsultaSaldo;
import com.example.corresponsapp.entidades.CuentaCreada;
import com.example.corresponsapp.entidades.Deposito;
import com.example.corresponsapp.entidades.PagoTarjeta;
import com.example.corresponsapp.entidades.Retiro;
import com.example.corresponsapp.entidades.Transferencia;

import java.util.ArrayList;

public class HistorialPresenterImpl implements HistorialMVP.Presenter{

    private final HistorialMVP.Model model;
    private final HistorialMVP.View view;

    public HistorialPresenterImpl(HistorialMVP.View view) {
        this.view = view;
        model = new HistorialModelImpl(this);
    }

    @Override
    public void mostrarPagosTarjeta(ArrayList<PagoTarjeta> listaPagoTarjetas) {
        if(view !=null){
            view.mostrarPagosTarjeta(listaPagoTarjetas);
        }
    }

    @Override
    public void mostrarRetiros(ArrayList<Retiro> listaRetiros) {
        if(view != null){
            view.mostrarRetiros(listaRetiros);
        }
    }

    @Override
    public void mostrarDepositos(ArrayList<Deposito> listaDepositos) {
        if(view != null){
            view.mostrarDepositos(listaDepositos);
        }
    }

    @Override
    public void mostrarTransferencias(ArrayList<Transferencia> listaTransferencias) {
        if(view != null){
            view.mostrarTransferencias(listaTransferencias);
        }
    }

    @Override
    public void mostrarConsultasSaldo(ArrayList<ConsultaSaldo> listaConsultasSaldo) {
        if(view != null){
            view.mostrarConsultasSaldo(listaConsultasSaldo);
        }
    }

    @Override
    public void mostrarCuentasCreadas(ArrayList<CuentaCreada> listaCuentasCreadas) {
        if(view != null){
            view.mostrarCuentasCreadas(listaCuentasCreadas);
        }
    }

    @Override
    public void consultarHistorialTransacciones(Context context) {
        if(model != null){
            model.consultarHistorialTransacciones(context);
        }
    }
}
