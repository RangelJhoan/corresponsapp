package com.example.corresponsapp.interfacesgraficas.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.corresponsapp.R;
import com.example.corresponsapp.databinding.FragmentDialogOpcCorBinding;
import com.example.corresponsapp.utilidades.Sesion;

public class DialogOpcCorFragment extends DialogFragment {

    private FragmentDialogOpcCorBinding binding;
    private View navView;
    private NavController navController;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public DialogOpcCorFragment(View view) {
        navController = Navigation.findNavController(view);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return crearDialogo();
    }

    private AlertDialog crearDialogo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        binding = FragmentDialogOpcCorBinding.inflate(getActivity().getLayoutInflater());
        builder.setView(binding.getRoot());

        inicializarPreference();

        eventoBotones();
        return builder.create();
    }

    private void inicializarPreference() {

    }

    private void eventoBotones() {
        binding.btnCambiarClave.setOnClickListener(view -> {
            navController.navigate(R.id.corresponsalCambiarClaveFragment);
            dismiss();
        });
        binding.btnSalir.setOnClickListener(view -> {

            cerrarSesion();

            getActivity().finish();
            dismiss();
        });
    }

    private void cerrarSesion() {
        Sesion.corresponsalSesion = null;
        editor.putBoolean(Sesion.LLAVE_SESION_INICIADA, false);
        editor.putInt(Sesion.LLAVE_ID, 0);
        editor.putString(Sesion.LLAVE_NOMBRE, null);
        editor.putFloat(Sesion.LLAVE_SALDO, 0);
        editor.apply();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        preferences = context.getSharedPreferences("sesion", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
    }

}