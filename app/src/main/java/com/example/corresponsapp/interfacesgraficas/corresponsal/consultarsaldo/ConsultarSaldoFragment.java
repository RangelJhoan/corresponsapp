package com.example.corresponsapp.interfacesgraficas.corresponsal.consultarsaldo;

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
import com.example.corresponsapp.databinding.FragmentConsultarSaldoBinding;
import com.example.corresponsapp.entidades.Cliente;
import com.example.corresponsapp.entidades.CuentaBancaria;
import com.example.corresponsapp.interfaces.ConfirmacionCallback;
import com.example.corresponsapp.interfaces.IAbrirDialogo;
import com.example.corresponsapp.utilidades.Constantes;
import com.example.corresponsapp.utilidades.Utilidades;
import com.example.corresponsapp.validaciones.Validaciones;

import java.util.Hashtable;

public class ConsultarSaldoFragment extends Fragment implements ConsultarSaldoMVP.View, ConfirmacionCallback {

    private FragmentConsultarSaldoBinding binding;
    private Activity actividad;

    private ConsultarSaldoMVP.Presenter presenter;
    private IAbrirDialogo iAbrirDialogo;

    public ConsultarSaldoFragment() {

    }

    public static ConsultarSaldoFragment newInstance() {
        return new ConsultarSaldoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentConsultarSaldoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.menuToolbar.tvTitulo.setText(Constantes.CONSULTAR_SALDO);
        binding.menuToolbar.ivPantalla.setImageResource(R.drawable.consultar_saldo_128);
        presenter = new ConsultarSaldoPresenterImpl(this);

        binding.btnConsultarSaldo.setOnClickListener(view1 -> {
            validarInformacion();
        });

    }

    private void validarInformacion() {
        EditText[] editTexts = {binding.etDocumento, binding.etPIN, binding.etConfirmarPIN};
        if (Validaciones.validarCampos(editTexts)) {
            if(binding.etPIN.getText().toString().equals(binding.etConfirmarPIN.getText().toString())){
                if(Utilidades.validarSoloNumeros(binding.etDocumento.getText().toString())){
                    if(Utilidades.validarSoloNumeros(binding.etPIN.getText().toString())){
                        abrirDialogo();
                    }else{
                        Toast.makeText(getContext(), "Número de PIN debe ser tipo numérico", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getContext(), "Número de Documento debe ser tipo numérico", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getContext(), "Número PIN no coincide", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Por favor llene todos los datos", Toast.LENGTH_LONG).show();
        }
    }

    private void abrirDialogo() {
        Hashtable<String, String> informacion = new Hashtable<>();
        informacion.put("accion", Constantes.CONSULTAR_SALDO);
        informacion.put("documento", binding.etDocumento.getText().toString());
        informacion.put("comision", String.valueOf(Constantes.COMISION_CONSULTAR_SALDO));
        iAbrirDialogo.abrirDialogo(informacion, this);
    }

    private void consultarSaldo() {
        CuentaBancaria cuentaBancaria = new CuentaBancaria();
        Cliente cliente = new Cliente();

        cliente.setDocumento(binding.etDocumento.getText().toString());
        cuentaBancaria.setPIN(binding.etPIN.getText().toString());
        cuentaBancaria.setCliente(cliente);

        presenter.consultarSaldo(getContext(), cuentaBancaria);
    }

    @Override
    public void mostrarSaldo(String saldo) {
        binding.tvSaldoDisponible.setText("$" + saldo);
    }

    @Override
    public void mostrarError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
        binding.tvSaldoDisponible.setText("");
    }

    @Override
    public void confirmarTransaccion(String confirmacion) {
        switch (confirmacion){
            case "Confirmar":
                consultarSaldo();
                break;
            case "Rechazar":
                Toast.makeText(getContext(), "Consulta cancelada", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(getContext(), "¡Error! Consulta no realizada", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Activity){
            actividad = (Activity) context;
            iAbrirDialogo = (IAbrirDialogo) actividad;
        }
    }
}