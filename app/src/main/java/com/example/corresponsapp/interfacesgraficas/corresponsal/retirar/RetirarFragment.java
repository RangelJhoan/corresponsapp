package com.example.corresponsapp.interfacesgraficas.corresponsal.retirar;

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
import com.example.corresponsapp.databinding.FragmentRetirarBinding;
import com.example.corresponsapp.entidades.Cliente;
import com.example.corresponsapp.entidades.CuentaBancaria;
import com.example.corresponsapp.entidades.Retiro;
import com.example.corresponsapp.interfaces.ConfirmacionCallback;
import com.example.corresponsapp.interfaces.IAbrirDialogo;
import com.example.corresponsapp.utilidades.Constantes;
import com.example.corresponsapp.utilidades.Utilidades;
import com.example.corresponsapp.validaciones.Validaciones;

import java.util.Hashtable;

public class RetirarFragment extends Fragment implements RetirarMVP.View, ConfirmacionCallback {
    private RetirarMVP.Presenter presenter;
    private FragmentRetirarBinding binding;
    private NavController navController;
    private Activity actividad;
    private IAbrirDialogo iAbrirDialogo;

    public RetirarFragment() {

    }

    public static RetirarFragment newInstance(String param1, String param2) {
        RetirarFragment fragment = new RetirarFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRetirarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new RetirarPresenterImpl(this);
        navController = Navigation.findNavController(view);

        binding.menuToolbar.ivPantalla.setImageResource(R.drawable.retirar_128);
        binding.menuToolbar.tvTitulo.setText(Constantes.RETIRAR);

        binding.btnRetirar.setOnClickListener(v -> {
            abrirDialogo();
        });

    }

    private void abrirDialogo() {
        EditText[] editTexts = {binding.etDocumento, binding.etPIN, binding.etConfirmarPIN, binding.etMonto};
        if (Validaciones.validarCampos(editTexts)) {
            if (Utilidades.validarSoloNumeros(binding.etDocumento.getText().toString())) {
                if (Utilidades.validarSoloNumeros(binding.etPIN.getText().toString())) {
                    if (Utilidades.validarSoloNumeros(binding.etMonto.getText().toString())) {
                        if(binding.etPIN.getText().toString().equals(binding.etConfirmarPIN.getText().toString())){
                            Hashtable<String, String> informacion = new Hashtable<>();
                            informacion.put("accion", Constantes.RETIRAR);
                            informacion.put("documentoAccion", binding.etDocumento.getText().toString());
                            informacion.put("monto", binding.etMonto.getText().toString());
                            informacion.put("comision", String.valueOf(Constantes.COMISION_RETIRAR));
                            iAbrirDialogo.abrirDialogo(informacion, this);
                        }else{
                            Toast.makeText(getContext(), "Número PIN no coincide", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Digite el monto sólo números", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Digite PIN sólo números", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(getContext(), "Digite documento sólo números", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Por favor llene todos los campos", Toast.LENGTH_LONG).show();
        }
    }

    private void retirarDinero() {
        EditText[] editTexts = {binding.etDocumento, binding.etPIN, binding.etConfirmarPIN, binding.etMonto};

        if (Validaciones.validarCampos(editTexts)) {
            if (binding.etPIN.getText().toString().equals(binding.etConfirmarPIN.getText().toString())) {
                Retiro retiro = new Retiro();
                Cliente cliente = new Cliente();
                CuentaBancaria cuentaBancaria = new CuentaBancaria();
                cuentaBancaria.setCliente(cliente);
                cuentaBancaria.setPIN(binding.etPIN.getText().toString());

                cliente.setDocumento(binding.etDocumento.getText().toString());
                retiro.setMonto(Double.parseDouble(binding.etMonto.getText().toString()));
                retiro.setCuentaBancaria(cuentaBancaria);

                presenter.retirarDinero(getContext(), retiro);
            } else {
                Toast.makeText(getContext(), "Código PIN no coincide", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Por favor llene todos los campos", Toast.LENGTH_LONG).show();
        }

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
    public void confirmarTransaccion(String confirmacion) {
        switch (confirmacion) {
            case "Confirmar":
                retirarDinero();
                break;
            case "Rechazar":
                Toast.makeText(getContext(), "Retiro cancelado", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(getContext(), "¡Error al retirar!", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.actividad = (Activity) context;
            iAbrirDialogo = (IAbrirDialogo) actividad;
        }
    }
}