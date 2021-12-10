package com.example.corresponsapp.interfacesgraficas.corresponsal.historialtransacciones;

import android.content.Context;

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
    }
    interface Presenter{
        void mostrarPagosTarjeta(ArrayList<PagoTarjeta> listaPagoTarjetas);
        void mostrarRetiros(ArrayList<Retiro> listaRetiros);
        void mostrarDepositos(ArrayList<Deposito> listaDepositos);
        void mostrarTransferencias(ArrayList<Transferencia> listaTransferencias);
        void consultarHistorialTransacciones(Context context);
    }
    interface Model{
        void consultarHistorialTransacciones(Context context);
    }
}
