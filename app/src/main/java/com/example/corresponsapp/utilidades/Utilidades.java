package com.example.corresponsapp.utilidades;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utilidades {

    @SuppressLint("SimpleDateFormat")
    public static String obtenerFechaDMYHMS(Date fecha){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(calendar.getTime());
    }

    @SuppressLint("SimpleDateFormat")
    public static String obtenerFechaExpiracion(){
        Date fechaActual = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaActual);
        calendar.add(Calendar.YEAR, 5);
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(calendar.getTime());
    }

}
