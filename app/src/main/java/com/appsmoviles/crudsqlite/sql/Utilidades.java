package com.appsmoviles.crudsqlite.sql;

public class Utilidades {

    //Constantes campos usuarios
    public static final String TABLA_CABALLEROS = "caballeros";
    public static final String CAMPO_ID = "id";
    public static final String CAMPO_NOMBRE = "nombre";
    public static final String CAMPO_LUGAR = "lugar_nacimiento";;

    //Query creacion tabla y campos PACIENTES
    public static final String CREAR_TABLA_CABALLEROS = "CREATE TABLE " + TABLA_CABALLEROS
                                                        +"(" + CAMPO_ID + " TEXT PRIMARY KEY NOT NULL, "
                                                        + CAMPO_NOMBRE + " TEXT, "
                                                        + CAMPO_LUGAR + " TEXT )";

}
