package com.example.corresponsapp.utilidades;

import android.annotation.SuppressLint;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class Utilidades {

    @SuppressLint("SimpleDateFormat")
    public static String obtenerFechaExpiracion() {
        Date fechaActual = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaActual);
        calendar.add(Calendar.YEAR, 5);
        return new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());
    }

    public static boolean validarTextoMayuscula(String texto) {
        Pattern mayusculas = Pattern.compile(
                "^" +
                        "[A-Z\\s]+" + //\s permite espacios
                        "$"
        );
        return mayusculas.matcher(texto).matches();
    }

    public static boolean validarSoloNumeros(String texto) {
        Pattern mayusculas = Pattern.compile(
                "^" +
                        "[0-9]+" + //+ para permitir varios dígitos menos vacío. * para permitir vacío y varios dígitos. Ninguno para un sólo dígito
                        "$"
        );
        return mayusculas.matcher(texto).matches();
    }

    public static boolean validarCampos(EditText[] listaTextViews) {
        for (EditText textView : listaTextViews) {
            if (textView.getText().toString().trim().equals("")) {
                return false;
            }
        }
        return true;
    }

}
