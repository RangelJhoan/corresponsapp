package com.example.corresponsapp.interfacesgraficas.corresponsal.depositar;

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
import com.example.corresponsapp.databinding.FragmentDepositarBinding;
import com.example.corresponsapp.entidades.Cliente;
import com.example.corresponsapp.entidades.CuentaBancaria;
import com.example.corresponsapp.entidades.Deposito;
import com.example.corresponsapp.interfaces.ConfirmacionCallback;
import com.example.corresponsapp.interfaces.IAbrirDialogo;
import com.example.corresponsapp.utilidades.Constantes;
import com.example.corresponsapp.utilidades.Utilidades;

import java.util.Hashtable;

public class DepositarFragment extends Fragment implements DepositarMVP.View, ConfirmacionCallback {
    private FragmentDepositarBinding binding;
    private DepositarMVP.Presenter presenter;
    private NavController navController;
    private IAbrirDialogo iAbrirDialogo;
    private Activity actividad;

    public DepositarFragment() {

    }

    public static DepositarFragment newInstance(String param1, String param2) {
        DepositarFragment fragment = new DepositarFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDepositarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new DepositarPresenterImpl(this);
        navController = Navigation.findNavController(view);

        binding.menuToolbar.ivPantalla.setImageResource(R.drawable.banco_128);
        binding.menuToolbar.tvTitulo.setText(Constantes.DEPOSITAR);

        binding.btnDepositar.setOnClickListener(v -> {
            validarCampos();
        });

    }

    /**
     * M??todo que valida los campos y sus caracter??sticas y si la informaci??n es correcta, abre el di??logo de confirmaci??n de la acci??n
     */
    private void validarCampos() {
        EditText[] editTexts = {binding.etDocumentoEnvia, binding.etDocumentoRecibe, binding.etMonto};
        //Valida que los campos est??n llenos
        if (Utilidades.validarCampos(editTexts)) {
            //Valida que el documento que deposita sea num??rico
            if (Utilidades.validarSoloNumeros(binding.etDocumentoEnvia.getText().toString())) {
                //Valida que el documento que recibe el dep??sito sea de tipo num??rico
                if (Utilidades.validarSoloNumeros(binding.etDocumentoRecibe.getText().toString())) {
                    //Valida que el monto sea de tipo num??rico
                    if (Utilidades.validarSoloNumeros(binding.etMonto.getText().toString())) {
                        //El valor m??nimo de la transacci??n son 2000 pesos
                        if(Double.parseDouble(binding.etMonto.getText().toString()) >= 2000){
                            Hashtable<String, String> informacion = new Hashtable<>();
                            informacion.put("accion", Constantes.DEPOSITAR);
                            informacion.put("documentoAccion", binding.etDocumentoEnvia.getText().toString());
                            informacion.put("documentoRecibe", binding.etDocumentoRecibe.getText().toString());
                            informacion.put("monto", binding.etMonto.getText().toString());
                            informacion.put("comision", String.valueOf(Constantes.COMISION_DEPOSITAR));

                            iAbrirDialogo.abrirDialogo(informacion, this);
                        }else{
                            Toast.makeText(getContext(), "Dep??sito m??nimo: 1000", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "El monto a depositar debe ser de tipo num??rico", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "El documento recibe el dep??sito debe ser de tipo num??rico", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getContext(), "El documento que deposita debe ser de tipo num??rico", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Por favor llene todos los campos", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * M??todo que ejecuta el m??todo del Presentador para comunicarse con el Modelo.
     */
    private void depositarDinero() {
        Deposito deposito = new Deposito();
        Cliente cliente = new Cliente();
        CuentaBancaria cuentaBancaria = new CuentaBancaria();

        deposito.setDocumento(binding.etDocumentoEnvia.getText().toString());
        cliente.setDocumento(binding.etDocumentoRecibe.getText().toString());
        cuentaBancaria.setCliente(cliente);
        deposito.setCuentaBancaria(cuentaBancaria);
        deposito.setMonto(Double.parseDouble(binding.etMonto.getText().toString()));

        presenter.depositarDinero(getContext(), deposito);
    }

    @Override
    public void mostrarRespueta(String respuesta) {
        Toast.makeText(getContext(), respuesta, Toast.LENGTH_SHORT).show();
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
                depositarDinero();
                break;
            case "Rechazar":
                Toast.makeText(getContext(), "Dep??sito cancelado", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(getContext(), "??Error al depositar!", Toast.LENGTH_LONG).show();
                navController.navigate(R.id.depositarFragment);
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