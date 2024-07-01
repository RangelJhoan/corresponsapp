package com.example.corresponsapp.interfacesgraficas.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.corresponsapp.databinding.FragmentDialogConfirmarBinding;
import com.example.corresponsapp.interfaces.ConfirmacionCallback;
import com.example.corresponsapp.utilidades.Constantes;

import java.util.Hashtable;

public class DialogConfirmarFragment extends DialogFragment {

    private FragmentDialogConfirmarBinding binding;
    private final ConfirmacionCallback callback;
    private final Hashtable<String, String> informacion;

    public DialogConfirmarFragment(Hashtable<String, String> informacion, ConfirmacionCallback callback) {
        this.callback = callback;
        this.informacion = informacion;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return crearDialogo();
    }

    private AlertDialog crearDialogo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        binding = FragmentDialogConfirmarBinding.inflate(getActivity().getLayoutInflater());

        if (informacion.get("accion").equals(Constantes.DEPOSITAR)) {
            //Cargar información de depositar
            binding.tvSetAccion.setText(informacion.get("accion"));
            binding.tvSetDocumento.setText(informacion.get("documentoAccion"));
            binding.tvSetDocumentoRecibe.setText(informacion.get("documentoRecibe"));
            binding.tvSetValor.setText(informacion.get("monto"));
            binding.tvSetComision.setText(informacion.get("comision"));

            //Ocultar
            binding.llNumeroTarjeta.setVisibility(View.GONE);
            binding.llCuotas.setVisibility(View.GONE);
            binding.llFechaExpiracion.setVisibility(View.GONE);
            binding.llNombre.setVisibility(View.GONE);

        } else if (informacion.get("accion").equals(Constantes.RETIRAR)) {
            //Cargar información de retirar
            binding.tvSetAccion.setText(informacion.get("accion"));
            binding.tvSetDocumento.setText(informacion.get("documentoAccion"));
            binding.tvSetValor.setText(informacion.get("monto"));
            binding.tvSetComision.setText(informacion.get("comision"));

            //Ocultar
            binding.llDocumentoRecibe.setVisibility(View.GONE);
            binding.llNumeroTarjeta.setVisibility(View.GONE);
            binding.llCuotas.setVisibility(View.GONE);
            binding.llFechaExpiracion.setVisibility(View.GONE);
            binding.llNombre.setVisibility(View.GONE);
        } else if (informacion.get("accion").equals(Constantes.PAGOTARJETA)) {
            //Cargar información de pagar con tarjeta
            binding.tvSetAccion.setText(informacion.get("accion"));
            binding.tvSetTarjeta.setText(informacion.get("numero_tarjeta"));
            binding.tvSetFecha.setText(informacion.get("fecha_expiracion"));
            binding.tvSetNombre.setText(informacion.get("nombre"));
            binding.tvSetValor.setText(informacion.get("valor"));
            binding.tvSetCuotas.setText(informacion.get("cuotas"));

            //Ocultar
            binding.llDocumentoRecibe.setVisibility(View.GONE);
            binding.llDocumentoAccion.setVisibility(View.GONE);
            binding.llComision.setVisibility(View.GONE);
        }else if (informacion.get("accion").equals(Constantes.CONSULTAR_SALDO)) {
            //Cargar información de consultar saldo
            binding.tvSetAccion.setText(informacion.get("accion"));
            binding.tvSetDocumento.setText(informacion.get("documento"));
            binding.tvSetComision.setText(informacion.get("comision"));

            //Ocultar
            binding.llCuotas.setVisibility(View.GONE);
            binding.llValor.setVisibility(View.GONE);
            binding.llNombre.setVisibility(View.GONE);
            binding.llFechaExpiracion.setVisibility(View.GONE);
            binding.llNumeroTarjeta.setVisibility(View.GONE);
            binding.llDocumentoRecibe.setVisibility(View.GONE);
        }else if (informacion.get("accion").equals(Constantes.TRANSFERIR)) {
            //Cargar información de transferir dinero
            binding.tvSetAccion.setText(informacion.get("accion"));
            binding.tvSetDocumento.setText(informacion.get("documentoTransfiere"));
            binding.tvSetDocumentoRecibe.setText(informacion.get("documentoRecibe"));
            binding.tvSetValor.setText(informacion.get("monto"));
            binding.tvSetComision.setText(informacion.get("comision"));

            //Ocultar
            binding.llCuotas.setVisibility(View.GONE);
            binding.llNombre.setVisibility(View.GONE);
            binding.llFechaExpiracion.setVisibility(View.GONE);
            binding.llNumeroTarjeta.setVisibility(View.GONE);
        }

        builder.setView(binding.getRoot());

        eventoBotones();

        return builder.create();
    }

    private void eventoBotones() {
        binding.btnConfirmar.setOnClickListener(view -> {
            callback.confirmarTransaccion("Confirmar");
            dismiss();
        });
        binding.btnCancelar.setOnClickListener(view -> {
            callback.confirmarTransaccion("Rechazar");
            dismiss();
        });
    }

}