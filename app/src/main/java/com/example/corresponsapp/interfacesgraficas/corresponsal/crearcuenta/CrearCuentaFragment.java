package com.example.corresponsapp.interfacesgraficas.corresponsal.crearcuenta;

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
import com.example.corresponsapp.databinding.FragmentCrearCuentaBinding;
import com.example.corresponsapp.entidades.Cliente;
import com.example.corresponsapp.entidades.CuentaBancaria;
import com.example.corresponsapp.entidades.Tarjeta;
import com.example.corresponsapp.validaciones.Validaciones;

public class CrearCuentaFragment extends Fragment implements CrearCuentaMVP.View {
    private FragmentCrearCuentaBinding binding;
    private CrearCuentaMVP.Presenter presenter;

    public CrearCuentaFragment() {

    }

    public static CrearCuentaFragment newInstance(String param1, String param2) {
        CrearCuentaFragment fragment = new CrearCuentaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCrearCuentaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new CrearCuentaPresenterImpl(this);

        binding.menuToolbar.ivPantalla.setImageResource(R.drawable.anadir_128);
        binding.menuToolbar.tvTitulo.setText("Crear Cuenta");

        binding.btnCrearCuenta.setOnClickListener(view1 -> {
            crearCuenta();
        });

    }

    private void crearCuenta() {
        EditText[] editTexts = {binding.etNombreCompleto,binding.etDocumento,binding.etPIN,binding.etConfirmarPIN, binding.etSaldoInicial};
        if(Validaciones.validarCampos(editTexts)){
            if (binding.etPIN.getText().equals(binding.etConfirmarPIN.getText())) {

                CuentaBancaria cuentaBancaria = new CuentaBancaria();
                Cliente cliente = new Cliente();
                Tarjeta tarjeta = new Tarjeta();

                //Crear cliente
                cliente.setDocumento(binding.etDocumento.getText().toString());
                cliente.setNombre_completo(binding.etNombreCompleto.getText().toString());

                //Crear tarjeta
                tarjeta.setFecha_expiracion("mañana");
                tarjeta.setCvv("1234");

                //Crear cuenta bancaria
                String numeroTarjeta = crearNumeroTarjeta(binding.etDocumento.getText().toString()); //Generar el número de la cuenta/tarjeta
                cuentaBancaria.setNumero_cuenta(numeroTarjeta);
                cuentaBancaria.setPIN(binding.etPIN.getText().toString());
                cuentaBancaria.setSaldo(Double.parseDouble(binding.etSaldoInicial.getText().toString()));
                cuentaBancaria.setCliente(cliente);
                cuentaBancaria.setTarjeta(tarjeta);

                presenter.crearCuenta(getContext(), cuentaBancaria);
            }
        }else{
            Toast.makeText(getContext(),"Por favor llene todos los datos",Toast.LENGTH_LONG).show();
        }
    }

    private String crearNumeroTarjeta(String documento) {
        int longitudDocumento = documento.length();
        String numeroTarjeta = "3" + documento;
        for (int i = longitudDocumento; i < 15; i++) {
            numeroTarjeta += String.valueOf((int) (Math.random() * 10));
        }
        return numeroTarjeta;
    }

    @Override
    public void mostrarResultado(String resultado) {

    }

    @Override
    public void mostrarError(String error) {

    }
}