package com.example.corresponsapp.interfacesgraficas.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.corresponsapp.databinding.ActivityLoginBinding;
import com.example.corresponsapp.entidades.Corresponsal;
import com.example.corresponsapp.interfacesgraficas.corresponsal.CorresponsalActivity;
import com.example.corresponsapp.utilidades.Sesion;
import com.example.corresponsapp.utilidades.Utilidades;

public class LoginActivity extends AppCompatActivity implements LoginMVP.View {
    private ActivityLoginBinding binding;
    private LoginMVP.Presenter presenter;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    /**
     * @author Jhoan Manuel Rangel Mariño
     * @since 01/12/2021
     * @version 1.0
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        presenter = new LoginPresenterImpl(this);

        //Se inicializa el shared preference para almacenar la sesion
        iniciarlizarPreference();

        //Se valida que haya una sesión activa para redirigir automáticamente al menú principal
        if(validarSesion()){
            Toast.makeText(getApplicationContext(), "¡Bienvenido!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(LoginActivity.this, CorresponsalActivity.class));
            finish();
        }

        binding.btnIniciarSesion.setOnClickListener(v -> iniciarSesion());

    }

    //Se valida que la sesión esté activa
    private boolean validarSesion() {
        return preferences.getBoolean(Sesion.LLAVE_SESION_INICIADA, false);
    }

    //Se inicializa el shared preference de la sesión con el nombre "sesion"
    private void iniciarlizarPreference() {
        preferences = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    //Método para iniciar sesión
    private void iniciarSesion() {
        EditText[] editTexts = {binding.etCorreo, binding.etClave};
        //Se valida que los campos de texto contengan información
        if (Utilidades.validarCampos(editTexts)) {
            //Se validan los datos ingresados con los datos almacenados en la base de datos
            presenter.iniciarSesion(getApplicationContext(), binding.etCorreo.getText().toString(), binding.etClave.getText().toString());
        } else {
            Toast.makeText(getApplicationContext(), "Por favor llene todos los campos", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void mostrarResultado(Corresponsal corresponsal, String resultado) {
        editor.putBoolean(Sesion.LLAVE_SESION_INICIADA, true);
        editor.putInt(Sesion.LLAVE_ID,corresponsal.getId());
        editor.putString(Sesion.LLAVE_NOMBRE, corresponsal.getNombreCompleto());
        editor.putFloat(Sesion.LLAVE_SALDO, (float) corresponsal.getSaldo());
        editor.apply();
        Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();
        startActivity(new Intent(LoginActivity.this, CorresponsalActivity.class));
        finish();
    }

    @Override
    public void mostrarError(String error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
    }
}