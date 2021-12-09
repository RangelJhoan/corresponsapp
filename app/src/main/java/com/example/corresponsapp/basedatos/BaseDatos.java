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
        db.execSQL("INSERT INTO " + UtilidadesBD.CORRESPONSAL_TABLA + " (nombre_completo, saldo, correo, clave) VALUES ('Jhoan Rangel',10000, 'jhoan', '123')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + UtilidadesBD.CLIENTE_TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + UtilidadesBD.TARJETA_TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + UtilidadesBD.CUENTA_BANCARIA_TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + UtilidadesBD.DEPOSITO_TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + UtilidadesBD.CORRESPONSAL_TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + UtilidadesBD.RETIRO_TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + UtilidadesBD.PAGO_TARJETA_TABLA);
        onCreate(db);
    }

    //GESTIÓN DEL CORRESPONSAL
    //CREAR CUENTA DEL CLIENTE
    public long crearCuenta(CuentaBancaria cuentaBancaria) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Consultar el id del cliente apartir del documento para saber si ya se encuentra registrado
        long idCliente = consultarIdCliente(cuentaBancaria.getCliente().getDocumento());
        if (idCliente <= 0) {
            //Guardamos el registro del cliente nuevo
            long respuestaCliente = crearCliente(cuentaBancaria.getCliente());
            if (respuestaCliente > 0) {
                //Le asignamos una tarjeta
                long respuestaTarjeta = crearTarjeta(cuentaBancaria.getTarjeta());
                if (respuestaTarjeta > 0) {

                    ContentValues valuesCuenta = new ContentValues();
                    valuesCuenta.put(UtilidadesBD.CUENTA_BANCARIA_NUMERO_CUENTA, cuentaBancaria.getNumero_cuenta());
                    valuesCuenta.put(UtilidadesBD.CUENTA_BANCARIA_PIN, cuentaBancaria.getPIN());
                    valuesCuenta.put(UtilidadesBD.CUENTA_BANCARIA_SALDO, cuentaBancaria.getSaldo());
                    valuesCuenta.put(UtilidadesBD.CUENTA_BANCARIA_FK_CLIENTE, respuestaCliente);
                    valuesCuenta.put(UtilidadesBD.CUENTA_BANCARIA_FK_TARJETA, respuestaTarjeta);

                    //Le asignamos una cuenta bancaria
                    long respuestaCuenta = db.insert(UtilidadesBD.CUENTA_BANCARIA_TABLA, UtilidadesBD.CUENTA_BANCARIA_ID, valuesCuenta);

                    if (respuestaCuenta > 0) {
                        //Cuando se registre el cliente se le debe sumar 10000 al corresponsal
                        long respuestaComision = registrarComision(Sesion.corresponsalSesion.getId(), Constantes.COMISION_CUENTA_NUEVA);
                        if (respuestaComision > 0) {
                            return respuestaComision;
                        } else {
                            //RETORNAR -5: Comisión no registrada
                            return -5;
                        }
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
        } else {
            //RETORNAR -4: Documento previamente registrado
            return -4;
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
        int idCliente = consultarIdCliente(deposito.getCuentaBancaria().getCliente().getDocumento());
        //Verificamos que el cliente esté registrado
        if (idCliente > 0) {
            //Verificamos que el cliente tenga asignada una cuenta bancaria
            int idCuenta = consultarIdCuentaDocumento(deposito.getCuentaBancaria().getCliente().getDocumento());
            if (idCuenta > 0) {
                //Verificamos que se haya actualizado la cuenta bancaria del cliente
                long resultadoDepositoCliente = depositarDinero(idCuenta, deposito.getMonto());
                if (resultadoDepositoCliente > 0) {
                    SQLiteDatabase db = this.getWritableDatabase();
                    ContentValues valuesDeposito = new ContentValues();
                    valuesDeposito.put(UtilidadesBD.DEPOSITO_DOCUMENTO, deposito.getDocumento());
                    valuesDeposito.put(UtilidadesBD.DEPOSITO_FK_CLIENTE, idCliente);
                    valuesDeposito.put(UtilidadesBD.DEPOSITO_MONTO, deposito.getMonto());

                    //Verificamos que se haya registrado el depósito en el sistema
                    long resultadoDeposito = db.insert(UtilidadesBD.DEPOSITO_TABLA, UtilidadesBD.DEPOSITO_ID, valuesDeposito);
                    if (resultadoDeposito > 0) {
                        return resultadoDeposito;
                    } else {
                        //Retornar -4: No se creó el depósito
                        return -4;
                    }
                } else {
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

    private long depositarDinero(int idCuenta, double deposito) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesDeposito = new ContentValues();
        double nuevoSaldo = consultarSaldoCuenta(idCuenta) + deposito;
        valuesDeposito.put(UtilidadesBD.CUENTA_BANCARIA_SALDO, nuevoSaldo);
        //Se le suma al corresponsal el valor del depósito
        long respuestaComision = registrarComision(Sesion.corresponsalSesion.getId(), Constantes.COMISION_DEPOSITAR);
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
        //Consultar que el PIN ingresado sea igual al del cliente
        if (retiro.getCuentaBancaria().getPIN().equals(consultarPINCuenta(retiro.getCuentaBancaria().getCliente().getDocumento()))) {
            //Consultar que el cliente exista
            int idCliente = consultarIdCliente(retiro.getCuentaBancaria().getCliente().getDocumento());
            if (idCliente > 0) {
                //Consultar que el cliente tenga asignada una cuenta bancaria
                int idCuenta = consultarIdCuentaDocumento(retiro.getCuentaBancaria().getCliente().getDocumento());
                if (idCuenta > 0) {
                    //Retirar el monto de la cuenta del cliente
                    long resultadoRetiroCliente = retirarDinero(idCuenta, retiro.getMonto());
                    if (resultadoRetiroCliente > 0) {
                        SQLiteDatabase db = this.getWritableDatabase();
                        ContentValues valuesRetiro = new ContentValues();
                        valuesRetiro.put(UtilidadesBD.RETIRO_FK_CLIENTE, idCliente);
                        valuesRetiro.put(UtilidadesBD.RETIRO_MONTO, retiro.getMonto());

                        long resultadoDeposito = db.insert(UtilidadesBD.RETIRO_TABLA, UtilidadesBD.RETIRO_ID, valuesRetiro);

                        if (resultadoDeposito > 0) {
                            return resultadoDeposito;
                        } else {
                            //Retornar -4: No se creó el retiro
                            return -4;
                        }
                    } else if (resultadoRetiroCliente == -3) {
                        //Retornar -6: Saldo insuficiente
                        return -6;
                    } else {
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
        } else {
            //Retornar -5: PIN incorrecto
            return -5;
        }
    }

    private long retirarDinero(int idCuenta, double retiro) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Validar que al saldo del cliente se le pueda retirar el dinero y la comisión
        double saldoDisponible = consultarSaldoCuenta(idCuenta) - (retiro + Constantes.COMISION_RETIRAR);

        if (saldoDisponible >= 0) {

            //Registrar la comisión en la cuenta del corresponsal
            long respuestaComision = registrarComision(Sesion.corresponsalSesion.getId(), Constantes.COMISION_RETIRAR);

            if (respuestaComision > 0) {
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
                //Retornar -2: No se registró la comisión
                return -2;
            }
        } else {
            //Retornar -3; Saldo insuficiente para el retiro
            return -3;
        }
    }

    public long crearPagoTarjeta(PagoTarjeta pagoTarjeta) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesPagoTarjeta = new ContentValues();

        int idCuenta = consultarIdCuentaNumero(pagoTarjeta.getCuentaBancaria().getNumero_cuenta());
        if (idCuenta > 0) {
            valuesPagoTarjeta.put(UtilidadesBD.PAGO_TARJETA_FK_CUENTA, idCuenta);
            valuesPagoTarjeta.put(UtilidadesBD.PAGO_TARJETA_VALOR, pagoTarjeta.getValor());
            valuesPagoTarjeta.put(UtilidadesBD.PAGO_TARJETA_COUTAS, pagoTarjeta.getNumeroCuotas());

            long resultadoPagoTarjeta = db.insert(UtilidadesBD.PAGO_TARJETA_TABLA, UtilidadesBD.PAGO_TARJETA_ID, valuesPagoTarjeta);

            if (resultadoPagoTarjeta > 0) {
                return resultadoPagoTarjeta;
            } else {
                //Retornar -2: No se creó el pago con tarjeta
                return -2;
            }
        } else {
            //RETORNAR -1: No se encontró la cuenta bancaria
            return -1;
        }

    }

    private String consultarPINCuenta(String documento) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT cu.pin FROM " + UtilidadesBD.CUENTA_BANCARIA_TABLA + " cu " +
                "JOIN cliente c ON c.id = cu.id_cliente " +
                "WHERE c.documento = ?", new String[]{documento});

        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        } else {
            //Retornar -1: No se encontró el PIN
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
    private long registrarComision(int idCorresponsal, double comision) {
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

    private double consultarSaldoCorresponsal(int idCorresponsal) {
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
        //Validar que el cliente exista
        int idCliente = consultarIdCliente(cuentaBancaria.getCliente().getDocumento());
        if (idCliente > 0) {
            //Validar que el PIN de la cuenta sea igual al ingresado
            if (cuentaBancaria.getPIN().equals(consultarPINCuenta(cuentaBancaria.getCliente().getDocumento()))) {
                //Validar que el cliente tenga asignada una cuenta bancaria
                int idCuenta = consultarIdCuentaDocumento(cuentaBancaria.getCliente().getDocumento());
                if (idCuenta > 0) {
                    //Realizar el retiro de la comisión a la cuenta bancaria del cliente
                    long respuestaRetiro = retirarDinero(idCuenta, Constantes.COMISION_CONSULTAR_SALDO);
                    if (respuestaRetiro > 0) {
                        //Validar que se registre la comisión al corresponsal
                        if (registrarComision(Sesion.corresponsalSesion.getId(), Constantes.COMISION_CONSULTAR_SALDO) > 0) {
                            Cursor cursor = db.rawQuery("SELECT cu.saldo " +
                                    "FROM " + UtilidadesBD.CLIENTE_TABLA + " cl " +
                                    "JOIN " + UtilidadesBD.CUENTA_BANCARIA_TABLA + " cu ON cu.id_cliente = cl.id " +
                                    "WHERE cl.documento = ?", new String[]{String.valueOf(cuentaBancaria.getCliente().getDocumento())});

                            //Validar que se haya encontrado el saldo
                            if (cursor.moveToFirst()) {
                                return cursor.getLong(0);
                            } else {
                                //Consulta no realizada
                                return -2;
                            }
                        } else {
                            //Error al registrar la comisión
                            return -4;
                        }
                    } else {
                        //No se pudo relizar el retiro de la comisión
                        return -6;
                    }
                } else {
                    //Cuenta bancaria del cliente no registrada
                    return -5;
                }
            } else {
                //PIN no coincide
                return -3;
            }
        } else {
            //Cliente no registrado
            return -1;
        }
    }
}

