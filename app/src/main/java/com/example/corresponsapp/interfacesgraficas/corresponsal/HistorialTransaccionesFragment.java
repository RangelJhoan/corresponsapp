package com.example.corresponsapp.interfacesgraficas.corresponsal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.corresponsapp.R;

public class HistorialTransaccionesFragment extends Fragment {

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
        return inflater.inflate(R.layout.fragment_historial_transacciones, container, false);
    }
}