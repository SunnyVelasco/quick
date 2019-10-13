package com.example.marip.quick;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Alonso Pola on 15/10/2017.
 */

public class BaseDatos  {

    public static final String sqlCreate = "CREATE TABLE usuarios (_id integer primary key autoincrement, usuario TEXT, password TEXT, correo TEXT, cp TEXT,telefono TEXT)";
    public static final String sqlCreate2 = "CREATE TABLE contactos (_id integer primary key autoincrement, nombre TEXT, telefono TEXT)";
    private AyudaBD helper;
    private SQLiteDatabase db;
    private SQLiteDatabase x;
    public BaseDatos(Context context){
        helper = new AyudaBD(context);
        db = helper.getWritableDatabase();
        x = helper.getReadableDatabase();
    }

    public void insertarusuario (String usuario, String correo, String cp, String password,String telefono){
        db.insert("usuarios",null, generar_contenedorT1(usuario,correo,cp,password,telefono));
    }
    public void insertarusuarioN (String usuario, String correo, String cp, String password,String telefono){
     //   db.insert("usuarios",null, generar_contenedorT1(usuario,correo,cp,password,telefono));
        db.execSQL("INSERT INTO usuarios (usuario,password,correo,cp,telefono) VALUES ("+usuario+","+correo+","+cp+","+password+","+telefono+") WHERE NOT EXISTS (SELECT * FROM usuarios WHERE usuario="+usuario+" AND correo="+correo+" AND telefono="+telefono+")");
    }
    public boolean consul_usuario (String usuario,String password){
        Cursor cursor = null;
        Cursor cursorN = null;
        String contra;
        cursor = db.rawQuery("SELECT * FROM usuarios WHERE usuario=?",new String[]{usuario});
        if(cursor.getCount()!=0) {
            cursor.moveToFirst();
            contra = cursor.getString(cursor.getColumnIndex("password"));
            if(contra.equals(password)){
                return true;
            }
            else{
                return false;
            }
        }else{
            return false;
        }
    }

    public boolean consul_usuario2 (String password){
        Cursor cursor = null;
        Cursor cursorN = null;
        String contra;
        cursor = db.rawQuery("SELECT * FROM usuarios WHERE usuario=?",new String[]{password});
        if(cursor.getCount()!=0) {
            cursor.moveToFirst();
            contra = cursor.getString(cursor.getColumnIndex("password"));
            if(contra.equals(password)){
                return true;
            }
            else{
                return false;
            }
        }else{
            return false;
        }
    }

    public boolean consul_usuario3 (String usuario){
        Cursor cursor = null;
        Cursor cursorN = null;
        String contra;
        cursor = db.rawQuery("SELECT * FROM usuarios WHERE usuario=?",new String[]{usuario});
        if(cursor.getCount()!=0) {
            cursor.moveToFirst();
            contra = cursor.getString(cursor.getColumnIndex("usuario"));
            if(contra.equals(usuario)){
                return true;
            }
            else{
                return false;
            }
        }else{
            return false;
        }
    }

    public Cursor consul_usuario (String usuario){
        Cursor cursor = null;
        cursor = db.rawQuery("SELECT * FROM usuarios WHERE usuario=?",new String[]{usuario});
        return cursor;
    }

    public Cursor consul_telefono (String telefono){
        Cursor cursor = null;
        cursor = db.rawQuery("SELECT * FROM usuarios WHERE telefono=?",new String[]{telefono});
        return cursor;
    }


    public Cursor consul_N (String password){
        Cursor cursor = null;
        cursor = db.rawQuery("SELECT * FROM usuarios WHERE password=?",new String[]{password});
        return cursor;
    }

    private ContentValues generar_contenedorT1 (String usuario, String correo, String cp, String password,String telefono){
        ContentValues valores = new ContentValues();
        valores.put("usuario",usuario);
        valores.put("password",password);
        valores.put("cp",cp);
        valores.put("correo",correo);
        valores.put("telefono",telefono);

        return valores;
    }

    public void actualizar (String id, String passwordN){
        db.execSQL("UPDATE usuarios SET password='" + passwordN + "WHERE _id='" + id);
    }

    public void eliminar (String id){
        db.rawQuery("DELETE FROM usuarios WHERE _id=?", new String[]{id});
    }
//-------------------------------------- METODOS DE TABLA CONTACTOS ---------------------------------
    public void insertarContacto (String nombre, String telefono){
        db.insert("contactos",null, generar_contenedorContacto(nombre,telefono));
    }
    private ContentValues generar_contenedorContacto (String nombre, String telefono){
        ContentValues valores = new ContentValues();
        valores.put("nombre",nombre);
        valores.put("telefono",telefono);

        return valores;
    }
    public Cursor consul_contacto (){
        Cursor cursor = null;
        cursor = db.rawQuery("SELECT * FROM contactos",null);
        return cursor;
    }

    public Cursor consul_telefonoagregado (String telefonoa){
        Cursor cursor = null;
        cursor = db.rawQuery("SELECT * FROM contactos WHERE telefono=?",new String[]{telefonoa});
        return cursor;
    }

    public Cursor consul_contacto2 (String contacto){
        Cursor cursor = null;
        cursor = db.rawQuery("SELECT * FROM contactos WHERE contacto=?",new String[]{contacto});
        return cursor;
    }
    public Cursor eliminar2(String id){
        db.rawQuery("DELETE FROM contactos WHERE _id=?", new String[]{id});
        return null;
    }

    public void eliminar2n (String id){
        db.rawQuery("DELETE FROM contactos WHERE _id=?", new String[]{id});
    }



}
