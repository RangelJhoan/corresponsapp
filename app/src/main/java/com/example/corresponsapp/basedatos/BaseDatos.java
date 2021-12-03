package com.example.corresponsapp.basedatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.corresponsapp.entidades.Cliente;
import com.example.corresponsapp.entidades.CuentaBancaria;
import com.example.corresponsapp.entidades.Deposito;
import com.example.corresponsapp.entidades.Retiro;
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
        db.execSQL(UtilidadesBD.CREAR_DEPOSITO_TABLA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + UtilidadesBD.CLIENTE_TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + UtilidadesBD.TARJETA_TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + UtilidadesBD.CUENTA_BANCARIA_TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + UtilidadesBD.DEPOSITO_TABLA);
        onCreate(db);
    }

    //GESTIÓN DEL CORRESPONSAL
    //CREAR CUENTA DEL CLIENTE
    public long crearCuenta(CuentaBancaria cuentaBancaria) {
        SQLiteDatabase db = this.getWritableDatabase();
        long respuestaCliente = crearCliente(cuentaBancaria.getCliente());
        long respuestaTarjeta = crearTarjeta(cuentaBancaria.getTarjeta());
        if (respuestaCliente > 0) {
            if (respuestaTarjeta > 0) {
                ContentValues valuesCuenta = new ContentValues();
                valuesCuenta.put(UtilidadesBD.CUENTA_BANCARIA_NUMERO_CUENTA, cuentaBancaria.getNumero_cuenta());
                valuesCuenta.put(UtilidadesBD.CUENTA_BANCARIA_PIN, cuentaBancaria.getPIN());
                valuesCuenta.put(UtilidadesBD.CUENTA_BANCARIA_SALDO, cuentaBancaria.getSaldo());
                valuesCuenta.put(UtilidadesBD.CUENTA_BANCARIA_FK_CLIENTE, respuestaCliente);
                valuesCuenta.put(UtilidadesBD.CUENTA_BANCARIA_FK_TARJETA, respuestaTarjeta);

                long respuestaCuenta = db.insert(UtilidadesBD.CUENTA_BANCARIA_TABLA, UtilidadesBD.CUENTA_BANCARIA_ID, valuesCuenta);

                if (respuestaCuenta > 0) {
                    return respuestaCuenta;
                } else {
                    //RETORNAR -1: Error al crear la cuenta
                    return -1;
                }
            } else {
                //RETORNAR -2: Error al crear la tarjeta
                return -2;
            }
        } else {
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

    //DEPOSITAR DINERO
    public long crearDeposito(Deposito deposito) {
        int idCliente = consultarIdCliente(deposito.getCliente().getDocumento());
        if (idCliente > 0) {
            int idCuenta = consultarIdCuentaDocumento(deposito.getCliente().getDocumento());
            if (idCuenta > 0) {
                long resultadoDepositoCliente = depositarDinero(idCuenta, deposito.getMonto());
                if(resultadoDepositoCliente > 0){
                    SQLiteDatabase db = this.getWritableDatabase();
                    ContentValues valuesDeposito = new ContentValues();
                    valuesDeposito.put(UtilidadesBD.DEPOSITO_DOCUMENTO, deposito.getDocumento());
                    valuesDeposito.put(UtilidadesBD.DEPOSITO_FK_CLIENTE, idCliente);
                    valuesDeposito.put(UtilidadesBD.DEPOSITO_MONTO, deposito.getMonto());

                    long resultadoDeposito = db.insert(UtilidadesBD.DEPOSITO_TABLA, UtilidadesBD.DEPOSITO_ID, valuesDeposito);

                    if (resultadoDeposito > 0) {
                        return resultadoDeposito;
                    } else {
                        //Retornar -4: No se creó el depósito
                        return -4;
                    }
                }else{
                    //Retornar -3: No se pudo depositar
                    return -3;
                }
            } else {
                //Retornar -2: No se encontró la cuenta
                return -2;
            }
        } else {
            //Retornar -1: No se encontró el cliente
            return -1;
        }
    }

    //DEPOSITAR DINERO
    public long creaRetiro(Retiro retiro) {
        int idCliente = consultarIdCliente(retiro.getCliente().getDocumento());
        if (idCliente > 0) {
            int idCuenta = consultarIdCuentaDocumento(retiro.getCliente().getDocumento());
            if (idCuenta > 0) {
                long resultadoRetiroCliente = retirarDinero(idCuenta, retiro.getMonto());
                if(resultadoRetiroCliente > 0){
                    SQLiteDatabase db = this.getWritableDatabase();
                    ContentValues valuesRetiro = new ContentValues();
                    valuesRetiro.put(UtilidadesBD.RETIRO_FK_CLIENTE, idCliente);
                    valuesRetiro.put(UtilidadesBD.RETIRO_MONTO, retiro.getMonto());

                    long resultadoDeposito = db.insert(UtilidadesBD.DEPOSITO_TABLA, UtilidadesBD.DEPOSITO_ID, valuesRetiro);

                    if (resultadoDeposito > 0) {
                        return resultadoDeposito;
                    } else {
                        //Retornar -4: No se creó el retiro
                        return -4;
                    }
                }else{
                    //Retornar -3: No se pudo retirar
                    return -3;
                }
            } else {
                //Retornar -2: No se encontró la cuenta
                return -2;
            }
        } else {
            //Retornar -1: No se encontró el cliente
            return -1;
        }
    }

    private long depositarDinero(int idCuenta, double deposito) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesDeposito = new ContentValues();
        double nuevoSaldo = consultarSaldoCuenta(idCuenta) + deposito;
        valuesDeposito.put(UtilidadesBD.CUENTA_BANCARIA_SALDO, nuevoSaldo);

        long resultadoDeposito = db.update(UtilidadesBD.CUENTA_BANCARIA_TABLA, valuesDeposito, UtilidadesBD.CUENTA_BANCARIA_ID + "= ?", new String[] {String.valueOf(idCuenta)});

        if (resultadoDeposito > 0) {
            return resultadoDeposito;
        } else {
            //Retornar -1: No se depositó
            return -1;
        }
    }

    private long retirarDinero(int idCuenta, double retiro) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesRetiro = new ContentValues();
        double nuevoSaldo = consultarSaldoCuenta(idCuenta) - retiro;
        valuesRetiro.put(UtilidadesBD.CUENTA_BANCARIA_SALDO, nuevoSaldo);

        long resultadoRetiro = db.update(UtilidadesBD.CUENTA_BANCARIA_TABLA, valuesRetiro, UtilidadesBD.CUENTA_BANCARIA_ID + "= ?", new String[] {String.valueOf(idCuenta)});

        if (resultadoRetiro > 0) {
            return resultadoRetiro;
        } else {
            //Retornar -1: No se depositó
            return -1;
        }
    }

    public int consultarSaldoCuenta(int idCuenta) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT saldo FROM " + UtilidadesBD.CUENTA_BANCARIA_TABLA + " WHERE id = ?", new String[]{String.valueOf(idCuenta)});

        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        } else {
            //Retornar -1: No se encontró la cuenta
            return -1;
        }
    }

    public int consultarIdCliente(String documento) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT id FROM "+UtilidadesBD.CLIENTE_TABLA+" WHERE documento = ?", new String[]{documento});

        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        } else {
            //Retornar -1: No se encontró el cliente
            return -1;
        }
    }

    //CONSULTAR ID CUENTA POR DOCUMENTO DEL CLIENTE
    public int consultarIdCuentaDocumento(String documento) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT cu.id " +
                "FROM "+UtilidadesBD.CUENTA_BANCARIA_TABLA+" cu " +
                "JOIN "+UtilidadesBD.CLIENTE_TABLA+" c ON c.id = cu.id_cliente " +
                "WHERE c.documento = ?", new String[]{documento});

        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        } else {
            //Retornar -1: No se encontró la cuenta
            return -1;
        }
    }

}
