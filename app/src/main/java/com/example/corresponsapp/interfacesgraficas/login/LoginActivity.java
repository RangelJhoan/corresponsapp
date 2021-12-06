package com.example.corresponsapp.interfacesgraficas.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.corresponsapp.databinding.ActivityLoginBinding;
import com.example.corresponsapp.interfacesgraficas.corresponsal.CorresponsalActivity;
import com.example.corresponsapp.validaciones.Validaciones;

public class LoginActivity extends AppCompatActivity implements LoginMVP.View {
    private ActivityLoginBinding binding;
    private LoginMVP.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        presenter = new LoginPresenterImpl(this);

        binding.btnIniciarSesion.setOnClickListener(v -> {
            iniciarSesion();
        });

    }

    private void iniciarSesion() {
        EditText[] editTexts = {binding.etCorreo, binding.etClave};
        if (Validaciones.validarCampos(editTexts)) {
            presenter.iniciarSesion(getApplicationContext(), binding.etCorreo.getText().toString(), binding.etClave.getText().toString());
        } else {
            Toast.makeText(getApplicationContext(), "Por favor llene todos los campos", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void mostrarResultado(String resultado) {
        Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();
        startActivity(new Intent(LoginActivity.this, CorresponsalActivity.class));
    }

    @Override
    public void mostrarError(String error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
    }
}