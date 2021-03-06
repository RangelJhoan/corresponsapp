package com.example.corresponsapp.utilidades;

public class UtilidadesBD {

    //CAMPOS ENTIDAD: cliente
    public static final String CLIENTE_TABLA = "cliente";
    public static final String CLIENTE_ID = "id";
    public static final String CLIENTE_DOCUMENTO = "documento";
    public static final String CLIENTE_NOMBRE_COMPLETO = "nombre_completo";

    public static final String CREAR_CLIENTE_TABLA = "CREATE TABLE " + CLIENTE_TABLA + " (" + CLIENTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + CLIENTE_DOCUMENTO +
            " TEXT, " + CLIENTE_NOMBRE_COMPLETO + " TEXT)";

    //CAMPOS ENTIDAD: tarjeta
    public static final String TARJETA_TABLA = "tarjeta";
    public static final String TARJETA_ID = "id";
    public static final String TARJETA_FECHA_EXPIRACION = "fecha_expiracion";
    public static final String TARJETA_CVV = "cvv";

    public static final String CREAR_TARJETA_TABLA = "CREATE TABLE " + TARJETA_TABLA + " (" + TARJETA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + TARJETA_FECHA_EXPIRACION +
            " TEXT, " + TARJETA_CVV + " TEXT)";


    //CAMPOS ENTIDAD: cuenta
    public static final String CUENTA_BANCARIA_TABLA = "cuenta_bancaria";
    public static final String CUENTA_BANCARIA_ID = "id";
    public static final String CUENTA_BANCARIA_NUMERO_CUENTA = "numero_cuenta";
    public static final String CUENTA_BANCARIA_PIN = "pin";
    public static final String CUENTA_BANCARIA_SALDO = "saldo";
    public static final String CUENTA_BANCARIA_FK_CLIENTE = "id_cliente";
    public static final String CUENTA_BANCARIA_FK_TARJETA = "id_tarjeta";

    public static final String CREAR_CUENTA_BANCARIA_TABLA = "CREATE TABLE " + CUENTA_BANCARIA_TABLA + " (" + CUENTA_BANCARIA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            CUENTA_BANCARIA_NUMERO_CUENTA + " TEXT, " + CUENTA_BANCARIA_PIN + " TEXT, " + CUENTA_BANCARIA_SALDO + " REAL, " + CUENTA_BANCARIA_FK_CLIENTE + " INTEGER, "  + CUENTA_BANCARIA_FK_TARJETA + " INTEGER)";

    //CAMPOS ENTIDAD: corresponsal
    public static final String CORRESPONSAL_TABLA = "corresponsal";
    public static final String CORRESPONSAL_ID = "id";
    public static final String CORRESPONSAL_NOMBRE_COMPLETO = "nombre_completo";
    public static final String CORRESPONSAL_CORREO = "correo";
    public static final String CORRESPONSAL_CLAVE = "clave";
    public static final String CORRESPONSAL_SALDO = "saldo";

    public static final String CREAR_CORRESPONSAL_TABLA = "CREATE TABLE " + CORRESPONSAL_TABLA + " (" + CORRESPONSAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + CORRESPONSAL_NOMBRE_COMPLETO +
            " TEXT, " + CORRESPONSAL_CORREO + " TEXT, " + CORRESPONSAL_CLAVE + " TEXT, " + CORRESPONSAL_SALDO + " REAL)";

    //CAMPOS ENTIDAD: deposito
    public static final String DEPOSITO_TABLA = "deposito";
    public static final String DEPOSITO_ID = "id";
    public static final String DEPOSITO_DOCUMENTO = "documento";
    public static final String DEPOSITO_FK_CLIENTE = "id_cliente";
    public static final String DEPOSITO_MONTO = "monto";
    public static final String DEPOSITO_FECHA = "fecha";

    public static final String CREAR_DEPOSITO_TABLA = "CREATE TABLE " + DEPOSITO_TABLA + " (" + DEPOSITO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + DEPOSITO_DOCUMENTO +
            " TEXT, " + DEPOSITO_FK_CLIENTE + " INTEGER," + DEPOSITO_MONTO + " REAL, " + DEPOSITO_FECHA + " DEFAULT CURRENT_TIMESTAMP)";

    //CAMPOS ENTIDAD: retiro
    public static final String RETIRO_TABLA = "retiro";
    public static final String RETIRO_ID = "id";
    public static final String RETIRO_FK_CLIENTE = "id_cliente";
    public static final String RETIRO_MONTO = "monto";
    public static final String RETIRO_FECHA = "fecha";

    public static final String CREAR_TABLA_RETIRO = "CREATE TABLE " + RETIRO_TABLA + " (" + RETIRO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            RETIRO_FK_CLIENTE + " INTEGER," + RETIRO_MONTO + " REAL, " + RETIRO_FECHA + " DEFAULT CURRENT_TIMESTAMP)";

    //CAMPOS ENTIDAD: pago tarjeta
    public static final String PAGO_TARJETA_TABLA = "pago_tarjeta";
    public static final String PAGO_TARJETA_ID = "id";
    public static final String PAGO_TARJETA_FK_CUENTA = "id_cuenta";
    public static final String PAGO_TARJETA_VALOR = "valor";
    public static final String PAGO_TARJETA_COUTAS = "cuotas";
    public static final String PAGO_TARJETA_FECHA = "fecha";

    public static final String CREAR_TABLA_PAGO_TARJETA = "CREATE TABLE " + PAGO_TARJETA_TABLA + " (" + PAGO_TARJETA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            PAGO_TARJETA_FK_CUENTA + " INTEGER," + PAGO_TARJETA_VALOR + " REAL, " + PAGO_TARJETA_COUTAS + " INTEGER, " + PAGO_TARJETA_FECHA + " DEFAULT CURRENT_TIMESTAMP)";

    //CAMPOS ENTIDAD: transferencia
    public static final String TRANSFERENCIA_TABLA = "transferencia";
    public static final String TRANSFERENCIA_ID = "id";
    public static final String TRANSFERENCIA_FK_CUENTA_RECIBE = "id_cuenta_recibe";
    public static final String TRANSFERENCIA_FK_CUENTA_TRANSFIERE = "id_cuenta_transfiere";
    public static final String TRANSFERENCIA_MONTO = "monto";
    public static final String TRANSFERENCIA_FECHA = "fecha";

    public static final String CREAR_TABLA_TRANSFERENCIA = "CREATE TABLE " + TRANSFERENCIA_TABLA + " (" + TRANSFERENCIA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            TRANSFERENCIA_FK_CUENTA_RECIBE + " INTEGER," + TRANSFERENCIA_FK_CUENTA_TRANSFIERE + " INTEGER," + TRANSFERENCIA_MONTO + " REAL, " + TRANSFERENCIA_FECHA + " DEFAULT CURRENT_TIMESTAMP)";

    //CAMPOS ENTIDAD: consulta_saldo
    public static final String CONSULTA_SALDO_TABLA = "consulta_saldo";
    public static final String CONSULTA_SALDO_ID = "id";
    public static final String CONSULTA_SALDO_FK_CLIENTE = "id_cliente";
    public static final String CONSULTA_SALDO_SALDO = "saldo";
    public static final String CONSULTA_SALDO_FECHA = "fecha";

    public static final String CREAR_TABLA_CONSULTA_SALDO = "CREATE TABLE " + CONSULTA_SALDO_TABLA + " (" + CONSULTA_SALDO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            CONSULTA_SALDO_FK_CLIENTE + " INTEGER," + CONSULTA_SALDO_SALDO + " REAL, " + CONSULTA_SALDO_FECHA + " DEFAULT CURRENT_TIMESTAMP)";

    //CAMPOS ENTIDAD: cuenta_creada
    public static final String CUENTA_CREADA_TABLA = "cuenta_creada";
    public static final String CUENTA_CREADA_ID = "id";
    public static final String CUENTA_CREADA_FK_CLIENTE = "id_cliente";
    public static final String CUENTA_CREADA_MONTO = "monto";
    public static final String CUENTA_CREADA_FECHA = "fecha";

    public static final String CREAR_TABLA_CUENTA_CREADA = "CREATE TABLE " + CUENTA_CREADA_TABLA + " (" + CUENTA_CREADA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            CUENTA_CREADA_FK_CLIENTE + " INTEGER," + CUENTA_CREADA_MONTO + " REAL, " + CUENTA_CREADA_FECHA + " DEFAULT CURRENT_TIMESTAMP)";

}
