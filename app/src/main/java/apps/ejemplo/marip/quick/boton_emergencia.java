package apps.ejemplo.marip.quick;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import apps.ejemplo.marip.quick.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Tag;

import java.util.HashMap;
import java.util.Map;

public class boton_emergencia extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    public Button EnviarMensaje,bt2;
    public EditText txtNum, txt_Men;
    public Cursor listaContactos;
    Thread hilo;
    BaseDatos db;
    Handler handler = new Handler();

    private final int TIEMPO = 60000;
    double dato1, dato2;
    String direccion,direccion2, coma=",";

    FirebaseAuth auth;
    GoogleApiClient client;
    Location request;
    static final int   REQUEST_LOCATION =1;
    public DatabaseReference usuariobd;


    private FusedLocationProviderClient fusedLocationClient;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Activando el client de Google API

        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();







        setContentView(R.layout.activity_boton_emergencia);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        db = new BaseDatos(boton_emergencia.this);
        EnviarMensaje = (Button) findViewById(R.id.btn_ayuda);
        bt2 = (Button) findViewById(R.id.button);
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


    public void stopHandler() {
        handler.removeMessages(0);
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
        //String numTel = txtNum.getText().toString()


        try {
            int permissioncheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
            if (permissioncheck != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getApplicationContext(), "No se tiene permisos para enviar SMS", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 255);
            } else {
                Log.i("Mensaje", "Se tienen permisos para enviar SMS");
            }
            SmsManager sms= SmsManager.getDefault();

            sms.sendTextMessage(numTel, null, "Necesito ayuda. Ubicacion: https://www.google.com.mx/maps/search/"+direccion+coma+direccion2,null,null);
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
            Intent a = new Intent(boton_emergencia.this, PasarContactos.class);
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


    //GEOLOCALIZACION METODO
    //PERMISOS DEL GPS






    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Aquí muestras confirmación explicativa al usuario
                // por si rechazó los permisos anteriormente
            } else {
                ActivityCompat.requestPermissions(
                        this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
            }
        } else {

            request = LocationServices.FusedLocationApi.getLastLocation(client);
            if (request != null) {
                dato1=request.getLatitude();
                dato2=request.getLongitude();
                direccion= Double.toString(dato1);
                direccion2= Double.toString(dato2);

            }

            else{
                Toast.makeText(this, "Ubicación no encontrada", Toast.LENGTH_LONG).show();
            }


        }
    }

    //PERMISOS DEL GPS
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if(grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                request = LocationServices.FusedLocationApi.getLastLocation(client);
                if (request != null) {
                    dato1=request.getLatitude();
                    dato2=request.getLongitude();
                    direccion= Double.toString(dato1);
                    direccion2= Double.toString(dato2);
                } else {
                    Toast.makeText(this, "Ubicación no encontrada", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Permisos no otorgados", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void getLastLocation() {
        if (isLocationPermissionGranted()) {
            request = LocationServices.FusedLocationApi.getLastLocation(client);
        } else {
            manageDeniedPermission();
        }
    }

    private boolean isLocationPermissionGranted() {
        int permission = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    private void manageDeniedPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Aquí muestras confirmación explicativa al usuario
            // por si rechazó los permisos anteriormente
        } else {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }
    }



    private void updateLocationUI() {
        request.getLatitude();
        request.getLongitude();
    }





    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Nueva ubicación: "," "+ location.getLatitude()+location.getLongitude());
        request = location;
        updateLocationUI();

    }


    //CICLOS DE CONEXION DE LA API CLIENT
    @Override
    protected void onStart() {
        super.onStart();
        client.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        client.disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (client.isConnected()) {
            //stopLocationUpdates();
        }
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

