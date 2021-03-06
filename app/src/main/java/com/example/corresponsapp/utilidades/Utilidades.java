package com.example.corresponsapp.utilidades;

import android.annotation.SuppressLint;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class Utilidades {

    @SuppressLint("SimpleDateFormat")
    public static String obtenerFechaExpiracion(){
        Date fechaActual = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaActual);
        calendar.add(Calendar.YEAR, 5);
        return new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());
    }

    public static boolean validarTextoMayuscula(String texto){
        Pattern mayusculas = Pattern.compile(
                "^" +
                        "[A-Z\\s]+" + //\s permite espacios
                        "$"
        );
        if(mayusculas.matcher(texto).matches()){
            return true;
        }
        return false;
    }

    public static boolean validarSoloNumeros(String texto){
        Pattern mayusculas = Pattern.compile(
                "^" +
                        "[0-9]+" + //+ para permitir varios dígitos menos vacío. * para permitir vacío y varios dígitos. Ninguno para un sólo dígito
                        "$"
        );
        if(mayusculas.matcher(texto).matches()){
            return true;
        }
        return false;
    }

    public static boolean validarCampos(EditText[] listaTextViews) {
        for (int i = 0; i < listaTextViews.length; i++) {
            if (listaTextViews[i].getText().toString().trim().equals("")) {
                return false;
            }
        }
        return true;
    }

}
