package com.example.corresponsapp.interfacesgraficas.corresponsal.retirar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.corresponsapp.R;
import com.example.corresponsapp.databinding.FragmentRetirarBinding;
import com.example.corresponsapp.entidades.Cliente;
import com.example.corresponsapp.entidades.Retiro;
import com.example.corresponsapp.validaciones.Validaciones;

public class RetirarFragment extends Fragment implements RetirarMVP.View{
    private RetirarMVP.Presenter presenter;
    private FragmentRetirarBinding binding;

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
        binding = FragmentRetirarBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new RetirarPresenterImpl(this);

        binding.menuToolbar.ivPantalla.setImageResource(R.drawable.retirar_128);
        binding.menuToolbar.tvTitulo.setText("Retirar Dinero");

        binding.btnRetirar.setOnClickListener(v -> {
            retirarDinero();
        });

    }

    private void retirarDinero() {
        EditText[] editTexts = {binding.etDocumento, binding.etPIN, binding.etConfirmarPIN, binding.etMonto};

        if(Validaciones.validarCampos(editTexts)){
            if(binding.etPIN.getText().toString().equals(binding.etConfirmarPIN.getText().toString())){
                Retiro retiro = new Retiro();
                Cliente cliente = new Cliente();

                cliente.setDocumento(binding.etDocumento.getText().toString());
                retiro.setMonto(Double.parseDouble(binding.etMonto.getText().toString()));
                retiro.setCliente(cliente);

                presenter.retirarDinero(getContext(), retiro);
            }else{
                Toast.makeText(getContext(), "Código PIN no coincide",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getContext(), "Por favor llene todos los campos",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void mostrarResultado(String resultado) {

    }

    @Override
    public void mostrarError(String error) {

    }
}