package com.example.corresponsapp.interfacesgraficas.corresponsal.transferir;

import android.content.Context;

import com.example.corresponsapp.entidades.Transferencia;

public class TransferirPresenterImpl implements TransferirMVP.Presenter{

    private final TransferirMVP.Model model;
    private final TransferirMVP.View view;

    public TransferirPresenterImpl(TransferirMVP.View view) {
        this.view = view;
        model = new TransferirModelImpl(this);
    }

    @Override
    public void mostrarResultado(String resultado) {
        if(view != null){
            view.mostrarResultado(resultado);
        }
    }

    @Override
    public void mostrarError(String error) {
        if(view != null){
            view.mostrarError(error);
        }
    }

    @Override
    public void transferirDinero(Context context, Transferencia transferencia) {
        if(model != null){
            model.transferirDinero(context, transferencia);
        }
    }
}
