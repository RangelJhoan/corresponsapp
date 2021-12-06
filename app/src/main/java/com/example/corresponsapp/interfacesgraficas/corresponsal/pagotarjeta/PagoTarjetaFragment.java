package com.example.corresponsapp.interfacesgraficas.corresponsal.pagotarjeta;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.corresponsapp.R;
import com.example.corresponsapp.databinding.FragmentPagoTarjetaBinding;
import com.example.corresponsapp.entidades.Cliente;
import com.example.corresponsapp.entidades.CuentaBancaria;
import com.example.corresponsapp.entidades.PagoTarjeta;
import com.example.corresponsapp.entidades.Tarjeta;
import com.example.corresponsapp.interfaces.ConfirmacionCallback;
import com.example.corresponsapp.interfaces.IAbrirDialogo;
import com.example.corresponsapp.utilidades.Constantes;
import com.example.corresponsapp.utilidades.Utilidades;
import com.example.corresponsapp.validaciones.Validaciones;

import java.util.Hashtable;

public class PagoTarjetaFragment extends Fragment implements PagoTarjetaMVP.View, ConfirmacionCallback {

    private FragmentPagoTarjetaBinding binding;
    private IAbrirDialogo iAbrirDialogo;
    private Activity actividad;
    private PagoTarjetaMVP.Presenter presenter;
    private NavController navController;

    public PagoTarjetaFragment() {

    }

    public static PagoTarjetaFragment newInstance(String param1, String param2) {
        PagoTarjetaFragment fragment = new PagoTarjetaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPagoTarjetaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        presenter = new PagoTarjetaPresenterImpl(this);

        binding.menuToolbar.ivPantalla.setImageResource(R.drawable.pagotarjeta_128);
        binding.menuToolbar.tvTitulo.setText(Constantes.PAGOTARJETA);

        binding.btnPagar.setOnClickListener(v -> {
            abrirDialogo();
        });

    }

    private void abrirDialogo() {
        if (Utilidades.validarSoloNumeros(binding.etNumeroTarjeta.getText().toString())) {
            if (Utilidades.validarSoloNumeros(binding.etCVV.getText().toString())) {
                if (Utilidades.validarTextoMayuscula(binding.etNombre.getText().toString())) {
                    if (Utilidades.validarSoloNumeros(binding.etValor.getText().toString())) {
                        if (Utilidades.validarSoloNumeros(binding.etCuotas.getText().toString())) {
                            if (Integer.parseInt(binding.etCuotas.getText().toString()) >= 1 && Integer.parseInt(binding.etCuotas.getText().toString()) <= 12) {
                                Hashtable<String, String> informacion = new Hashtable<>();
                                informacion.put("accion", Constantes.PAGOTARJETA);
                                informacion.put("numeroTarjeta", binding.etNumeroTarjeta.getText().toString());
                                informacion.put("fechaExpiracion", binding.etFechaExpiracion.getText().toString());
                                informacion.put("nombre", binding.etNombre.getText().toString());
                                informacion.put("valor", binding.etValor.getText().toString());
                                informacion.put("numeroCuotas", binding.etCuotas.getText().toString());

                                iAbrirDialogo.abrirDialogo(informacion, this);

                            } else {
                                Toast.makeText(getContext(), "Número mínimo de cuotas: 1.\nNúnmero máximo de cuotas: 12", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Digite Cuotas sólo números", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Digite Valor a pagar sólo números", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Digite Nombre sólo mayúsculas", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getContext(), "Digite CVV sólo números", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Digite Número de Tarjeta sólo números", Toast.LENGTH_LONG).show();
        }
    }

    private void pagarDinero() {
        EditText[] editTexts = {binding.etNumeroTarjeta, binding.etFechaExpiracion, binding.etCVV, binding.etNombre, binding.etValor, binding.etCuotas};
        if (Validaciones.validarCampos(editTexts)) {
            PagoTarjeta pagoTarjeta = new PagoTarjeta();
            CuentaBancaria cuentaBancaria = new CuentaBancaria();
            Tarjeta tarjeta = new Tarjeta();
            Cliente cliente = new Cliente();

            cuentaBancaria.setNumero_cuenta(binding.etNumeroTarjeta.getText().toString());
            tarjeta.setFecha_expiracion(binding.etFechaExpiracion.getText().toString());
            tarjeta.setCvv(binding.etCVV.getText().toString());
            cliente.setNombre_completo(binding.etNombre.getText().toString());
            pagoTarjeta.setValor(Double.parseDouble(binding.etValor.getText().toString()));
            pagoTarjeta.setNumeroCuotas(Integer.parseInt(binding.etCuotas.getText().toString()));

            cuentaBancaria.setCliente(cliente);
            cuentaBancaria.setTarjeta(tarjeta);
            pagoTarjeta.setCuentaBancaria(cuentaBancaria);

            presenter.pagarTarjeta(getContext(), pagoTarjeta);
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
                pagarDinero();
                break;
            case "Rechazar":
                Toast.makeText(getContext(), "Pago cancelado", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(getContext(), "¡Error al pagar!", Toast.LENGTH_LONG).show();
                break;
        }
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