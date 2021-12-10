package com.example.corresponsapp.basedatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.corresponsapp.entidades.Cliente;
import com.example.corresponsapp.entidades.Corresponsal;
import com.example.corresponsapp.entidades.CuentaBancaria;
import com.example.corresponsapp.entidades.Deposito;
import com.example.corresponsapp.entidades.PagoTarjeta;
import com.example.corresponsapp.entidades.Retiro;
import com.example.corresponsapp.entidades.Tarjeta;
import com.example.corresponsapp.entidades.Transferencia;
import com.example.corresponsapp.utilidades.Constantes;
import com.example.corresponsapp.utilidades.Sesion;
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
        db.execSQL(UtilidadesBD.CREAR_CORRESPONSAL_TABLA);
        db.execSQL(UtilidadesBD.CREAR_TABLA_RETIRO);
        db.execSQL(UtilidadesBD.CREAR_TABLA_PAGO_TARJETA);
        db.execSQL(UtilidadesBD.CREAR_TABLA_TRANSFERENCIA);
        db.execSQL("INSERT INTO " + UtilidadesBD.CORRESPONSAL_TABLA + " (nombre_completo, saldo, correo, clave) VALUES ('Jhoan Rangel',10000, 'jhoan@wposs.com', '123456')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(Constantes.DROP_TABLE_EXISTENT + UtilidadesBD.CLIENTE_TABLA);
        db.execSQL(Constantes.DROP_TABLE_EXISTENT + UtilidadesBD.TARJETA_TABLA);
        db.execSQL(Constantes.DROP_TABLE_EXISTENT + UtilidadesBD.CUENTA_BANCARIA_TABLA);
        db.execSQL(Constantes.DROP_TABLE_EXISTENT + UtilidadesBD.DEPOSITO_TABLA);
        db.execSQL(Constantes.DROP_TABLE_EXISTENT + UtilidadesBD.CORRESPONSAL_TABLA);
        db.execSQL(Constantes.DROP_TABLE_EXISTENT + UtilidadesBD.RETIRO_TABLA);
        db.execSQL(Constantes.DROP_TABLE_EXISTENT + UtilidadesBD.PAGO_TARJETA_TABLA);
        db.execSQL(Constantes.DROP_TABLE_EXISTENT + UtilidadesBD.TRANSFERENCIA_MONTO);
        onCreate(db);
    }

    //GESTIÓN DEL CORRESPONSAL
    //CREAR CUENTA DEL CLIENTE
    public long crearCuenta(CuentaBancaria cuentaBancaria) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesCuenta = new ContentValues();
        valuesCuenta.put(UtilidadesBD.CUENTA_BANCARIA_NUMERO_CUENTA, cuentaBancaria.getNumero_cuenta());
        valuesCuenta.put(UtilidadesBD.CUENTA_BANCARIA_PIN, cuentaBancaria.getPIN());
        valuesCuenta.put(UtilidadesBD.CUENTA_BANCARIA_SALDO, cuentaBancaria.getSaldo());
        valuesCuenta.put(UtilidadesBD.CUENTA_BANCARIA_FK_CLIENTE, cuentaBancaria.getCliente().getId());
        valuesCuenta.put(UtilidadesBD.CUENTA_BANCARIA_FK_TARJETA, cuentaBancaria.getTarjeta().getId());

        //Le asignamos una cuenta bancaria
        long respuestaCuenta = db.insert(UtilidadesBD.CUENTA_BANCARIA_TABLA, UtilidadesBD.CUENTA_BANCARIA_ID, valuesCuenta);

        if (respuestaCuenta > 0) {
            return respuestaCuenta;
        } else {
            return -1;
        }
    }

    //CREAR CLIENTE
    public long crearCliente(Cliente cliente) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesCliente = new ContentValues();
        valuesCliente.put(UtilidadesBD.CLIENTE_DOCUMENTO, cliente.getDocumento());
        valuesCliente.put(UtilidadesBD.CLIENTE_NOMBRE_COMPLETO, cliente.getNombre_completo());

        //Crea el cliente
        long resultadoCliente = db.insert(UtilidadesBD.CLIENTE_TABLA, UtilidadesBD.CLIENTE_ID, valuesCliente);
        return resultadoCliente;
    }

    //CREAR CLIENTE
    public long crearTarjeta(Tarjeta tarjeta) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesTarjeta = new ContentValues();
        valuesTarjeta.put(UtilidadesBD.TARJETA_FECHA_EXPIRACION, tarjeta.getFecha_expiracion());
        valuesTarjeta.put(UtilidadesBD.TARJETA_CVV, tarjeta.getCvv());

        //Crea la tarjeta
        long resultadoTarjeta = db.insert(UtilidadesBD.TARJETA_TABLA, UtilidadesBD.TARJETA_ID, valuesTarjeta);
        return resultadoTarjeta;
    }

    //DEPOSITAR DINERO
    public long crearDeposito(Deposito deposito) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesDeposito = new ContentValues();
        valuesDeposito.put(UtilidadesBD.DEPOSITO_DOCUMENTO, deposito.getDocumento());
        valuesDeposito.put(UtilidadesBD.DEPOSITO_FK_CLIENTE, deposito.getCuentaBancaria().getCliente().getId());
        valuesDeposito.put(UtilidadesBD.DEPOSITO_MONTO, deposito.getMonto());

        //Verificamos que se haya registrado el depósito en el sistema
        long resultadoDeposito = db.insert(UtilidadesBD.DEPOSITO_TABLA, UtilidadesBD.DEPOSITO_ID, valuesDeposito);
        if (resultadoDeposito > 0) {
            return resultadoDeposito;
        } else {
            //No se creó el depósito
            return -1;
        }
    }

    public long depositarDinero(int idCuenta, double deposito, double comision) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesDeposito = new ContentValues();
        double nuevoSaldo = consultarSaldoCuenta(idCuenta) + (deposito - comision);
        valuesDeposito.put(UtilidadesBD.CUENTA_BANCARIA_SALDO, nuevoSaldo);
        //Se le suma al corresponsal el valor del depósito
        long respuestaComision = registrarComision(Sesion.corresponsalSesion.getId(), comision);
        if (respuestaComision > 0) {
            long resultadoDeposito = db.update(UtilidadesBD.CUENTA_BANCARIA_TABLA, valuesDeposito, UtilidadesBD.CUENTA_BANCARIA_ID + "= ?", new String[]{String.valueOf(idCuenta)});

            if (resultadoDeposito > 0) {
                return resultadoDeposito;
            } else {
                //Retornar -1: No se depositó
                return -1;
            }
        } else {
            //Retornar -2: No se registró la comisión
            return -2;
        }
    }

    //RETIRAR DINERO
    public long crearRetiro(Retiro retiro) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesRetiro = new ContentValues();
        valuesRetiro.put(UtilidadesBD.RETIRO_FK_CLIENTE, retiro.getCuentaBancaria().getCliente().getId());
        valuesRetiro.put(UtilidadesBD.RETIRO_MONTO, retiro.getMonto());

        long resultadoDeposito = db.insert(UtilidadesBD.RETIRO_TABLA, UtilidadesBD.RETIRO_ID, valuesRetiro);

        if (resultadoDeposito > 0) {
            return resultadoDeposito;
        } else {
            //No se creó el retiro
            return -1;
        }
    }

    public long retirarDinero(int idCuenta, double retiro, double comision) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Validar que al saldo del cliente se le pueda retirar el dinero y la comisión
        double saldoDisponible = consultarSaldoCuenta(idCuenta) - (retiro + comision);

        if (saldoDisponible >= 0) {

            ContentValues valuesRetiro = new ContentValues();
            valuesRetiro.put(UtilidadesBD.CUENTA_BANCARIA_SALDO, saldoDisponible);

            //Retirar el monto y la comisión de la cuenta bancaria del cliente
            long resultadoRetiro = db.update(UtilidadesBD.CUENTA_BANCARIA_TABLA, valuesRetiro, UtilidadesBD.CUENTA_BANCARIA_ID + "= ?", new String[]{String.valueOf(idCuenta)});

            if (resultadoRetiro > 0) {
                return resultadoRetiro;
            } else {
                //Retornar -1: No se depositó
                return -1;
            }
        } else {
            //Retornar -3; Saldo insuficiente para el retiro
            return -3;
        }
    }

    public long crearPagoTarjeta(PagoTarjeta pagoTarjeta) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesPagoTarjeta = new ContentValues();

        valuesPagoTarjeta.put(UtilidadesBD.PAGO_TARJETA_FK_CUENTA, pagoTarjeta.getCuentaBancaria().getId());
        valuesPagoTarjeta.put(UtilidadesBD.PAGO_TARJETA_VALOR, pagoTarjeta.getValor());
        valuesPagoTarjeta.put(UtilidadesBD.PAGO_TARJETA_COUTAS, pagoTarjeta.getNumeroCuotas());

        long resultadoPagoTarjeta = db.insert(UtilidadesBD.PAGO_TARJETA_TABLA, UtilidadesBD.PAGO_TARJETA_ID, valuesPagoTarjeta);

        if (resultadoPagoTarjeta > 0) {
            return resultadoPagoTarjeta;
        } else {
            //No se creó el pago con tarjeta
            return -1;
        }
    }

    public String consultarPINCuenta(String documento) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT cu.pin FROM " + UtilidadesBD.CUENTA_BANCARIA_TABLA + " cu " +
                "JOIN cliente c ON c.id = cu.id_cliente " +
                "WHERE c.documento = ?", new String[]{documento});

        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        } else {
            //No se encontró el PIN
            return null;
        }
    }

    public String consultarCVVCuenta(String numeroCuenta) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT t.cvv " +
                "FROM " + UtilidadesBD.CUENTA_BANCARIA_TABLA + " cu " +
                "JOIN tarjeta t ON t.id = cu.id_tarjeta " +
                "WHERE cu.numero_cuenta = ?", new String[]{numeroCuenta});

        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        } else {
            //No se encontró el CVV
            return null;
        }
    }

    public double consultarSaldoCuenta(int idCuenta) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT saldo FROM " + UtilidadesBD.CUENTA_BANCARIA_TABLA + " WHERE id = ?", new String[]{String.valueOf(idCuenta)});

        if (cursor.moveToFirst()) {
            return cursor.getDouble(0);
        } else {
            //Retornar -1: No se encontró la cuenta
            return -1;
        }
    }

    public int consultarIdCliente(String documento) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT id FROM " + UtilidadesBD.CLIENTE_TABLA + " WHERE documento = ?", new String[]{documento});

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
                "FROM " + UtilidadesBD.CUENTA_BANCARIA_TABLA + " cu " +
                "JOIN " + UtilidadesBD.CLIENTE_TABLA + " c ON c.id = cu.id_cliente " +
                "WHERE c.documento = ?", new String[]{documento});

        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        } else {
            //Retornar -1: No se encontró la cuenta
            return -1;
        }
    }

    //CONSULTAR ID CUENTA POR NUMERO DE CUENTA
    public int consultarIdCuentaNumero(String numeroCuenta) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT id " +
                "FROM " + UtilidadesBD.CUENTA_BANCARIA_TABLA + " " +
                "WHERE numero_cuenta = ?", new String[]{numeroCuenta});

        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        } else {
            //Retornar -1: No se encontró la cuenta
            return -1;
        }
    }

    //Corresponsal sesión
    public Corresponsal iniciarSesion(String correo, String clave) {
        SQLiteDatabase db = this.getReadableDatabase();
        Corresponsal corresponsal = new Corresponsal();

        Cursor cursor = db.rawQuery("SELECT id, nombre_completo, saldo " +
                "FROM " + UtilidadesBD.CORRESPONSAL_TABLA + " " +
                "WHERE correo = ? and clave = ?", new String[]{correo, clave});

        if (cursor.moveToFirst()) {
            corresponsal.setId(cursor.getInt(0));
            corresponsal.setNombre_completo(cursor.getString(1));
            corresponsal.setSaldo(cursor.getDouble(2));
            corresponsal.setCorreo(correo);
            corresponsal.setClave(clave);
            return corresponsal;
        }
        return null;
    }

    //Agregarle al saldo del corresponsal el valor de la comisión de la acción a realizar
    public long registrarComision(int idCorresponsal, double comision) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesRetiro = new ContentValues();
        //Sumarle al saldo del corresponsal el valor de la comisión
        double nuevoSaldo = consultarSaldoCorresponsal(idCorresponsal) + comision;
        valuesRetiro.put(UtilidadesBD.CORRESPONSAL_SALDO, nuevoSaldo);

        //Asignarle el nuevo saldo a la cuenta del corresponsal
        long resultadoComision = db.update(UtilidadesBD.CORRESPONSAL_TABLA, valuesRetiro, UtilidadesBD.CORRESPONSAL_ID + "= ?",
                new String[]{String.valueOf(idCorresponsal)});

        if (resultadoComision > 0) {
            //Se le agrega a la sesión del corresponsal el nuevo saldo
            Sesion.corresponsalSesion.setSaldo(nuevoSaldo);
            return resultadoComision;
        } else {
            //Retornar -1: No se registró la comisión
            return -1;
        }
    }

    public double consultarSaldoCorresponsal(int idCorresponsal) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT saldo " +
                "FROM " + UtilidadesBD.CORRESPONSAL_TABLA + " " +
                "WHERE id = ?", new String[]{String.valueOf(idCorresponsal)});

        if (cursor.moveToFirst()) {
            return cursor.getDouble(0);
        }
        return -1;
    }

    public long consultarSaldoCliente(CuentaBancaria cuentaBancaria) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT cu.saldo " +
                "FROM " + UtilidadesBD.CLIENTE_TABLA + " cl " +
                "JOIN " + UtilidadesBD.CUENTA_BANCARIA_TABLA + " cu ON cu.id_cliente = cl.id " +
                "WHERE cl.documento = ?", new String[]{String.valueOf(cuentaBancaria.getCliente().getDocumento())});

        //Validar que se haya encontrado el saldo
        if (cursor.moveToFirst()) {
            return cursor.getLong(0);
        } else {
            //Consulta no realizada
            return -1;
        }
    }

    public long crearTransferencia(Transferencia transferencia) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesTransferencia = new ContentValues();
        valuesTransferencia.put(UtilidadesBD.TRANSFERENCIA_FK_CUENTA_TRANSFIERE, transferencia.getCuentaTransfiere().getId());
        valuesTransferencia.put(UtilidadesBD.TRANSFERENCIA_FK_CUENTA_RECIBE, transferencia.getCuentaRecibe().getId());
        valuesTransferencia.put(UtilidadesBD.TRANSFERENCIA_MONTO, transferencia.getMonto());

        long resultadoTransferencia = db.insert(UtilidadesBD.TRANSFERENCIA_TABLA, UtilidadesBD.TRANSFERENCIA_ID, valuesTransferencia);

        if (resultadoTransferencia > 0) {
            return resultadoTransferencia;
        } else {
            //No se creó el retiro
            return -1;
        }
    }
}

