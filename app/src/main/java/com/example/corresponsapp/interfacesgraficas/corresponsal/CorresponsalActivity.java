package com.example.corresponsapp.interfacesgraficas.corresponsal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.corresponsapp.R;
import com.example.corresponsapp.interfaces.ConfirmacionCallback;
import com.example.corresponsapp.interfaces.IAbrirDialogo;
import com.example.corresponsapp.interfaces.IAbrirOpcCor;
import com.example.corresponsapp.interfacesgraficas.dialogos.DialogConfirmarFragment;
import com.example.corresponsapp.interfacesgraficas.dialogos.DialogOpcCorFragment;

import java.util.Hashtable;

public class CorresponsalActivity extends AppCompatActivity implements IAbrirDialogo, IAbrirOpcCor {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corresponsal);

        preferences = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        editor = preferences.edit();

    }

    @Override
    public void abrirDialogo(Hashtable<String, String> informacion, ConfirmacionCallback callback) {
        DialogConfirmarFragment dialog = new DialogConfirmarFragment(informacion, callback);
        dialog.show(getSupportFragmentManager(),"DialogoConfirmacion");
    }

    @Override
    public void abrirOpciones(View view) {
        DialogOpcCorFragment dialog = new DialogOpcCorFragment(view);
        dialog.show(getSupportFragmentManager(), "DialogoOpciones");
    }
}