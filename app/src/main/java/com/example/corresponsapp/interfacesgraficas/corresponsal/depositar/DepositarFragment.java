package com.example.corresponsapp.interfacesgraficas.corresponsal.depositar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.corresponsapp.R;
import com.example.corresponsapp.databinding.FragmentDepositarBinding;

public class DepositarFragment extends Fragment {
    private FragmentDepositarBinding binding;

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
        binding = FragmentDepositarBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.menuToolbar.ivPantalla.setImageResource(R.drawable.banco_128);
        binding.menuToolbar.tvTitulo.setText("Depositar Dinero");

    }
}