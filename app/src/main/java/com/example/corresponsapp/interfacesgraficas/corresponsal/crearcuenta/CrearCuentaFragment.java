package com.example.corresponsapp.interfacesgraficas.corresponsal.crearcuenta;

import android.annotation.SuppressLint;
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
import com.example.corresponsapp.databinding.FragmentCrearCuentaBinding;
import com.example.corresponsapp.entidades.Cliente;
import com.example.corresponsapp.entidades.CuentaBancaria;
import com.example.corresponsapp.entidades.Tarjeta;
import com.example.corresponsapp.utilidades.Constantes;
import com.example.corresponsapp.utilidades.Utilidades;
import com.example.corresponsapp.validaciones.Validaciones;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class CrearCuentaFragment extends Fragment implements CrearCuentaMVP.View {
    private FragmentCrearCuentaBinding binding;
    private CrearCuentaMVP.Presenter presenter;
    private NavController navController;

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

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        presenter = new CrearCuentaPresenterImpl(this);

        binding.menuToolbar.ivPantalla.setImageResource(R.drawable.anadir_128);
        binding.menuToolbar.tvTitulo.setText(Constantes.CREAR_CUENTA);

        binding.btnCrearCuenta.setOnClickListener(view1 -> {
            crearCuenta();
        });

    }

    /**
     * Método que valida la información de la creación de la cuenta del cliente
     */
    private void crearCuenta() {
        EditText[] editTexts = {binding.etNombreCompleto, binding.etDocumento, binding.etPIN, binding.etConfirmarPIN, binding.etSaldoInicial};
        //Validar que los campos tengan texto
        if (Validaciones.validarCampos(editTexts)) {
            //Nombre del cliente debe de estar en mayúscula
            if (Utilidades.validarTextoMayuscula(binding.etNombreCompleto.getText().toString())) {
                //PIN debe ser tipo numérico
                if(Utilidades.validarSoloNumeros(binding.etPIN.getText().toString())){
                    //Número de cédula debe ser tipo numérico
                    if(Utilidades.validarSoloNumeros(binding.etDocumento.getText().toString())){
                        //Se valida que el saldo inicial sea de tipo numérico
                        if(Utilidades.validarSoloNumeros(binding.etSaldoInicial.getText().toString())){
                            //Se valida la confirmación del PIN
                            if (binding.etPIN.getText().toString().equals(binding.etConfirmarPIN.getText().toString())) {

                                CuentaBancaria cuentaBancaria = new CuentaBancaria();
                                Cliente cliente = new Cliente();
                                Tarjeta tarjeta = new Tarjeta();

                                //Crear cliente
                                cliente.setDocumento(binding.etDocumento.getText().toString());
                                cliente.setNombre_completo(binding.etNombreCompleto.getText().toString());

                                //Crear tarjeta
                                crearTarjeta(tarjeta);

                                //Crear cuenta bancaria
                                String numeroTarjeta = crearNumeroTarjeta(binding.etDocumento.getText().toString()); //Generar el número de la cuenta/tarjeta
                                cuentaBancaria.setNumero_cuenta(numeroTarjeta);
                                cuentaBancaria.setPIN(binding.etPIN.getText().toString());
                                cuentaBancaria.setSaldo(Double.parseDouble(binding.etSaldoInicial.getText().toString()));
                                cuentaBancaria.setCliente(cliente);
                                cuentaBancaria.setTarjeta(tarjeta);

                                presenter.crearCuenta(getContext(), cuentaBancaria);
                            } else {
                                Toast.makeText(getContext(), "El código PIN no coincide", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getContext(), "Digite el saldo sólo números", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getContext(), "Digite documento sólo números", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getContext(), "Digite PIN sólo números", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getContext(), "Digite el nombre en mayúsculas", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Por favor llene todos los datos", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Método para crear la tarjeta que luego será asignada al cliente que crea la cuenta
     * @param tarjeta Objeto tarjeta vacío que será llenado con la información creada en este método
     */
    private void crearTarjeta(Tarjeta tarjeta) {
        //La fecha de expiración se crea con 5 años de validez
        tarjeta.setFecha_expiracion(Utilidades.obtenerFechaExpiracion());
        //El código CVV es de 4 dígitos asignados aleatoriamente
        tarjeta.setCvv(crearCVV());
    }

    /**
     * Método para crear el código CVV del cliente aleatoriamente
     * @return Retorna una cadena de caracteres con el código CVV que se creó
     */
    private String crearCVV() {
        StringBuilder cvv = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            cvv.append(String.valueOf((int) (Math.random() * 10)));
        }
        return cvv.toString();
    }


    /**
     * Crea el número de la tarjeta que será el mismo número de la cuenta bancaria. Se crea apartir de un número de franquicia, el número de documento y demás números aleatorios
     * @param documento Cadena de texto con el documento ingresado por el cliente
     * @return Retorna el número de la tarjeta
     */
    private String crearNumeroTarjeta(String documento) {
        int longitudDocumento = documento.length();
        //El primer número es un número aleatorio entre 3 y 6 y se concatena su número de documento
        String numeroTarjeta = (int) (Math.random()*4+3) + documento;
        //A la cadena anterior se le concatenan el resto de números aleatorios para completar un total de 16 dígitos
        for (int i = longitudDocumento; i < 15; i++) {
            numeroTarjeta += String.valueOf((int) (Math.random() * 10));
        }
        return numeroTarjeta;
    }

    /**
     * Muestra el resultado enviado desde el Modelo si se cumplieron con las demás validaciones
     * @param resultado Cadena de texto con el resultado enviado desde el Modelo
     */
    @Override
    public void mostrarResultado(String resultado) {
        Toast.makeText(getContext(), resultado, Toast.LENGTH_SHORT).show();
        navController.navigate(R.id.corresponsalMenuFragment);
    }

    @Override
    public void mostrarError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }
}