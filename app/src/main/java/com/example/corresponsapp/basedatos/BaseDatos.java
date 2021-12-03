package com.example.corresponsapp.basedatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.corresponsapp.entidades.Cliente;
import com.example.corresponsapp.entidades.CuentaBancaria;
import com.example.corresponsapp.entidades.Tarjeta;
import com.example.corresponsapp.utilidades.UtilidadesBD;

public class BaseDatos extends SQLiteOpenHelper {
    private static BaseDatos instancia = null;
    private Context mCxt;

    //PARAMETROS CONEXIÓN
    private static final String DATABASE_NAME = "corresponsapp";
    private static final int DATABASE_VERSION = 1;

    //Crear conexión
    public static BaseDatos getInstance(Context ctx) {
        if (instancia == null) {
            instancia = new BaseDatos(ctx.getApplicationContext());
        }
        return instancia;
    }

    private BaseDatos(@Nullable Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        this.mCxt = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UtilidadesBD.CREAR_CLIENTE_TABLA);
        db.execSQL(UtilidadesBD.CREAR_TARJETA_TABLA);
        db.execSQL(UtilidadesBD.CREAR_CUENTA_BANCARIA_TABLA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + UtilidadesBD.CLIENTE_TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + UtilidadesBD.TARJETA_TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + UtilidadesBD.CUENTA_BANCARIA_TABLA);
        onCreate(db);
    }

    //GESTIÓN DEL CORRESPONSAL
    //CREAR CUENTA DEL CLIENTE
    public long crearCuenta(CuentaBancaria cuentaBancaria) {
        SQLiteDatabase db = this.getWritableDatabase();
        long respuestaCliente = crearCliente(cuentaBancaria.getCliente());
        long respuestaTarjeta = crearTarjeta(cuentaBancaria.getTarjeta());
        if(respuestaCliente > 0){
            if(respuestaTarjeta > 0) {
                ContentValues valuesCuenta = new ContentValues();
                valuesCuenta.put(UtilidadesBD.CUENTA_BANCARIA_NUMERO_CUENTA, cuentaBancaria.getNumero_cuenta());
                valuesCuenta.put(UtilidadesBD.CUENTA_BANCARIA_PIN, cuentaBancaria.getPIN());
                valuesCuenta.put(UtilidadesBD.CUENTA_BANCARIA_SALDO, cuentaBancaria.getSaldo());
                valuesCuenta.put(UtilidadesBD.CUENTA_BANCARIA_FK_CLIENTE, respuestaCliente);
                valuesCuenta.put(UtilidadesBD.CUENTA_BANCARIA_FK_TARJETA, respuestaTarjeta);

                long respuestaCuenta = db.insert(UtilidadesBD.CUENTA_BANCARIA_TABLA, UtilidadesBD.CUENTA_BANCARIA_ID, valuesCuenta);

                if(respuestaCuenta > 0){
                    return respuestaCuenta;
                }else{
                    //RETORNAR -1: Error al crear la cuenta
                    return -1;
                }
            }else{
                //RETORNAR -2: Error al crear la tarjeta
                return -2;
            }
        }else{
            //RETORNAR -3: Error al crear al cliente
            return -3;
        }
    }

    //CREAR CLIENTE
    public long crearCliente(Cliente cliente) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesCliente = new ContentValues();
        valuesCliente.put(UtilidadesBD.CLIENTE_DOCUMENTO, cliente.getDocumento());
        valuesCliente.put(UtilidadesBD.CLIENTE_NOMBRE_COMPLETO, cliente.getNombre_completo());

        long resultadoCliente = db.insert(UtilidadesBD.CLIENTE_TABLA, UtilidadesBD.CLIENTE_ID, valuesCliente);
        return resultadoCliente;
    }

    //CREAR CLIENTE
    public long crearTarjeta(Tarjeta tarjeta) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesTarjeta = new ContentValues();
        valuesTarjeta.put(UtilidadesBD.TARJETA_FECHA_EXPIRACION, tarjeta.getFecha_expiracion());
        valuesTarjeta.put(UtilidadesBD.TARJETA_CVV, tarjeta.getCvv());

        long resultadoTarjeta = db.insert(UtilidadesBD.TARJETA_TABLA, UtilidadesBD.TARJETA_ID, valuesTarjeta);
        return resultadoTarjeta;
    }

}
