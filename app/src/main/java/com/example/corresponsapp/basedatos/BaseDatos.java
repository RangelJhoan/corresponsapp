package com.example.corresponsapp.basedatos;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.corresponsapp.entidades.Cliente;
import com.example.corresponsapp.entidades.ConsultaSaldo;
import com.example.corresponsapp.entidades.Corresponsal;
import com.example.corresponsapp.entidades.CuentaBancaria;
import com.example.corresponsapp.entidades.CuentaCreada;
import com.example.corresponsapp.entidades.Deposito;
import com.example.corresponsapp.entidades.PagoTarjeta;
import com.example.corresponsapp.entidades.Retiro;
import com.example.corresponsapp.entidades.Tarjeta;
import com.example.corresponsapp.entidades.Transferencia;
import com.example.corresponsapp.utilidades.Constantes;
import com.example.corresponsapp.utilidades.Sesion;
import com.example.corresponsapp.utilidades.UtilidadesBD;

import java.util.ArrayList;

public class BaseDatos extends SQLiteOpenHelper {
    private static BaseDatos instancia = null;
    private Context mCxt;

    //PARAMETROS CONEXIÓN
    private static final String DATABASE_NAME = "corresponsapp";
    private static final int DATABASE_VERSION = 1;

    //Crear conexión

    /**
     * Método diseñado con patrón Singleton el cual inicializa la conexión con la base de datos
     * @param ctx parámetro que recibe el contexto donde es llamado
     * @return regresa la instancia del objeto de esta clase
     */
    public static BaseDatos getInstance(Context ctx) {
        if (instancia == null) {
            instancia = new BaseDatos(ctx.getApplicationContext());
        }
        return instancia;
    }

    /**
     * Método para instanciar el objeto de la base de datos e inicilizar la conexión
     * @param ctx parámetro que recibe el conexto donde es llamado
     */
    private BaseDatos(@Nullable Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        this.mCxt = ctx;
    }

    /**
     * Método utilizado para la creación de las tablas de la base de datos
     * @param db parámetro que recibe la instancia de la base de datos
     */
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
        db.execSQL(UtilidadesBD.CREAR_TABLA_CONSULTA_SALDO);
        db.execSQL(UtilidadesBD.CREAR_TABLA_CUENTA_CREADA);
        db.execSQL("INSERT INTO " + UtilidadesBD.CORRESPONSAL_TABLA + " (nombre_completo, saldo, correo, clave) VALUES ('Jhoan Rangel',10000, 'jhoan@wposs.com', '123456')");
    }

    /**
     * Método que se ejecuta cuando la versión de la base de datos sufre algún cambio
     * @param db obtiene la isntancia del objeto de la base de datos
     * @param i parámetro que obtiene la versión antigua de la base de datos
     * @param i1 parámetro que obtiene la versión nueva de la base de datos
     */
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
        db.execSQL(Constantes.DROP_TABLE_EXISTENT + UtilidadesBD.CONSULTA_SALDO_TABLA);
        db.execSQL(Constantes.DROP_TABLE_EXISTENT + UtilidadesBD.CUENTA_CREADA_TABLA);
        onCreate(db);
    }

    //CREACIONES

    /**
     * Método que inserta una cuenta bancaria de un cliente a la base de datos
     * @param cuentaBancaria parámetro que recibe un objeto Cuenta Bancaria que almacena la información de la cuenta bancaria, el cliente y la tarjeta
     * @return El método retorna el id de la cuenta bancaria si es exitoso el registro, sino retorna -1 indicando que no se realizó el registro
     */
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

    /**
     * Método que inserta la información del cliente en la base de datos
     * @param cliente parámetro que recibe un objeto Cliente con la información personal del cliente
     * @return retorna el id del cliente. Si no se crea el registro, se retorna -1 como error
     */
    public long crearCliente(Cliente cliente) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesCliente = new ContentValues();
        valuesCliente.put(UtilidadesBD.CLIENTE_DOCUMENTO, cliente.getDocumento());
        valuesCliente.put(UtilidadesBD.CLIENTE_NOMBRE_COMPLETO, cliente.getNombre_completo());

        //Crea el cliente
        long resultadoCliente = db.insert(UtilidadesBD.CLIENTE_TABLA, UtilidadesBD.CLIENTE_ID, valuesCliente);
        return resultadoCliente;
    }

    public long crearCuentaCreada(CuentaCreada cuentaCreada){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesCuentaCreada = new ContentValues();
        valuesCuentaCreada.put(UtilidadesBD.CUENTA_CREADA_FK_CLIENTE, cuentaCreada.getCliente().getId());
        valuesCuentaCreada.put(UtilidadesBD.CUENTA_CREADA_MONTO, cuentaCreada.getMontoInicial());

        //Crea el registro de la creación del cliente
        long resultadoCliente = db.insert(UtilidadesBD.CUENTA_CREADA_TABLA, UtilidadesBD.CUENTA_CREADA_ID, valuesCuentaCreada);
        return resultadoCliente;
    }

    public long crearConsultaSaldo(ConsultaSaldo consultaSaldo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesConsultarSaldo = new ContentValues();
        valuesConsultarSaldo.put(UtilidadesBD.CONSULTA_SALDO_FK_CLIENTE, consultaSaldo.getCliente().getId());
        valuesConsultarSaldo.put(UtilidadesBD.CONSULTA_SALDO_SALDO, consultaSaldo.getSaldo());

        //Crea el registro de la consulta del saldo
        long resultadoConsultaSaldo = db.insert(UtilidadesBD.CONSULTA_SALDO_TABLA, UtilidadesBD.CONSULTA_SALDO_ID, valuesConsultarSaldo);
        return resultadoConsultaSaldo;
    }

    /**
     * Método que inserta la información de la tarjeta que se le asigna al cliente en la base de datos
     * @param tarjeta parámetro que recibe un objeto Tarjeta con la información de la tarjeta que se le asignó al cliente
     * @return retorna el id de la tarjeta. Si no se crea el registro, se retorna -1 como error
     */
    public long crearTarjeta(Tarjeta tarjeta) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesTarjeta = new ContentValues();
        valuesTarjeta.put(UtilidadesBD.TARJETA_FECHA_EXPIRACION, tarjeta.getFecha_expiracion());
        valuesTarjeta.put(UtilidadesBD.TARJETA_CVV, tarjeta.getCvv());

        //Crea la tarjeta
        long resultadoTarjeta = db.insert(UtilidadesBD.TARJETA_TABLA, UtilidadesBD.TARJETA_ID, valuesTarjeta);
        return resultadoTarjeta;
    }

    /**
     * Método que inserta la información del depósito que realizó el usuario en la base de datos
     * @param deposito parámetro que recibe un objeto Deposito con la información del depósito que realizó el cliente
     * @return retorna el id del registro del depósito. Si no se crea el registro, se retorna -1 como error
     */
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

    /**
     * Método usado para sumar dinero a la cuenta de un cliente y registrar la comisión en la cuenta del corresponsal
     * @param idCuenta parámetro que recibe el id de la cuenta a depositar el dinero
     * @param deposito parámetro que recibe el valor a depositar en la cuenta del cliente
     * @param comision parámetro que recibe el valor de la comisión según la acción a realizar
     * @return retorna el id del registro del depósito. Si no se crea el registro, se retorna -1 indicando que hubo un error en el depósito y -2 si hubo un error al registrar la comisión
     */
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

    /**
     * Método que inserta en la base de datos la información del retiro realizada por el cliente
     * @param retiro Parámetro que recibe un objeto Retiro con la información del retiro que realizó el cliente
     * @return Retorna el id del registro del retiro. Si no se registra el retiro, se retornará -1 como error
     */
    public long crearRetiro(Retiro retiro) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesRetiro = new ContentValues();
        valuesRetiro.put(UtilidadesBD.RETIRO_FK_CLIENTE, retiro.getCuentaBancaria().getCliente().getId());
        valuesRetiro.put(UtilidadesBD.RETIRO_MONTO, retiro.getMonto());

        long resultadoRetiro = db.insert(UtilidadesBD.RETIRO_TABLA, UtilidadesBD.RETIRO_ID, valuesRetiro);

        if (resultadoRetiro > 0) {
            return resultadoRetiro;
        } else {
            //No se creó el retiro
            return -1;
        }
    }

    /**
     * Método usado para restar dinero de una cuenta bancaria y sumar el valor de la comisión de esta acción a la cuenta del corresponsal
     * @param idCuenta Parámetro que recibe el id de la cuenta bancaria del cliente a la que se le retira el dinero
     * @param retiro Parámetro que recibe el valor a retirar de la cuenta bancaria del cliente
     * @param comision Parámetro que recibe el valor de la comisión de la acción realizada por el cliente
     * @return Retorna el id del registro del retiro. Si no se registra el retiro, se retorna -1 indicando que no se retiró el dinero y -2 si el cliente no tiene suficiente saldo para el retiro
     */
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
                //Retornar -1: No se retiró el dinero
                return -1;
            }
        } else {
            //Retornar -2; Saldo insuficiente para el retiro
            return -2;
        }
    }

    /**
     * Método que inserta el registro de un pago con tarjeta realizado por un cliente a la base de datos
     * @param pagoTarjeta Parámetro que recibe un objeto la información del pago con tarjeta realizada por el cliente
     * @return Retorna el id del registro del pago con tarjeta. Si no se registra el pago, retorna -1 indicando que hubo un error
     */
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

    /**
     * Método que inserta en la base de datos la suma del saldo actual del corresponsal y el valor de la comisión de la acción realizada por el cliente. El saldo del corresponsal es cambiado al nuevo saldo
     * @param idCorresponsal Parámetro que recibe el id del corresponsal que tiene la sesión activa en el dispositivo
     * @param comision Parámetro que recibe el valor de la comisión de la acción realizada por el cliente
     * @return Retorna el ID del registro de la comisión. Si no se hace el registro, se retorna -1 indicando que hubo un error
     */
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
            SharedPreferences preferences = mCxt.getSharedPreferences("sesion", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putFloat(Sesion.LLAVE_SALDO, (float) nuevoSaldo);
            editor.apply();
            return resultadoComision;
        } else {
            //Retornar -1: No se registró la comisión
            return -1;
        }
    }

    /**
     * Método que inserta en la base de datos el registro con la información de la transferencia que realiza el cliente
     * @param transferencia Parámetro que recibe un objeto Transferencia con la información de la transferencia
     * @return Retorna el ID del registro de la transferencia. Si no se hace el registro, retorna -1 indicando que hubo un error
     */
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

    /**
     * Método para cambiar la clave del corresponsal
     * @param idCorresponsal Parámetro que recibe el id del corresponsal
     * @param clave Párametro que recibe la nueva clave
     * @return Retorna el resultado del cambio de clave. Si no se realiza el cambio, se retorna -1 indicando que hubo un error
     */
    public long cambiarClave(int idCorresponsal, String clave) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesClave = new ContentValues();
        valuesClave.put(UtilidadesBD.CORRESPONSAL_CLAVE, clave);

        //Se cambia la clave del corresponsal
        long resultadoCambioClave = db.update(UtilidadesBD.CORRESPONSAL_TABLA, valuesClave, UtilidadesBD.CORRESPONSAL_ID + "= ?",
                new String[]{String.valueOf(idCorresponsal)});

        if (resultadoCambioClave > 0) {
            return resultadoCambioClave;
        } else {
            //No se cambió la clave
            return -1;
        }
    }

    //CONSULTAS

    /**
     * Método que consulta el saldo actual del corresponsal
     * @param idCorresponsal Parámetro que recibe el id del corresponsal a consultar
     * @return Retorna un dato de tipo Double con el saldo actual del corresponsal. Si no se encuentra el corresponsal, se retorna -1
     */
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

    /**
     * Consulta el nombre del cliente
     * @param numeroCuenta Parámetro que recibe el número de la cuenta del cliente a consultar el nombre
     * @return Retorna una cadena de caracteres con el nombre del cliente. Si no se encuentra el registro, se retorna una cadena nula
     */
    public String consultarNombreCliente(String numeroCuenta){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT c.nombre_completo " +
                "FROM " + UtilidadesBD.CLIENTE_TABLA + " c " +
                "JOIN " + UtilidadesBD.CUENTA_BANCARIA_TABLA + " cu ON cu.id_cliente = c.id " +
                "WHERE cu.numero_cuenta = ?", new String[]{numeroCuenta});

        //Validar que se haya encontrado el nombre
        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        } else {
            //No se encontraron registros
            return null;
        }
    }

    /**
     * Consulta el saldo actual del cliente
     * @param cuentaBancaria Parámetro que recibe un objeto Cuenta Bancaria con la información de la cuenta bancaria a consultar el saldo
     * @return Retorna un dato long con el saldo actual del cliente. Si no encuentra el cliente, retorna -1
     */
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

    /**
     * Consulta el código PIN de la cuenta bancaria del cliente
     * @param documento Parámetro que recibe una cadena de texto con el documento del cliente a consultar
     * @return Retorna una cadena de caracteres con el código PIN de la cuenta bancaria del cliente. Si no se encuentra el registro devuelve una cadena nula.
     */
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

    /**
     * Consulta el CVV de la tarjeta asociada a la cuenta bancaria del cliente
     * @param numeroCuenta Recibe una cadena de texto con el número de cuenta a consultar
     * @return Retorna una cadena de caracteres con el código CVV de la tarjeta
     */

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

    /**
     * Consulta el saldo de la cuenta bancaria del cliente
     * @param idCuenta Recibe el ID de la cuenta bancaria a consultar
     * @return Retorna un dato de tipo double con el saldo de la cuenta bancaria del cliente
     */
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

    /**
     * Consulta el ID del cliente según su documento
     * @param documento Recibe el número de documento del cliente a consultar
     * @return Retorna un entero con el ID del cliente. Si no se encuentra el cliente retorna -1
     */
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

    /**
     * Consulta el ID de la cuenta bancaria del cliente según su número de documento
     * @param documento Recibe una cadena de caracteres con el número de documento del cliente
     * @return Retorna un entero con el ID de la cuenta bancaria. Si no es encontrada, retorna -1
     */
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

    /**
     * Consulta el ID de la cuenta bancaria segun el número de la cuenta del cliente
     * @param numeroCuenta Recibe el número de la cuenta bancaria del cliente
     * @return Retorna un entero con el ID de la cuenta bancaria del cliente
     */
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

    /**
     * Consulta en la base de datos la existencia del usuario según el correo y la clave ingresada
     * @param correo Recibe una cadena de texto con el correo ingresado en el login
     * @param clave Recibe una cadena de texto con la clave ingresada en el login
     * @return Retorna un objeto Corresponsal con la información encontrada del corresponsal
     */
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

    /**
     * Consulta el correo electrónico del corresponsal según el ID de este
     * @param idCorresponsal Recibe un entero con el ID del corresponsal
     * @return Retorna una cadena de texto con el correo electrónico del corresponsal
     */
    public String consultarCorreoCorresponsal(int idCorresponsal) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT correo " +
                "FROM " + UtilidadesBD.CORRESPONSAL_TABLA + " " +
                "WHERE id = ?", new String[]{String.valueOf(idCorresponsal)});

        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        return null;
    }

    /**
     * Consulta la fecha de expiración de la tarjeta del cliente
     * @param numero_cuenta Recibe el número de la cuenta bancaria del cliente
     * @return Retorna una cadena de caracteres con la fecha de expiración de la tarjeta. Si no se encuentra, retorna una cadena de caracteres nula
     */
    public String consultarFechaExpiracion(String numero_cuenta) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT fecha_expiracion " +
                "FROM " + UtilidadesBD.TARJETA_TABLA + " t " +
                "JOIN " + UtilidadesBD.CUENTA_BANCARIA_TABLA + " cu ON cu.id_tarjeta = t.id " +
                "WHERE cu.numero_cuenta = ?", new String[]{numero_cuenta});

        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        return null;
    }

    /**
     * Consulta todos los registros en la base de datos de la acción de Retiros
     * @return Retorna una lista con los retiros que encontró en la consulta
     */
    public ArrayList<Retiro> consultarRetiros() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Retiro> listaRetiros = new ArrayList<>();
        Retiro retiro = null;
        CuentaBancaria cuentaBancaria = null;
        Cliente cliente = null;

        Cursor cursor = db.rawQuery("SELECT r.fecha, r.monto, c.documento " +
                "FROM " + UtilidadesBD.RETIRO_TABLA + " r " +
                "JOIN " + UtilidadesBD.CLIENTE_TABLA + " c ON c.id = r.id_cliente ", null);

        //Almacenar la lista
        while (cursor.moveToNext()) {
            retiro = new Retiro();
            cliente = new Cliente();
            cuentaBancaria = new CuentaBancaria();

            retiro.setFecha(cursor.getString(0));
            retiro.setMonto(cursor.getDouble(1));
            cliente.setDocumento(cursor.getString(2));

            cuentaBancaria.setCliente(cliente);

            retiro.setCuentaBancaria(cuentaBancaria);

            listaRetiros.add(retiro);

        }
        return listaRetiros;
    }

    /**
     * Consulta todos los registros de los depósitos realizados
     * @return Retorna una lista de depósitos
     */
    public ArrayList<Deposito> consultarDepositos() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Deposito> listaDepositos = new ArrayList<>();
        Deposito deposito = null;
        CuentaBancaria cuentaBancaria = null;
        Cliente cliente = null;

        Cursor cursor = db.rawQuery("SELECT d.fecha, d.monto, d.documento, c.documento " +
                "FROM " + UtilidadesBD.DEPOSITO_TABLA + " d " +
                "JOIN " + UtilidadesBD.CLIENTE_TABLA + " c ON c.id = d.id_cliente ", null);

        //Almacenar la lista
        while (cursor.moveToNext()) {
            deposito = new Deposito();
            cliente = new Cliente();
            cuentaBancaria = new CuentaBancaria();

            deposito.setFecha(cursor.getString(0));
            deposito.setMonto(cursor.getDouble(1));
            deposito.setDocumento(cursor.getString(2));
            cliente.setDocumento(cursor.getString(3));

            cuentaBancaria.setCliente(cliente);

            deposito.setCuentaBancaria(cuentaBancaria);

            listaDepositos.add(deposito);

        }
        return listaDepositos;
    }

    /**
     * Consulta todos los pagos con tarjeta realizaddos en la plataforma
     * @return Retorna una lista de Pagos con Tarjeta
     */
    public ArrayList<PagoTarjeta> consultarPagosTarjeta() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<PagoTarjeta> listaPagosTarjeta = new ArrayList<>();
        PagoTarjeta pagoTarjeta = null;
        CuentaBancaria cuentaBancaria = null;
        Cliente cliente = null;

        Cursor cursor = db.rawQuery("SELECT pt.fecha, pt.valor, cu.numero_cuenta, c.documento " +
                "FROM " + UtilidadesBD.PAGO_TARJETA_TABLA + " pt " +
                "JOIN " + UtilidadesBD.CUENTA_BANCARIA_TABLA + " cu ON cu.id = pt.id_cuenta " +
                "JOIN " + UtilidadesBD.CLIENTE_TABLA + " c ON c.id = cu.id_cliente ", null);

        //Almacenar la lista
        while (cursor.moveToNext()) {
            pagoTarjeta = new PagoTarjeta();
            cliente = new Cliente();
            cuentaBancaria = new CuentaBancaria();

            pagoTarjeta.setFecha(cursor.getString(0));
            pagoTarjeta.setValor(cursor.getDouble(1));
            cuentaBancaria.setNumero_cuenta(cursor.getString(2));
            cliente.setDocumento(cursor.getString(3));

            cuentaBancaria.setCliente(cliente);

            pagoTarjeta.setCuentaBancaria(cuentaBancaria);

            listaPagosTarjeta.add(pagoTarjeta);

        }
        return listaPagosTarjeta;
    }

    /**
     * Consulta todos los registros de transferencias realizadas por los clientes
     * @return Retorna una lista de Transferencias
     */
    public ArrayList<Transferencia> consultarTransferencias() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Transferencia> listaTransferencia = new ArrayList<>();
        Transferencia transferencia = null;
        CuentaBancaria cuentaBancariaRecibe = null;
        CuentaBancaria cuentaBancariaTransfiere = null;
        Cliente clienteRecibe = null;
        Cliente clienteTransfiere = null;

        Cursor cursor = db.rawQuery("SELECT t.fecha, t.monto, cr.documento, ct.documento " +
                "FROM " + UtilidadesBD.TRANSFERENCIA_TABLA + " t " +
                "JOIN " + UtilidadesBD.CUENTA_BANCARIA_TABLA + " cur ON cur.id = t.id_cuenta_recibe " +
                "JOIN " + UtilidadesBD.CUENTA_BANCARIA_TABLA + " cut ON cut.id = t.id_cuenta_transfiere " +
                "JOIN " + UtilidadesBD.CLIENTE_TABLA + " cr ON cr.id = cur.id_cliente " +
                "JOIN " + UtilidadesBD.CLIENTE_TABLA + " ct ON ct.id = cut.id_cliente ", null);

        //Almacenar la lista
        while (cursor.moveToNext()) {
            transferencia = new Transferencia();
            clienteRecibe = new Cliente();
            clienteTransfiere = new Cliente();
            cuentaBancariaRecibe = new CuentaBancaria();
            cuentaBancariaTransfiere = new CuentaBancaria();

            transferencia.setFecha(cursor.getString(0));
            transferencia.setMonto(cursor.getDouble(1));
            clienteRecibe.setDocumento(cursor.getString(2));
            clienteTransfiere.setDocumento(cursor.getString(3));

            cuentaBancariaRecibe.setCliente(clienteRecibe);
            cuentaBancariaTransfiere.setCliente(clienteTransfiere);

            transferencia.setCuentaTransfiere(cuentaBancariaTransfiere);
            transferencia.setCuentaRecibe(cuentaBancariaRecibe);

            listaTransferencia.add(transferencia);

        }
        return listaTransferencia;
    }

    public ArrayList<CuentaCreada> consultarCuentasCreadas() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<CuentaCreada> listaCuentasCreadas = new ArrayList<>();
        CuentaCreada cuentaCreada;
        Cliente cliente;

        Cursor cursor = db.rawQuery("SELECT cc.fecha, cc.monto, c.documento " +
                "FROM " + UtilidadesBD.CUENTA_CREADA_TABLA + " cc " +
                "JOIN " + UtilidadesBD.CLIENTE_TABLA + " c ON c.id = cc.id_cliente", null);

        //Almacenar la lista
        while (cursor.moveToNext()) {
            cuentaCreada = new CuentaCreada();
            cliente = new Cliente();

            cuentaCreada.setFecha(cursor.getString(0));
            cuentaCreada.setMontoInicial(cursor.getDouble(1));
            cliente.setDocumento(cursor.getString(2));
            cuentaCreada.setCliente(cliente);

            listaCuentasCreadas.add(cuentaCreada);

        }
        return listaCuentasCreadas;
    }

    public ArrayList<ConsultaSaldo> consultarConsultasSaldo() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<ConsultaSaldo> listaConsultaSaldo = new ArrayList<>();
        ConsultaSaldo consultaSaldo;
        Cliente cliente;

        Cursor cursor = db.rawQuery("SELECT cs.fecha, cs.saldo, c.documento " +
                "FROM " + UtilidadesBD.CONSULTA_SALDO_TABLA + " cs " +
                "JOIN " + UtilidadesBD.CLIENTE_TABLA + " c ON c.id = cs.id_cliente", null);

        //Almacenar la lista
        while (cursor.moveToNext()) {
            consultaSaldo = new ConsultaSaldo();
            cliente = new Cliente();

            consultaSaldo.setFecha(cursor.getString(0));
            consultaSaldo.setSaldo(cursor.getDouble(1));
            cliente.setDocumento(cursor.getString(2));
            consultaSaldo.setCliente(cliente);

            listaConsultaSaldo.add(consultaSaldo);

        }
        return listaConsultaSaldo;
    }
}

