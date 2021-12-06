package com.example.corresponsapp.interfacesgraficas.corresponsal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.corresponsapp.R;
import com.example.corresponsapp.interfaces.ConfirmacionCallback;
import com.example.corresponsapp.interfaces.IAbrirDialogo;
import com.example.corresponsapp.interfacesgraficas.dialogos.DialogConfirmarFragment;

import java.util.Hashtable;

public class CorresponsalActivity extends AppCompatActivity implements IAbrirDialogo {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corresponsal);
    }

    @Override
    public void abrirDialogo(Hashtable<String, String> informacion, ConfirmacionCallback callback) {
        DialogConfirmarFragment dialog = new DialogConfirmarFragment(informacion, callback);
        dialog.show(getSupportFragmentManager(),"DialogoDepositar");
    }
}