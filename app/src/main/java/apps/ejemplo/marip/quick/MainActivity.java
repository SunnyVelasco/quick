package apps.ejemplo.marip.quick;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import apps.ejemplo.marip.quick.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    BaseDatos db;
    Button Iniciar, CrearC;
    EditText Usuario_Login, Password_Login;
    RadioButton Rd_Sesion;
    private boolean EstaActivado;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private static final String STRING_PREFERENCES = "Quick";
    private static final String PREFERENCE_ESTADO_BUTTON_SESION = "estado.button.sesion";
    List<HashMap<String,String>> DatosUsuario= new ArrayList<>();//Lista Mapeada por 2 variables(Nombre de la variable, Dato)
    Cursor Contenedor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.txt_usuario).requestFocus();
        firebaseAuth = FirebaseAuth.getInstance();
        if(ObtenerEstado()){
            Intent Iniciar = new Intent(MainActivity.this, boton_emergencia.class);
            startActivity(Iniciar);
        }

        db= new BaseDatos(MainActivity.this);
        Iniciar=(Button)findViewById(R.id.btn_iniciarS);
       //CrearC=(Button)findViewById(R.id.btn_crearC);

        Usuario_Login=(EditText)findViewById(R.id.txt_usuario);
        Password_Login=(EditText)findViewById(R.id.txt_contra);

        Rd_Sesion= (RadioButton)findViewById(R.id.Rd_Sesion);
        EstaActivado=Rd_Sesion.isChecked(); //Desactivado

        Rd_Sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EstaActivado){
                    Rd_Sesion.setChecked(false);
                }
                EstaActivado = Rd_Sesion.isChecked();

            }
        });

        Iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
               // finish(); //aqui*****
            }
        });

     /*   CrearC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CrearC= new Intent(MainActivity.this, registro.class);
                startActivity(CrearC);
               // finish(); //aqui*****
            }
        }); */
        progressDialog = new ProgressDialog(this);
    }



    public void guardarEstadoSesion(){

        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES,MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCE_ESTADO_BUTTON_SESION, Rd_Sesion.isChecked()).apply();


    }



    public boolean ObtenerEstado(){
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES,MODE_PRIVATE);
        return preferences.getBoolean(PREFERENCE_ESTADO_BUTTON_SESION,false);
    }


    public static void CerrarSesionMenu(Context c, boolean b){

        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES,MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCE_ESTADO_BUTTON_SESION,b).apply();

    }


    private void Login() {

        String pass = Password_Login.getText().toString().trim();
        String correo = Usuario_Login.getText().toString().trim();

        //Verificar cajas de texto


        if (TextUtils.isEmpty(correo)) {

            Toast.makeText(this, "Se debe ingresar un correo", Toast.LENGTH_LONG).show();
            return;
        }


        if (TextUtils.isEmpty(pass)) {

            Toast.makeText(this, "Se debe ingresar una Contraseña", Toast.LENGTH_LONG).show();
            return;
        }


        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(correo, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    Intent intencion = new Intent(getApplication(), boton_emergencia.class);
                   // intencion.putExtra(boton_emergencia.us)
                    startActivity(intencion);

                    Toast.makeText(MainActivity.this, "Bienvenido", Toast.LENGTH_LONG).show();
                } else {

                    if (task.getException() instanceof FirebaseAuthUserCollisionException)
                    {
                        Toast.makeText(MainActivity.this, "ese usuario ya esta registrado", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "No se ha podido acceder", Toast.LENGTH_LONG).show();
                    }


                }

                progressDialog.dismiss();
            }
        });


    }


    private HashMap<String,String> datos(String id,String usuario,String correo, String cp, String password){
        HashMap<String,String> aux = new HashMap<>();
        aux.put("id",id);
        aux.put("usuario",usuario);
        aux.put("correo",correo);
        aux.put("cp",cp);
        aux.put("password",password);
        return aux;
    }


    }

/*
 private void Login() {
        guardarEstadoSesion();
        try {
            String user, pass;
            user = Usuario_Login.getText().toString();
            pass = Password_Login.getText().toString();
            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Los campos no pueden estar vacios", Toast.LENGTH_SHORT).show();
            } else {
               // boolean isCorrect = db.consul_usuario(Usuario_Login.getText().toString(), Password_Login.getText().toString());
                //boolean isCorrect2 = db.consul_usuario(Usuario_Login.getText().toString(), Password_Login.getText().toString());

                boolean isCorrect2 = db.consul_usuario2(Usuario_Login.getText().toString());
                boolean isCorrect3 = db.consul_usuario3(Password_Login.getText().toString());

                if (isCorrect2 && isCorrect3) {
                    Contenedor = db.consul_usuario(user);
                    Contenedor.moveToFirst();
                    DatosUsuario.add(datos(Contenedor.getString(Contenedor.getColumnIndex("_id")),
                            Contenedor.getString(Contenedor.getColumnIndex("usuario")),
                            Contenedor.getString(Contenedor.getColumnIndex("correo")),
                            Contenedor.getString(Contenedor.getColumnIndex("cp")),
                            Contenedor.getString(Contenedor.getColumnIndex("password"))));
                    Intent Iniciar = new Intent(MainActivity.this, boton_emergencia.class);
                    startActivity(Iniciar);
                    finish();
                }
                else if (isCorrect3) {
                        Toast.makeText(this, "Usuario incorrecto", Toast.LENGTH_SHORT).show();

                    }
                else
                {
                    Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }

            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Datos incorrectos", Toast.LENGTH_SHORT).show();

        }
    }
 */