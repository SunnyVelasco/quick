package apps.ejemplo.marip.quick;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import apps.ejemplo.marip.quick.R;

public class contactos extends AppCompatActivity {
    EditText txtContacto, txtNumContacto;
    Button btnAddContacto, btn_back, btnAgenda;
    String nombre, telefono;
    BaseDatos db;
    static final int PICK_CONTACT_RESQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);
        db = new BaseDatos(contactos.this);
        txtContacto = (EditText) findViewById(R.id.txt_contacto);
        txtNumContacto = (EditText) findViewById(R.id.txt_NumContacto);
        btnAddContacto = (Button) findViewById(R.id.btn_AddContacto);
        // btnAgenda = (Button) findViewById(R.id.botonAgenda);
        btnAddContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertarContacto();

                //al insertar el contacto se regresa a la vista principal--- borrar los datos
                Intent Regresar = new Intent(contactos.this, contactos.class);
                startActivity(Regresar);
               // finish(); //modifique para que 01/09/2019

            }
        });
        findViewById(R.id.txt_contacto).requestFocus();

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Regresar = new Intent(contactos.this, boton_emergencia.class);
                startActivity(Regresar);
               // finish(); //aqui*****
            }
        });

        btnAgenda = (Button) findViewById(R.id.botonAgenda);
        btnAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarContacto();
               // finish(); //aqui*****

            }
        });


    }

    private void seleccionarContacto() {
        Intent seleccContactoIntent=new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        seleccContactoIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(seleccContactoIntent,PICK_CONTACT_RESQUEST);
    }




        private void InsertarContacto () {
        //modifique para que no se guarden datos vacios
            View focus = null;
                nombre = txtContacto.getText().toString();
                telefono = txtNumContacto.getText().toString();
            Cursor cursor = db.consul_telefonoagregado(telefono);

            try {
                if (nombre.isEmpty() || telefono.isEmpty()) {
                    Toast.makeText(this, "Favor de llenar todos los campos", Toast.LENGTH_SHORT).show();

                }




              else if(cursor.moveToFirst() && cursor.getCount() > 0){
                    Toast.makeText(this, "Numero de telefono ya existente", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, "Revise los campos", Toast.LENGTH_SHORT).show();
                  focus.requestFocus();

                }


                    else {
                        db.insertarContacto(nombre, telefono);
                        Cursor c = db.consul_contacto();
                        c.moveToLast();
                        Toast.makeText(this, "Telefono Agregado:" + c.getString(c.getColumnIndex("telefono")), Toast.LENGTH_LONG).show();

                    }


            }
            catch(Exception e){
                    e.printStackTrace();
                }

            }
//para que no regrese
    @Override public boolean onKeyDown(int keyCode, KeyEvent event) { if(keyCode==KeyEvent.KEYCODE_BACK) { return false; } return super.onKeyDown(keyCode, event); }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT_RESQUEST) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);



                if (cursor.moveToFirst()) {
                    int columnaNombre = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    int columnaNumero = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                    String nombre = cursor.getString(columnaNombre);
                    String numero = cursor.getString(columnaNumero);

                    txtContacto.setText(nombre);
                    txtNumContacto.setText(numero);

                }
            }
        }
    }


    }

