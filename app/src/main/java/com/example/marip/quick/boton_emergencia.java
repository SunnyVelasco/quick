package com.example.marip.quick;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class boton_emergencia extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    public Button EnviarMensaje;
    public EditText txtNum, txt_Men;
    public Cursor listaContactos;
    Thread hilo;
    BaseDatos db;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_boton_emergencia);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        db = new BaseDatos(boton_emergencia.this);
        EnviarMensaje = (Button) findViewById(R.id.btn_ayuda);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.Open, R.string.Close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_viewN);
     //   navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MandarMensajes();

            }
        });

    }
//el de volver atras
@Override public boolean onKeyDown(int keyCode, KeyEvent event) { if(keyCode==KeyEvent.KEYCODE_BACK) { return false; } return super.onKeyDown(keyCode, event); }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawmenu, menu);
        return true;
    }


    private void MandarMensajes() {
        try {
            listaContactos = db.consul_contacto();
            listaContactos.moveToFirst();
/*        hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<=listaContactos.getCount();i++){
                    EnviarMensaje(listaContactos.getString(listaContactos.getColumnIndex("telefono")));
                    listaContactos.moveToNext();
                }
            }
        });
        hilo.start();*/
            for (int i = 0; i <= listaContactos.getCount() - 1; i++) {
                EnviarMensaje(listaContactos.getString(listaContactos.getColumnIndex("telefono")));
                listaContactos.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    public void EnviarMensaje(String numTel) {
        //String numTel = txtNum.getText().toString();
        String direccion="https://maps.app.goo.gl/u1Hx4242QEyEtqCy9";


        try {
            int permissioncheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
            if (permissioncheck != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getApplicationContext(), "No se tiene permisos para enviar SMS", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 255);
            } else {
                Log.i("Mensaje", "Se tienen permisos para enviar SMS");
            }
            SmsManager sms= SmsManager.getDefault();

            sms.sendTextMessage(numTel, null, "Necesito ayuda. Ubicacion: "+direccion,null,null);
            Toast.makeText(getApplicationContext(),"Mensaje Enviado",Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Mensaje no enviado, checar permisos por favor",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.Agregar_servicio){
            Intent a = new Intent(boton_emergencia.this,contactos.class);
            startActivity(a);
           // finish(); //aqui*****
        }
        if (id == R.id.Contactos_guardados){
            Intent a = new Intent(boton_emergencia.this,AgregadosContactos.class);
            startActivity(a);
           // finish(); //aqui*****
        }

        if(id == R.id.Cerrar_sesion){
            MainActivity.CerrarSesionMenu(boton_emergencia.this,false);
            Intent i = new Intent(boton_emergencia.this, bienvenida.class);
            startActivity(i);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }


   // @SuppressWarnings("StatementWithEmptyBody")
  //  @Override
    /*
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Agregar_servicio) {
            Intent a = new Intent(boton_emergencia.this,contactos.class);
            startActivity(a);
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
*/

/*    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId()==R.id.Agregar_servicio){
            Intent a = new Intent(boton_emergencia.this,contactos.class);
            startActivity(a);
        }
        return false;
    }*/
}

