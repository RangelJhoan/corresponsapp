package com.example.corresponsapp.interfacesgraficas.corresponsal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.corresponsapp.R;
import com.example.corresponsapp.databinding.FragmentPagoTarjetaBinding;

public class PagoTarjetaFragment extends Fragment {

    private FragmentPagoTarjetaBinding binding;

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
        binding = FragmentPagoTarjetaBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.menuToolbar.ivPantalla.setImageResource(R.drawable.pagotarjeta_128);
        binding.menuToolbar.tvTitulo.setText("Pago con Tarjeta");

    }
}