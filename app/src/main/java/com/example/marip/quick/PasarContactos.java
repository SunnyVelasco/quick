package com.example.marip.quick;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PasarContactos extends AppCompatActivity {
    //TextView txtContacto, txtNumContacto;


    Button btnManual, btnAgenda;
    static final int PICK_CONTACT_RESQUEST = 1;
    String nombre, telefono;

    BaseDatos db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pasar_contactos);

        btnManual = (Button) findViewById(R.id.btnManual);
        btnAgenda = (Button) findViewById(R.id.btnAgenda);
        db = new BaseDatos(PasarContactos.this);
        btnManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Contactos = new Intent(PasarContactos.this, contactos.class);
                startActivity(Contactos);
            }
        });




        btnAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                seleccionarContacto();

            }
        });

    }
    private void seleccionarContacto() {
        Intent seleccContactoIntent=new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        seleccContactoIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(seleccContactoIntent,PICK_CONTACT_RESQUEST);



        //al insertar el contacto se regresa a la vista principal--- borrar los datos

    }

   // @Override public boolean onKeyDown(int keyCode, KeyEvent event) { if(keyCode==KeyEvent.KEYCODE_BACK) { return false; } return super.onKeyDown(keyCode, event); }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        ///
        LayoutInflater inflater = LayoutInflater.from(this);
        View vistaEliminar = inflater.inflate(R.layout.confirmarcontacto, null);

        final TextView camponombre = vistaEliminar.findViewById(R.id.nombreV);
        final TextView campotelefono = vistaEliminar.findViewById(R.id.telefonoV);

        camponombre.setText("Nombre: " + nombre);
        camponombre.setEnabled(false);


        campotelefono.setText("Telefono: " + telefono);
        campotelefono.setEnabled(false);

        View focus = null;


        //////


        if (requestCode == PICK_CONTACT_RESQUEST) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);


                if (cursor.moveToFirst()) {
                    int columnaNombre = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    int columnaNumero = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                    String nombre2 = cursor.getString(columnaNombre);
                    String numero2 = cursor.getString(columnaNumero);

                    camponombre.setText(nombre2);
                    campotelefono.setText(numero2);

                }
            }
        }
        try {
            new AlertDialog.Builder(this)
                    .setTitle("Deseas Agregar el Contacto")
                    .setView(vistaEliminar)
                    .setPositiveButton("Agregar", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //eliminacion de registro
                            nombre = camponombre.getText().toString();
                            telefono = campotelefono.getText().toString();
                            Cursor cursor = db.consul_telefonoagregado(telefono);


                            if (cursor.moveToFirst() && cursor.getCount() > 0) {

                                Toast.makeText(getApplicationContext(), "Numero de telefono ya existente", Toast.LENGTH_SHORT).show();


                            } else {
                                db.insertarContacto(nombre, telefono);
                                Cursor c = db.consul_contacto();
                                c.moveToLast();

                                Intent Contactos = new Intent(PasarContactos.this, PasarContactos.class);
                                startActivity(Contactos);
                                Toast.makeText(getApplicationContext(), "Contacto Agregado", Toast.LENGTH_SHORT).show();
                            }

                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getApplicationContext(), "Cancelado", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
