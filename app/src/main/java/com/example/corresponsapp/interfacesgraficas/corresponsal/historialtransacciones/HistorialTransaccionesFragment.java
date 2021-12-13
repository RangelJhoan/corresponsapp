package com.example.corresponsapp.interfacesgraficas.corresponsal.historialtransacciones;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.corresponsapp.R;
import com.example.corresponsapp.databinding.FragmentHistorialTransaccionesBinding;
import com.example.corresponsapp.entidades.ConsultaSaldo;
import com.example.corresponsapp.entidades.CuentaCreada;
import com.example.corresponsapp.entidades.Deposito;
import com.example.corresponsapp.entidades.PagoTarjeta;
import com.example.corresponsapp.entidades.Retiro;
import com.example.corresponsapp.entidades.Transferencia;
import com.example.corresponsapp.interfacesgraficas.adaptadores.AdaptadorInfoCorta;

import java.util.ArrayList;

public class HistorialTransaccionesFragment extends Fragment implements HistorialMVP.View {

    private FragmentHistorialTransaccionesBinding binding;
    private HistorialMVP.Presenter presenter;
    private AdaptadorInfoCorta adaptadorInfoCorta;

    public HistorialTransaccionesFragment() {

    }

    public static HistorialTransaccionesFragment newInstance(String param1, String param2) {
        HistorialTransaccionesFragment fragment = new HistorialTransaccionesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHistorialTransaccionesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new HistorialPresenterImpl(this);
        presenter.consultarHistorialTransacciones(getContext());

    }

    /**
     * Método que recibe una lista de Libros enviada desde el Modelo
     *
     * @param listaRetiros
     */
    @Override
    public void mostrarRetiros(ArrayList<Retiro> listaRetiros) {
        binding.rvRetiro.setLayoutManager(new LinearLayoutManager(getContext()));
        //Se envía la lista de los retiros al adaptador
        adaptadorInfoCorta = new AdaptadorInfoCorta(listaRetiros, null, null, null, null, null);
        binding.rvRetiro.setAdapter(adaptadorInfoCorta);
    }

    @Override
    public void mostrarDepositos(ArrayList<Deposito> listaDepositos) {
        binding.rvDeposito.setLayoutManager(new LinearLayoutManager(getContext()));
        //Se envía la lista de depósitos al adaptador
        adaptadorInfoCorta = new AdaptadorInfoCorta(null, listaDepositos, null, null, null, null);
        binding.rvDeposito.setAdapter(adaptadorInfoCorta);
    }

    @Override
    public void mostrarPagosTarjeta(ArrayList<PagoTarjeta> listaPagoTarjetas) {
        binding.rvPagoTarjeta.setLayoutManager(new LinearLayoutManager(getContext()));
        //Se envía la lista de pagos con tarjeta al adaptador
        adaptadorInfoCorta = new AdaptadorInfoCorta(null, null, listaPagoTarjetas, null, null, null);
        binding.rvPagoTarjeta.setAdapter(adaptadorInfoCorta);
    }

    @Override
    public void mostrarTransferencias(ArrayList<Transferencia> listaTransferencias) {
        binding.rvTransferencia.setLayoutManager(new LinearLayoutManager(getContext()));
        //Se envía la lista de transferencias al adaptador
        adaptadorInfoCorta = new AdaptadorInfoCorta(null, null, null, listaTransferencias, null, null);
        binding.rvTransferencia.setAdapter(adaptadorInfoCorta);
    }

    @Override
    public void mostrarConsultasSaldo(ArrayList<ConsultaSaldo> listaConsultasSaldo) {
        binding.rvConsultasSaldo.setLayoutManager(new LinearLayoutManager(getContext()));
        //Se envía la lista de consultas de saldo al adaptador
        adaptadorInfoCorta = new AdaptadorInfoCorta(null, null, null, null, listaConsultasSaldo, null);
        binding.rvConsultasSaldo.setAdapter(adaptadorInfoCorta);
    }

    @Override
    public void mostrarCuentasCreadas(ArrayList<CuentaCreada> listaCuentasCreadas) {
        binding.rvCuentasCreada.setLayoutManager(new LinearLayoutManager(getContext()));
        //Se envía la lista de cuentas creadas al adaptador
        adaptadorInfoCorta = new AdaptadorInfoCorta(null, null, null, null, null, listaCuentasCreadas);
        binding.rvCuentasCreada.setAdapter(adaptadorInfoCorta);
    }
}