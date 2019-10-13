package com.example.marip.quick;;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.marip.quick.BaseDatos;

import java.util.ArrayList;

/**
 * Created by Jorge & alonso :v on 02/05/2017.
 */

public class AyudaBD extends SQLiteOpenHelper{
    //Creamos una variable que contendra la sentencia SQL de creación de la tabla
    private static final String DB_NAME = "BaseDatos_Quick.db";
    private static final int DB_VERSION = 4;
    public static final String NOMBRE_TABLA="contactos";
    public static final String CAMPO_ID="_id";
    public static final String CAMPO_NC="nombre";
    public static final String CAMPO_TEL="telefono";
    private Context contexto;
   // String sql = "CREATE TABLE Cliente (Id INTEGER, Nombre TEXT)";
    public AyudaBD(Context context) {
        super(context, DB_NAME,null,DB_VERSION);
        this.contexto= context;

    }


    //2da referencia de contexto




    @Override
    public void onCreate(SQLiteDatabase db) {
        //Este metodo ejecuta automaticamente cuando la base de datos no existe, es decir, que la primera vez que se hace llamado
        //a la clase, crea la BD
        db.execSQL(BaseDatos.sqlCreate);
        db.execSQL(BaseDatos.sqlCreate2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Este método se ejecuta cuando se detecta que la version de la base de datos cambió, por lo que se debe definir todos los
        // procesos de migracion de la estructura anterior a la estrucutra nueva.
    }

}
