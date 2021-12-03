package com.example.corresponsapp.utilidades;

public class UtilidadesBD {

    //CAMPOS ENTIDAD: cliente
    public static final String CLIENTE_TABLA = "cliente";
    public static final String CLIENTE_ID = "id";
    public static final String CLIENTE_DOCUMENTO = "documento";
    public static final String CLIENTE_NOMBRE_COMPLETO = "nombre_completo";

    public static final String CREAR_CLIENTE_TABLA = "CREATE TABLE " + CLIENTE_TABLA + " (" + CLIENTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + CLIENTE_DOCUMENTO +
            " TEXT, " + CLIENTE_NOMBRE_COMPLETO + " TEXT";

    //CAMPOS ENTIDAD: tarjeta
    public static final String TARJETA_TABLA = "tarjeta";
    public static final String TARJETA_ID = "id";
    public static final String TARJETA_FECHA_EXPIRACION = "fecha_expiracion";
    public static final String TARJETA_CVV = "cvv";

    public static final String CREAR_TARJETA_TABLA = "CREATE TABLE " + TARJETA_TABLA + " (" + TARJETA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + TARJETA_FECHA_EXPIRACION +
            " TEXT, " + TARJETA_CVV + " TEXT";


    //CAMPOS ENTIDAD: cuenta
    public static final String CUENTA_BANCARIA_TABLA = "cuenta_bancaria";
    public static final String CUENTA_BANCARIA_ID = "id";
    public static final String CUENTA_BANCARIA_NUMERO_CUENTA = "numero_cuenta";
    public static final String CUENTA_BANCARIA_PIN = "pin";
    public static final String CUENTA_BANCARIA_SALDO = "saldo";
    public static final String CUENTA_BANCARIA_FK_CLIENTE = "id_cliente";
    public static final String CUENTA_BANCARIA_FK_TARJETA = "id_tarjeta";

    public static final String CREAR_CUENTA_BANCARIA_TABLA = "CREATE TABLE " + CUENTA_BANCARIA_TABLA + " (" + CUENTA_BANCARIA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            CUENTA_BANCARIA_NUMERO_CUENTA + " TEXT, " + CUENTA_BANCARIA_PIN + " TEXT" + CUENTA_BANCARIA_SALDO + " REAL" + CUENTA_BANCARIA_FK_CLIENTE + " INTEGER"  + CUENTA_BANCARIA_FK_TARJETA + " INTEGER";



}
