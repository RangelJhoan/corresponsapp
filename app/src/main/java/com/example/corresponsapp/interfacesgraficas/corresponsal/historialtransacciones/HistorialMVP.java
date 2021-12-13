package com.example.corresponsapp.interfacesgraficas.corresponsal.historialtransacciones;

import android.content.Context;

import com.example.corresponsapp.entidades.ConsultaSaldo;
import com.example.corresponsapp.entidades.CuentaCreada;
import com.example.corresponsapp.entidades.Deposito;
import com.example.corresponsapp.entidades.PagoTarjeta;
import com.example.corresponsapp.entidades.Retiro;
import com.example.corresponsapp.entidades.Transferencia;

import java.util.ArrayList;

public interface HistorialMVP {
    interface View{
        void mostrarPagosTarjeta(ArrayList<PagoTarjeta> listaPagoTarjetas);
        void mostrarRetiros(ArrayList<Retiro> listaRetiros);
        void mostrarDepositos(ArrayList<Deposito> listaDepositos);
        void mostrarTransferencias(ArrayList<Transferencia> listaTransferencias);
        void mostrarConsultasSaldo(ArrayList<ConsultaSaldo> listaConsultasSaldo);
        void mostrarCuentasCreadas(ArrayList<CuentaCreada> listaCuentasCreadas);
    }
    interface Presenter{
        void mostrarPagosTarjeta(ArrayList<PagoTarjeta> listaPagoTarjetas);
        void mostrarRetiros(ArrayList<Retiro> listaRetiros);
        void mostrarDepositos(ArrayList<Deposito> listaDepositos);
        void mostrarTransferencias(ArrayList<Transferencia> listaTransferencias);
        void mostrarConsultasSaldo(ArrayList<ConsultaSaldo> listaConsultasSaldo);
        void mostrarCuentasCreadas(ArrayList<CuentaCreada> listaCuentasCreadas);
        void consultarHistorialTransacciones(Context context);
    }
    interface Model{
        void consultarHistorialTransacciones(Context context);
    }
}
