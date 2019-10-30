package com.example.marip.quick;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

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


public class registro extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    Button CrearC;
    EditText Usuario, Correo, Cp, Password, Telefono;
    BaseDatos db;
    private AyudaBD helper;
    private SQLiteDatabase x;
    private Cursor cursor;
    private String llaveCursor;
    private SQLiteDatabase db2;
    private ProgressDialog progressDialog;

    //Firebase

    private FirebaseAuth firebaseAuth;
    private GoogleApiClient googleApiClient;
    private SignInButton singInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //modif
/*
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this) // un error :C
                //.addApi(Auth.GOO)
                  .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        singInButton = (SignInButton) findViewById(R.id.botonG);
        singInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,777);
            }
        }); */

        //inicializamos el objeto firebaseAuth
        CrearC = (Button) findViewById(R.id.btn_cuenta);
        firebaseAuth = FirebaseAuth.getInstance();

        CrearC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });



        // (this);


        Usuario = (EditText) findViewById(R.id.txt_UsuarioC);
        Correo = (EditText) findViewById(R.id.txt_Correo2);
        Cp = (EditText) findViewById(R.id.txt_codP);
        Password = (EditText) findViewById(R.id.txt_pass);
        Telefono = (EditText) findViewById(R.id.txt_TelUser);
        findViewById(R.id.txt_UsuarioC).requestFocus();
        db = new BaseDatos(registro.this);
        db2 = new AyudaBD(this).getWritableDatabase();

        //final EditText texto_nombre = findViewById(R.id.txt_UsuarioC);
        //final EditText texto_telefono = findViewById(R.id.txt_TelUser);
        //final EditText texto_email = findViewById(R.id.txt_Correo2);


        progressDialog = new ProgressDialog(this);



    }


    private void registrarUsuario() {


        String usuario = Usuario.getText().toString().trim();
        String correo = Correo.getText().toString().trim();
        String cp = Cp.getText().toString().trim();
        String pass = Password.getText().toString().trim();
        String tel = Password.getText().toString().trim();

        //Verificar cajas de texto

        if (TextUtils.isEmpty(usuario)) {

            Toast.makeText(this, "Se debe ingresar un usuario", Toast.LENGTH_LONG).show();
            return;
        }


        if (TextUtils.isEmpty(correo)) {

            Toast.makeText(this, "Se debe ingresar un correo", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(cp)) {

            Toast.makeText(this, "Se debe ingresar un Codigo Postal", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(pass)) {

            Toast.makeText(this, "Se debe ingresar una Contraseña", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(tel)) {

            Toast.makeText(this, "Se debe ingresar un Telefono", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Realizando registro...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(correo, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(registro.this, "Se ha registrado el usuario con exito", Toast.LENGTH_LONG).show();
                } else {

                    if (task.getException() instanceof FirebaseAuthUserCollisionException)
                    {
                        Toast.makeText(registro.this, "ese usuario ya esta registrado", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(registro.this, "No se ha podido registrar el usuario", Toast.LENGTH_LONG).show();
                    }


                }

                progressDialog.dismiss();
            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode ==777){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSingnInResult(result);
        }
    }

    private void handleSingnInResult(GoogleSignInResult result) {
        if (result.isSuccess()){
            goMainScreen();
        }
        else {
            Toast.makeText(this,"no se puede iniciar secion" , Toast.LENGTH_SHORT).show();
        }
    }

    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

