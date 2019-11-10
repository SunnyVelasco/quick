package com.example.marip.quick;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
///////


import java.util.ArrayList;
import java.util.List;

public class AgregadosContactos extends AppCompatActivity {
    BaseDatos d;
    //////////////////////////////////

    private String llaveCursor;
   // private SQLiteDatabase d;
    private ListView lv1;
    private ArrayList<String> telefonos;
    private ArrayAdapter<String> adaptador1;
    // private ListAdapter adapter;
    private SQLiteDatabase db;
    private Cursor cursor;
    private SimpleCursorAdapter adapter;
    private AyudaBD helper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregados_contactos);

        db = new AyudaBD(this).getWritableDatabase();
               // (this);

        cursor= db.rawQuery("SELECT * FROM "+ AyudaBD.NOMBRE_TABLA + " ORDER BY _id", null);
        //asociamos el adaptador con el cursor.


      //cursor= db.consul_contacto2();

        //verRutas();

        //se instancia al list view
        adapter= new SimpleCursorAdapter(
                this, //Contexto
                R.layout.user_row, //layout para cada renglon
                cursor, //resultado de consulta
                new String[]{AyudaBD.CAMPO_ID, AyudaBD.CAMPO_NC, AyudaBD.CAMPO_TEL},
                new int[] {R.id.TITULO, R.id.AUTOR, R.id.ANO},0
        );

        lv1= (ListView) findViewById(R.id.lista);
        lv1.setAdapter(adapter);
        registerForContextMenu(lv1);
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_contexto, menu);
    }


    public boolean onContextItemSelected(MenuItem item) {
        int numColumnaCursor=0;
        //obtener informacion del renglon
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){

            case R.id.op_eliminar:
                //obtener la key del cursor
                cursor.moveToPosition(info.position);
                numColumnaCursor = cursor.getColumnIndex(AyudaBD.CAMPO_NC);
                llaveCursor = cursor.getString(numColumnaCursor);
                eliminar2(llaveCursor);
                break;
        }

        return super.onContextItemSelected(item);
    }


    private void eliminar2(final String lleveCursor){

        Cursor c1= db.rawQuery(
                "SELECT * FROM "+ AyudaBD.NOMBRE_TABLA +" WHERE " + AyudaBD.CAMPO_NC + "=?"
                , new  String[] {lleveCursor});
        c1.moveToFirst();

        final  String ID = c1.getString(c1.getColumnIndex(AyudaBD.CAMPO_ID));
        final String nombre = c1.getString(c1.getColumnIndex(AyudaBD.CAMPO_NC));
        String telefono = c1.getString(c1.getColumnIndex(AyudaBD.CAMPO_TEL));

//inflamos la vista plantilla
        LayoutInflater inflater =LayoutInflater.from(this);
        View vistaEliminar = inflater.inflate(R.layout.eliminar,null);


        final TextView campoid=vistaEliminar.findViewById(R.id.ID2);
        campoid.setText(ID);
        campoid.setEnabled(false);

        final TextView campoTitulo= vistaEliminar.findViewById(R.id.TITULO2);
        campoTitulo.setText("Nombre: "+nombre);
        campoTitulo.setEnabled(false); //se inabilita el campo

        final  TextView campoNombre=vistaEliminar.findViewById(R.id.AUTOR2);
        campoNombre.setText("Telefono: "+telefono);
        campoNombre.setEnabled(false);

        //dialogo para modificar
        new AlertDialog.Builder(this)
                .setTitle("Deseas Eliminar El Contacto")
                .setView(vistaEliminar)
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //eliminacion de registro
                        db.delete(AyudaBD.NOMBRE_TABLA, AyudaBD.CAMPO_ID + "= " + ID,null);

                        //recupera nuevamente los datos para desplejar
                        cursor=db.rawQuery("SELECT * FROM "+AyudaBD.NOMBRE_TABLA +
                                " ORDER BY _id",null);
                        adapter.changeCursor(cursor);

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "proceso cancelado", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();

    }
}
