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
     * M??todo que realiza las validaciones necesarias para abrir el di??logo de confirmaci??n
     */
    private void validarInformacion() {
        EditText[] editTexts = {binding.etDocumento, binding.etPIN, binding.etConfirmarPIN};
        //Se valida que todos los campos est??n llenos
        if (Utilidades.validarCampos(editTexts)) {
            //Se valida que los c??digo PIN coincidan
            if(binding.etPIN.getText().toString().equals(binding.etConfirmarPIN.getText().toString())){
                //Se valida que el n??mero de documento sea de tipo num??rico
                if(Utilidades.validarSoloNumeros(binding.etDocumento.getText().toString())){
                    //Se valida que el c??digo PIN ingresado sea de tipo num??rico
                    if(Utilidades.validarSoloNumeros(binding.etPIN.getText().toString())){
                        abrirDialogo();
                    }else{
                        Toast.makeText(getContext(), "N??mero de PIN debe ser tipo num??rico", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getContext(), "N??mero de Documento debe ser tipo num??rico", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getContext(), "N??mero PIN no coincide", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Por favor llene todos los datos", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Abre el di??logo de confirmaci??n de la transacci??n
     */
    private void abrirDialogo() {
        Hashtable<String, String> informacion = new Hashtable<>();
        informacion.put("accion", Constantes.CONSULTAR_SALDO);
        informacion.put("documento", binding.etDocumento.getText().toString());
        informacion.put("comision", String.valueOf(Constantes.COMISION_CONSULTAR_SALDO));
        iAbrirDialogo.abrirDialogo(informacion, this);
    }

    /**
     * A trav??s del presenter ejecuta el m??todo consultar saldo para luego mostrar en pantalla el saldo actual del cliente
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
     * M??todo de la Vista donde se muestra el saldo consultado del cliente
     * @param saldo Par??metro que recibe el saldo actual del cliente
     */
    @Override
    public void mostrarSaldo(String saldo) {
        binding.tvSaldoDisponible.setText("$" + saldo);
        binding.etPIN.setText("");
        binding.etConfirmarPIN.setText("");
    }

    /**
     * M??todo de la Vista donde se muestra alg??n error encontrado en el Modelo
     * @param error Par??metro que obtiene el error que se encontr?? y se muestra en pantalla
     */
    @Override
    public void mostrarError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
        binding.tvSaldoDisponible.setText("");
    }

    /**
     * Obtiene la opci??n escogida por el cliente. Si el cliente confirma la consulta se raliza la consulta, si no se cerrar?? el di??logo y no se ejecutar?? ninguna acci??n
     * @param confirmacion Par??metro que obtiene la opci??n que seleccion?? el cliente
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
                Toast.makeText(getContext(), "??Error! Consulta no realizada", Toast.LENGTH_LONG).show();
                break;
        }
    }

    /**
     * Se inicializa la apertura del di??logo a trav??s de la implementaci??n en la actividad del corresponsal
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