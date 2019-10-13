package com.example.marip.quick;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


public class registro extends AppCompatActivity {
    Button CrearC;
    EditText Usuario, Correo, Cp, Password,Telefono;
    BaseDatos db;
    private AyudaBD helper;
    private SQLiteDatabase x;
    private Cursor cursor;
    private String llaveCursor;
    private SQLiteDatabase db2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);


        // (this);

        CrearC = (Button) findViewById(R.id.btn_cuenta);
        Usuario = (EditText) findViewById(R.id.txt_UsuarioC);
        Correo = (EditText) findViewById(R.id.txt_Correo2);
        Cp = (EditText) findViewById(R.id.txt_codP);
        Password = (EditText) findViewById(R.id.txt_pass);
        Telefono=(EditText)findViewById(R.id.txt_TelUser);
        findViewById(R.id.txt_UsuarioC).requestFocus();
        db = new BaseDatos(registro.this);
        db2= new AyudaBD(this).getWritableDatabase();

        final EditText texto_nombre = findViewById(R.id.txt_UsuarioC);
        final EditText texto_telefono = findViewById(R.id.txt_TelUser);
        final EditText texto_email = findViewById(R.id.txt_Correo2);

        CrearC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // new DescargarImagen(registro.this).execute(Usuario,Correo,Password,Cp,Telefono);
                String nombre = texto_nombre.getText().toString();
                String telefono = texto_telefono.getText().toString();
                String correo = texto_email.getText().toString();
                new InsertWeb(registro.this).execute(nombre,telefono,correo);
                InsertarUsuario();
                //esto es nuevo una clase interna


               Intent CrearC = new Intent(registro.this, MainActivity.class);
                startActivity(CrearC);
               // finish(); //aqui*****

            }
        });


    }

    public static class InsertWeb extends AsyncTask<String, Void, String> {

        private WeakReference<Context> context;
        public InsertWeb(Context context){
            this.context = new WeakReference<>(context);
        }

        @Override
        protected String doInBackground(String... params) {
            String registrar_url = "http://baseq.freeoda.com/registrar.php";
            String resultado;
            try {
                URL url = new URL(registrar_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

                String nombre = params[0];
                String telefono = params[1];
                String correo = params[2];
                //  String cp = params[2];
                // String password = params[3];


                String data = URLEncoder.encode("nombre", "UTF-8") + "=" + URLEncoder.encode(nombre, "UTF-8")
                        + "&" + URLEncoder.encode("telefono", "UTF-8") + "=" + URLEncoder.encode(telefono, "UTF-8")
                        + "&" + URLEncoder.encode("correo", "UTF-8") + "=" + URLEncoder.encode(correo, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                resultado = stringBuilder.toString();
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

            } catch (MalformedURLException e){
                Log.d("MiAPP","SE HA UTILIZADO UNA URL DE FORMA INCORRECTA");
                resultado = "se ha producido un error";
            } catch (IOException e){
                Log.d("MiAPP","error posobles problemas de conexion a la red");
                resultado = "se ha producido un error, comprueba tu conexion a internet";
            }
            return resultado;
        }

        protected void onPostExecute(String resultado){
            Toast.makeText(context.get(), resultado, Toast.LENGTH_LONG).show();
        }
    }


    private void InsertarUsuario() {
        String usuario,correo,cp,password, tel;
        View focus = null;
        usuario = Usuario.getText().toString();
        correo = Correo.getText().toString();
        cp = Cp.getText().toString();
        password = Password.getText().toString();
        tel=Telefono.getText().toString(); //el numero de telefono es el indentificador para que no se duplique un dato
        //agregar consulta
      //  int numColumnaCursor=0;
      //   String nombre,telefono;
       //  nombre = Usuario.getText().toString();
        // telefono = Telefono.getText().toString();



//esto es nuevo una clase interna
      // new DescargarImagen(registro.this).execute(nombre,telefono,correo);

       Cursor cursor = db.consul_telefono(tel); // poner if para que no registre doble

      // char cadTexto= cursor;
      //  cursor= db2.rawQuery("SELECT * FROM "+AyudaBD.NOMBRE_TABLA+" WHERE "+AyudaBD.CAMPO_TEL+" LIKE '%"+ cadTexto +"%' ORDER BY "+AyudaBD.CAMPO_TEL,null);
    //  String  c = String.valueOf((db2.rawQuery("SELECT * FROM usuarios WHERE telefono=tel",null)));
        // se actualiza la pantalla
     //  String llaveCursor2 = cursor.getString(numColumnaCursor);
       // cursor.moveToFirst();
        //String c = String.valueOf(db2.rawQuery("SELECT * FROM contactos WHERE telefono=tel",null));
        //

        try {
            if (usuario.isEmpty() || correo.isEmpty() || cp.isEmpty() || password.isEmpty() || tel.isEmpty()) {
                focus = Usuario;
                Toast.makeText(this, "Los campos no pueden quedar vacios", Toast.LENGTH_SHORT).show();
                focus.requestFocus();
            } else if(cursor.moveToFirst() && cursor.getCount() > 0){
                Toast.makeText(this, "Numero de telefono invalido", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Revise los campos", Toast.LENGTH_SHORT).show();
                focus.requestFocus();

            }
            else{
                db.insertarusuario(usuario, correo, cp, password,tel);
                Toast.makeText(getApplicationContext(), "Cuenta creada", Toast.LENGTH_LONG).show();
                focus.requestFocus();

            }

        } catch (Exception e) {

        //    Toast.makeText(this, "Revise los campos", Toast.LENGTH_SHORT).show();


        }
    }

    //nuevo 05/09/2018
   //aqui iba el metodo esee ALV

  /*  private void InsertarUsuario() {
        String usuario,correo,cp,password, tel;
        View focus = null;
        usuario = Usuario.getText().toString();
        correo = Correo.getText().toString();
        cp = Cp.getText().toString();
        password = Password.getText().toString();
        tel=Telefono.getText().toString(); //el numero de telefono es el indentificador para que no se duplique un dato
        try {
            if (usuario.isEmpty() || correo.isEmpty() || cp.isEmpty() || password.isEmpty()) {
                focus = Usuario;
                Toast.makeText(this, "Los campos no pueden quedar vacios", Toast.LENGTH_SHORT).show();
                focus.requestFocus();
            } else {

                db.insertarusuario(usuario, correo, cp, password,"9613294730");
            }

        } catch (Exception e) {

            Toast.makeText(this, "Revise los campos", Toast.LENGTH_SHORT).show();


        }
    } */
}
