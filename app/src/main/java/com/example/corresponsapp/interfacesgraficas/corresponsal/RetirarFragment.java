package com.example.corresponsapp.interfacesgraficas.corresponsal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.corresponsapp.R;
import com.example.corresponsapp.databinding.FragmentRetirarBinding;

public class RetirarFragment extends Fragment {
    private FragmentRetirarBinding binding;

    public RetirarFragment() {

    }

    public static RetirarFragment newInstance(String param1, String param2) {
        RetirarFragment fragment = new RetirarFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRetirarBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.menuToolbar.ivPantalla.setImageResource(R.drawable.retirar_128);
        binding.menuToolbar.tvTitulo.setText("Retirar Dinero");

    }
}