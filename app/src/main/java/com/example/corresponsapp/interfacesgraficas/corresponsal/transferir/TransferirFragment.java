package com.example.corresponsapp.interfacesgraficas.corresponsal.transferir;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.corresponsapp.R;
import com.example.corresponsapp.databinding.FragmentTransferirBinding;
import com.example.corresponsapp.entidades.Transferencia;
import com.example.corresponsapp.interfaces.ConfirmacionCallback;
import com.example.corresponsapp.interfaces.IAbrirDialogo;
import com.example.corresponsapp.utilidades.Constantes;
import com.example.corresponsapp.utilidades.Utilidades;
import com.example.corresponsapp.validaciones.Validaciones;

import java.util.Hashtable;

public class TransferirFragment extends Fragment implements TransferirMVP.View, ConfirmacionCallback {

    private FragmentTransferirBinding binding;
    private Activity actividad;
    private IAbrirDialogo iAbrirDialogo;
    private TransferirMVP.Presenter presenter;

    public TransferirFragment() {

    }

    public static TransferirFragment newInstance(String param1, String param2) {
        TransferirFragment fragment = new TransferirFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTransferirBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new TransferirPresenterImpl(this);

        binding.menuToolbar.ivPantalla.setImageResource(R.drawable.transferir_128);
        binding.menuToolbar.tvTitulo.setText(Constantes.TRANSFERIR);

        binding.btnTransferir.setOnClickListener(view1 -> {
            validarCampos();
        });

    }

    private void validarCampos() {
        EditText[] editTexts = {binding.etDocumentoRecibe, binding.etDocumentoTransfiere, binding.etPIN, binding.etConfirmarPIN, binding.etMonto};
        if (Validaciones.validarCampos(editTexts)) {
            if (binding.etPIN.getText().toString().equals(binding.etConfirmarPIN.getText().toString())) {
                if (Utilidades.validarSoloNumeros(binding.etDocumentoTransfiere.getText().toString())) {
                    if (Utilidades.validarSoloNumeros(binding.etPIN.getText().toString())) {
                        if (Utilidades.validarSoloNumeros(binding.etDocumentoRecibe.getText().toString())) {
                            if (Utilidades.validarSoloNumeros(binding.etMonto.getText().toString())) {
                                Hashtable<String, String> información = new Hashtable<>();
                                información.put("accion", Constantes.TRANSFERIR);
                                información.put("documentoTransfiere", binding.etDocumentoTransfiere.getText().toString());
                                información.put("documentoRecibe", binding.etDocumentoRecibe.getText().toString());
                                información.put("monto", binding.etMonto.getText().toString());
                                información.put("comision", String.valueOf(Constantes.COMISION_TRANSFERIR));
                                iAbrirDialogo.abrirDialogo(información, this);
                            } else {
                                Toast.makeText(getContext(), "Monto a transferir debe ser tipo numérico", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Número de documento del cliente que recibe la transferencia debe ser tipo numérico", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Número PIN debe ser tipo numérico", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Número de documento del cliente que transfiere debe ser tipo numérico", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getContext(), "Código PIN no coindice", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Por favor llene todos los campos", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void confirmarTransaccion(String confirmacion) {
        switch (confirmacion){
            case "Confirmar":
                transferirDinero();
                break;
            case "Rechazar":
                Toast.makeText(getContext(), "Transferencia cancelada", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(getContext(), "¡Error! Transferencia no realizada", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void transferirDinero() {
        Transferencia transferencia = new Transferencia();
        transferencia.setMonto(Double.parseDouble(binding.etMonto.getText().toString()));
        transferencia.getCuentaTransfiere().getCliente().setDocumento(binding.etDocumentoTransfiere.getText().toString());
        transferencia.getCuentaTransfiere().setPIN(binding.etPIN.getText().toString());
        transferencia.getCuentaRecibe().getCliente().setDocumento(binding.etDocumentoRecibe.getText().toString());

        presenter.transferirDinero(getContext(),transferencia);
    }

    @Override
    public void mostrarResultado(String resultado) {
        Toast.makeText(getContext(), resultado, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mostrarError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            actividad = (Activity) context;
            iAbrirDialogo = (IAbrirDialogo) actividad;
        }
    }
}