package com.example.corresponsapp.interfacesgraficas.corresponsal.pagotarjeta;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.corresponsapp.databinding.FragmentPagoTarjetaBinding;
import com.example.corresponsapp.entidades.Cliente;
import com.example.corresponsapp.entidades.CuentaBancaria;
import com.example.corresponsapp.entidades.PagoTarjeta;
import com.example.corresponsapp.entidades.Tarjeta;
import com.example.corresponsapp.interfaces.ConfirmacionCallback;
import com.example.corresponsapp.interfaces.IAbrirDialogo;
import com.example.corresponsapp.utilidades.Constantes;
import com.example.corresponsapp.utilidades.Utilidades;

import java.util.Calendar;
import java.util.Hashtable;

public class PagoTarjetaFragment extends Fragment implements PagoTarjetaMVP.View, ConfirmacionCallback {

    private FragmentPagoTarjetaBinding binding;
    private IAbrirDialogo iAbrirDialogo;
    private PagoTarjetaMVP.Presenter presenter;
    private NavController navController;
    private char numeroInicialtarjeta;

    public PagoTarjetaFragment() {
        //Constructor vacío para iniciar el fragmento
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPagoTarjetaBinding.inflate(inflater, container, false);

        mostrarFranquiciaTarjeta();

        seleccionarFechaExpiracion();

        return binding.getRoot();
    }

    private void mostrarFranquiciaTarjeta() {
        binding.etNumeroTarjeta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    char[] charArray = editable.toString().toCharArray();
                    if (charArray[0] == '3') {
                        numeroInicialtarjeta = charArray[0];
                        binding.tilTarjeta.setHint("Franquicia: American Express");
                    } else if (charArray[0] == '4') {
                        binding.tilTarjeta.setHint("Franquicia: VISA");
                    } else if (charArray[0] == '5') {
                        binding.tilTarjeta.setHint("Franquicia: MasterCard");
                    } else if (charArray[0] == '6') {
                        binding.tilTarjeta.setHint("Franquicia: UnionPay");
                    } else {
                        binding.tilTarjeta.setHint("Número de tarjeta");
                        binding.etNumeroTarjeta.setError("Franquicia incorrecta");
                    }
                } else {
                    binding.tilTarjeta.setHint("Número de tarjeta");
                }
            }
        });
    }

    private void seleccionarFechaExpiracion() {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        binding.etFechaExpiracion.setKeyListener(null);
        binding.etFechaExpiracion.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, year1, month1, day1) -> {
                month1 = month1 + 1;
                String fecha = day1 + "/" + month1 + "/" + year1;
                binding.etFechaExpiracion.setText(fecha);
            }, year, month, day);
            datePickerDialog.show();
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        presenter = new PagoTarjetaPresenterImpl(this);

        binding.menuToolbar.ivPantalla.setImageResource(R.drawable.pagotarjeta_128);
        binding.menuToolbar.tvTitulo.setText(Constantes.PAGOTARJETA);

        binding.btnPagar.setOnClickListener(v -> abrirDialogo());

    }

    private void abrirDialogo() {
        EditText[] editTexts = {binding.etNumeroTarjeta, binding.etCVV, binding.etNombre, binding.etValor, binding.etCuotas};
        if (Utilidades.validarCampos(editTexts)) {
            if (numeroInicialtarjeta == '3' || numeroInicialtarjeta == '4' || numeroInicialtarjeta == '5' || numeroInicialtarjeta == '6') {
                if (Utilidades.validarSoloNumeros(binding.etNumeroTarjeta.getText().toString())) {
                    if (Utilidades.validarSoloNumeros(binding.etCVV.getText().toString())) {
                        if (Utilidades.validarTextoMayuscula(binding.etNombre.getText().toString())) {
                            if (Utilidades.validarSoloNumeros(binding.etValor.getText().toString())) {
                                if (Utilidades.validarSoloNumeros(binding.etCuotas.getText().toString())) {
                                    if (Integer.parseInt(binding.etValor.getText().toString()) >= 10000 && Integer.parseInt(binding.etValor.getText().toString()) <= 1000000) {
                                        if (Integer.parseInt(binding.etCuotas.getText().toString()) >= 1 && Integer.parseInt(binding.etCuotas.getText().toString()) <= 12) {

                                            Hashtable<String, String> informacion = new Hashtable<>();
                                            informacion.put("accion", Constantes.PAGOTARJETA);
                                            informacion.put("numero_tarjeta", binding.etNumeroTarjeta.getText().toString());
                                            informacion.put("fecha_expiracion", binding.etFechaExpiracion.getText().toString());
                                            informacion.put("nombre", binding.etNombre.getText().toString());
                                            informacion.put("valor", binding.etValor.getText().toString());
                                            informacion.put("cuotas", binding.etCuotas.getText().toString());

                                            iAbrirDialogo.abrirDialogo(informacion, this);

                                        } else {
                                            Toast.makeText(getContext(), "Número mínimo de cuotas: 1.\nNúnmero máximo de cuotas: 12", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "Valor mínimo a pagar 10000.\nValor máximo 1000000", Toast.LENGTH_LONG).show();
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
            } else {
                Toast.makeText(getContext(), "Número de franquicia no es válido", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Por favor llene todos los datos", Toast.LENGTH_LONG).show();
        }
    }

    private void pagarDinero() {
        PagoTarjeta pagoTarjeta = new PagoTarjeta();
        CuentaBancaria cuentaBancaria = new CuentaBancaria();
        Tarjeta tarjeta = new Tarjeta();
        Cliente cliente = new Cliente();

        cuentaBancaria.setNumeroCuenta(binding.etNumeroTarjeta.getText().toString());
        tarjeta.setFechaExpiracion(binding.etFechaExpiracion.getText().toString());
        tarjeta.setCvv(binding.etCVV.getText().toString());
        cliente.setNombreCompleto(binding.etNombre.getText().toString());
        pagoTarjeta.setValor(Double.parseDouble(binding.etValor.getText().toString()));
        pagoTarjeta.setNumeroCuotas(Integer.parseInt(binding.etCuotas.getText().toString()));

        cuentaBancaria.setCliente(cliente);
        cuentaBancaria.setTarjeta(tarjeta);
        pagoTarjeta.setCuentaBancaria(cuentaBancaria);

        presenter.pagarTarjeta(getContext(), pagoTarjeta);

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
            Activity actividad = (Activity) context;
            iAbrirDialogo = (IAbrirDialogo) actividad;
        }
    }
}