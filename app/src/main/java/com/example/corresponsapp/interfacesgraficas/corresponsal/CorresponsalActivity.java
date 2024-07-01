package com.example.corresponsapp.interfacesgraficas.corresponsal;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corresponsal);
    }

    /**
     * Abre el diálogo cuando sea llamado en algún fragmento
     * @param informacion Obtiene un objeto Hashtable con la información que se presentará en el fragment dialog
     * @param callback Callback que regresa la confirmación del cliente
     */
    @Override
    public void abrirDialogo(Hashtable<String, String> informacion, ConfirmacionCallback callback) {
        DialogConfirmarFragment dialog = new DialogConfirmarFragment(informacion, callback);
        dialog.show(getSupportFragmentManager(),"DialogoConfirmacion");
    }

    /**
     * Método que abre el menú secundario con las opciones del corresponsal
     * @param view Parámetro que recibe la vista del fragment para poder ejecutar el NavController
     */
    @Override
    public void abrirOpciones(View view) {
        DialogOpcCorFragment dialog = new DialogOpcCorFragment(view);
        dialog.show(getSupportFragmentManager(), "DialogoOpciones");
    }
}