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

    /**
     * Método que realiza las validaciones necesarias para abrir el diálogo de confirmación
     */
    private void validarInformacion() {
        EditText[] editTexts = {binding.etDocumento, binding.etPIN, binding.etConfirmarPIN};
        //Se valida que todos los campos están llenos
        if (Utilidades.validarCampos(editTexts)) {
            //Se valida que los código PIN coincidan
            if(binding.etPIN.getText().toString().equals(binding.etConfirmarPIN.getText().toString())){
                //Se valida que el número de documento sea de tipo numérico
                if(Utilidades.validarSoloNumeros(binding.etDocumento.getText().toString())){
                    //Se valida que el código PIN ingresado sea de tipo numérico
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

    /**
     * Abre el diálogo de confirmación de la transacción
     */
    private void abrirDialogo() {
        Hashtable<String, String> informacion = new Hashtable<>();
        informacion.put("accion", Constantes.CONSULTAR_SALDO);
        informacion.put("documento", binding.etDocumento.getText().toString());
        informacion.put("comision", String.valueOf(Constantes.COMISION_CONSULTAR_SALDO));
        iAbrirDialogo.abrirDialogo(informacion, this);
    }

    /**
     * A través del presenter ejecuta el método consultar saldo para luego mostrar en pantalla el saldo actual del cliente
     */
    private void consultarSaldo() {
        CuentaBancaria cuentaBancaria = new CuentaBancaria();
        Cliente cliente = new Cliente();

        cliente.setDocumento(binding.etDocumento.getText().toString());
        cuentaBancaria.setPin(binding.etPIN.getText().toString());
        cuentaBancaria.setCliente(cliente);

        presenter.consultarSaldo(getContext(), cuentaBancaria);
    }

    /**
     * Método de la Vista donde se muestra el saldo consultado del cliente
     * @param saldo Parámetro que recibe el saldo actual del cliente
     */
    @Override
    public void mostrarSaldo(String saldo) {
        binding.tvSaldoDisponible.setText("$" + saldo);
        binding.etPIN.setText("");
        binding.etConfirmarPIN.setText("");
    }

    /**
     * Método de la Vista donde se muestra algún error encontrado en el Modelo
     * @param error Parámetro que obtiene el error que se encontró y se muestra en pantalla
     */
    @Override
    public void mostrarError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
        binding.tvSaldoDisponible.setText("");
    }

    /**
     * Obtiene la opción escogida por el cliente. Si el cliente confirma la consulta se raliza la consulta, si no se cerrará el diálogo y no se ejecutará ninguna acción
     * @param confirmacion Parámetro que obtiene la opción que seleccionó el cliente
     */
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

    /**
     * Se inicializa la apertura del diálogo a través de la implementación en la actividad del corresponsal
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Activity){
            actividad = (Activity) context;
            iAbrirDialogo = (IAbrirDialogo) actividad;
        }
    }
}