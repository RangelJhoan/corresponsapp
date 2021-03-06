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
     * M??todo que valida la informaci??n de la creaci??n de la cuenta del cliente
     */
    private void crearCuenta() {
        EditText[] editTexts = {binding.etNombreCompleto, binding.etDocumento, binding.etPIN, binding.etConfirmarPIN, binding.etSaldoInicial};
        //Validar que los campos tengan texto
        if (Utilidades.validarCampos(editTexts)) {
            //Nombre del cliente debe de estar en may??scula
            if (Utilidades.validarTextoMayuscula(binding.etNombreCompleto.getText().toString())) {
                //PIN debe ser tipo num??rico
                if(Utilidades.validarSoloNumeros(binding.etPIN.getText().toString())){
                    //N??mero de c??dula debe ser tipo num??rico
                    if(Utilidades.validarSoloNumeros(binding.etDocumento.getText().toString())){
                        //Se valida que el saldo inicial sea de tipo num??rico
                        if(Utilidades.validarSoloNumeros(binding.etSaldoInicial.getText().toString())){
                            //Se valida la confirmaci??n del PIN
                            if (binding.etPIN.getText().toString().equals(binding.etConfirmarPIN.getText().toString())) {
                                //Validar que el PIN sea de 4 d??gitos
                                if(binding.etPIN.getText().toString().length() == 4){
                                    CuentaBancaria cuentaBancaria = new CuentaBancaria();
                                    Cliente cliente = new Cliente();
                                    Tarjeta tarjeta = new Tarjeta();

                                    //Crear cliente
                                    cliente.setDocumento(binding.etDocumento.getText().toString());
                                    cliente.setNombreCompleto(binding.etNombreCompleto.getText().toString());

                                    //Crear tarjeta
                                    crearTarjeta(tarjeta);

                                    //Crear cuenta bancaria
                                    String numeroTarjeta = crearNumeroTarjeta(binding.etDocumento.getText().toString()); //Generar el n??mero de la cuenta/tarjeta
                                    cuentaBancaria.setNumeroCuenta(numeroTarjeta);
                                    cuentaBancaria.setPin(binding.etPIN.getText().toString());
                                    cuentaBancaria.setSaldo(Double.parseDouble(binding.etSaldoInicial.getText().toString()));
                                    cuentaBancaria.setCliente(cliente);
                                    cuentaBancaria.setTarjeta(tarjeta);

                                    presenter.crearCuenta(getContext(), cuentaBancaria);
                                }else{
                                    Toast.makeText(getContext(), "El n??mero PIN debe ser de 4 d??gitos", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getContext(), "El c??digo PIN no coincide", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getContext(), "Digite el saldo s??lo n??meros", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getContext(), "Digite documento s??lo n??meros", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getContext(), "Digite PIN s??lo n??meros", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getContext(), "Digite el nombre en may??sculas", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Por favor llene todos los datos", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * M??todo para crear la tarjeta que luego ser?? asignada al cliente que crea la cuenta
     * @param tarjeta Objeto tarjeta vac??o que ser?? llenado con la informaci??n creada en este m??todo
     */
    private void crearTarjeta(Tarjeta tarjeta) {
        //La fecha de expiraci??n se crea con 5 a??os de validez
        tarjeta.setFechaExpiracion(Utilidades.obtenerFechaExpiracion());
        //El c??digo CVV es de 4 d??gitos asignados aleatoriamente
        tarjeta.setCvv(crearCVV());
    }

    /**
     * M??todo para crear el c??digo CVV del cliente aleatoriamente
     * @return Retorna una cadena de caracteres con el c??digo CVV que se cre??
     */
    private String crearCVV() {
        StringBuilder cvv = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            cvv.append(String.valueOf((int) (Math.random() * 10)));
        }
        return cvv.toString();
    }


    /**
     * Crea el n??mero de la tarjeta que ser?? el mismo n??mero de la cuenta bancaria. Se crea apartir de un n??mero de franquicia, el n??mero de documento y dem??s n??meros aleatorios
     * @param documento Cadena de texto con el documento ingresado por el cliente
     * @return Retorna el n??mero de la tarjeta
     */
    private String crearNumeroTarjeta(String documento) {
        int longitudDocumento = documento.length();
        //El primer n??mero es un n??mero aleatorio entre 3 y 6 y se concatena su n??mero de documento
        String numeroTarjeta = (int) (Math.random()*4+3) + documento;
        //A la cadena anterior se le concatenan el resto de n??meros aleatorios para completar un total de 16 d??gitos
        for (int i = longitudDocumento; i < 15; i++) {
            numeroTarjeta += String.valueOf((int) (Math.random() * 10));
        }
        return numeroTarjeta;
    }

    /**
     * Muestra el resultado enviado desde el Modelo si se cumplieron con las dem??s validaciones
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