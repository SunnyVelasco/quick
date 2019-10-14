package com.example.marip.quick;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class bienvenida extends AppCompatActivity {
    private static final String STRING_PREFERENCES = "Quick";
    private static final String PREFERENCE_ESTADO_BUTTON_SESION = "estado.button.sesion";
    Button Iniciar, registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);
        Iniciar=(Button)findViewById(R.id.Iniciar_sesión);
        registro=(Button)findViewById(R.id.Registrarse);


        Iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Crear= new Intent(bienvenida.this, MainActivity.class);
                startActivity(Crear);
               // finish(); //aqui*****
            }
        });


        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CrearC= new Intent(bienvenida.this, registro.class);
                startActivity(CrearC);
               // finish(); //aqui*****
            }
        });
    }


}
