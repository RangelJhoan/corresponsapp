package com.example.corresponsapp.validaciones;

import android.widget.EditText;

public class Validaciones {

    public static boolean validarCampos(EditText[] listaTextViews) {
        for (int i = 0; i < listaTextViews.length; i++) {
            if (listaTextViews[i].getText().toString().equals("")) {
                return false;
            }
        }
        return true;
    }

}
