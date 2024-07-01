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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.corresponsapp.R;
import com.example.corresponsapp.databinding.FragmentTransferirBinding;
import com.example.corresponsapp.entidades.Cliente;
import com.example.corresponsapp.entidades.CuentaBancaria;
import com.example.corresponsapp.entidades.Transferencia;
import com.example.corresponsapp.interfaces.ConfirmacionCallback;
import com.example.corresponsapp.interfaces.IAbrirDialogo;
import com.example.corresponsapp.utilidades.Constantes;
import com.example.corresponsapp.utilidades.Utilidades;

import java.util.Hashtable;

public class TransferirFragment extends Fragment implements TransferirMVP.View, ConfirmacionCallback {

    private FragmentTransferirBinding binding;
    private IAbrirDialogo iAbrirDialogo;
    private TransferirMVP.Presenter presenter;
    private NavController navController;

    public TransferirFragment() {

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
        navController = Navigation.findNavController(view);

        binding.menuToolbar.ivPantalla.setImageResource(R.drawable.transferir_128);
        binding.menuToolbar.tvTitulo.setText(Constantes.TRANSFERIR);

        binding.btnTransferir.setOnClickListener(view1 -> validarCampos());

    }

    /**
     * Valida que todos los campos contengan información correcta y abre el diálogo de confirmación
     */
    private void validarCampos() {
        EditText[] editTexts = {binding.etDocumentoRecibe, binding.etDocumentoTransfiere, binding.etPIN, binding.etConfirmarPIN, binding.etMonto};
        //Valida que todos los campos estén llenos
        if (Utilidades.validarCampos(editTexts)) {
            //Valida que los PINES coincidan
            if (binding.etPIN.getText().toString().equals(binding.etConfirmarPIN.getText().toString())) {
                //Valida que el documento sea de tipo numérico
                if (Utilidades.validarSoloNumeros(binding.etDocumentoTransfiere.getText().toString())) {
                    //Valida que el número PIN sea de tipo numérico
                    if (Utilidades.validarSoloNumeros(binding.etPIN.getText().toString())) {
                        //Valida que el documento que recibe la transferencia sea de tipo numérico
                        if (Utilidades.validarSoloNumeros(binding.etDocumentoRecibe.getText().toString())) {
                            //Valida que el monto sea de tipo numérico
                            if (Utilidades.validarSoloNumeros(binding.etMonto.getText().toString())) {
                                Hashtable<String, String> informacion = new Hashtable<>();
                                informacion.put("accion", Constantes.TRANSFERIR);
                                informacion.put("documentoTransfiere", binding.etDocumentoTransfiere.getText().toString());
                                informacion.put("documentoRecibe", binding.etDocumentoRecibe.getText().toString());
                                informacion.put("monto", binding.etMonto.getText().toString());
                                informacion.put("comision", String.valueOf(Constantes.COMISION_TRANSFERIR));
                                iAbrirDialogo.abrirDialogo(informacion, this);
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
        switch (confirmacion) {
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

    /**
     * Ejecuta el métido Transferir Dinero implementado en el Modelo
     */
    private void transferirDinero() {
        Transferencia transferencia = new Transferencia();
        CuentaBancaria cuentaBancariaTransfiere = new CuentaBancaria();
        CuentaBancaria cuentaBancariaRecibe = new CuentaBancaria();
        Cliente clienteTransfiere = new Cliente();
        Cliente clienteRecibe = new Cliente();
        clienteTransfiere.setDocumento(binding.etDocumentoTransfiere.getText().toString());
        cuentaBancariaTransfiere.setCliente(clienteTransfiere);
        cuentaBancariaTransfiere.setPin(binding.etPIN.getText().toString());
        clienteRecibe.setDocumento(binding.etDocumentoRecibe.getText().toString());
        cuentaBancariaRecibe.setCliente(clienteRecibe);

        transferencia.setMonto(Double.parseDouble(binding.etMonto.getText().toString()));
        transferencia.setCuentaTransfiere(cuentaBancariaTransfiere);
        transferencia.setCuentaRecibe(cuentaBancariaRecibe);

        presenter.transferirDinero(getContext(), transferencia);
    }

    @Override
    public void mostrarResultado(String resultado) {
        Toast.makeText(getContext(), resultado, Toast.LENGTH_SHORT).show();
        navController.navigate(R.id.corresponsalMenuFragment);
    }

    @Override
    public void mostrarError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            Activity actividad = (Activity) context;
            iAbrirDialogo = (IAbrirDialogo) actividad;
        }
    }
}