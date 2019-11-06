package com.example.marip.quick;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PasarContactos extends AppCompatActivity {

    Button btnManual, btnAgenda;
    static final int PICK_CONTACT_RESQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasar_contactos);

        btnManual = (Button) findViewById(R.id.btnManual);


      /*  btnManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Contactos = new Intent(PasarContactos.this, contactos.class);
                startActivity(Contactos);
            }
        });

        btnAgenda = (Button) findViewById(R.id.botonAgenda);
        btnAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //seleccionarContacto();
                // finish(); //aqui*****

            }
        }); */

    }
}
