package com.example.manue.rockcalendar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListadoAcontecimientosActivity extends AppCompatActivity {

    String TAG = "ListadoAcontecimientosActivity";
    private ArrayList<Acontecimiento> items;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_acontecimientos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListadoAcontecimientosActivity.this,
                        BuscarAcontecimientosActivity.class));
            }
        });

        listaAcontecimientos();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.acerca_de:
                startActivity(new Intent(context, AcercaDeActivity.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(context, ConfiguracionActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        MyLog.d(TAG, "Ejecutando OnStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyLog.d(TAG, "Ejecutando OnResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        MyLog.d(TAG, "Ejecutando OnRestart");

        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onPause() {
        MyLog.d(TAG, "On pause...");
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyLog.d(TAG, "Ejecutando OnStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyLog.d(TAG, "Ejecutando OnDestroy");
    }

    private void listaAcontecimientos() {
        // Creamos las variables necesarias de los acontecimientos
        items = new ArrayList<>();
        int id = -1;
        String nombre = null;
        String inicio = null;

        if(isReadStoragePermissionGranted()) {
            //Abrimos la base de datos en modo lectura
            // Recibe el contexto y la ruta de la base de datos
            AcontecimientoSQLiteHelper usdbh =
                    new AcontecimientoSQLiteHelper(context, Environment.getExternalStorageDirectory()+"/rockcalendar.db", null, 1);

            // creamos la variable de la base de datos
            SQLiteDatabase db = usdbh.getReadableDatabase();

            // la sentencia SQL
            String sSQL = "SELECT id, nombre, inicio FROM acontecimiento ORDER BY inicio ASC";
            Cursor c = db.rawQuery(sSQL, null);

            // Buscamos los elementos necesarios para trabajar con la vista
            LinearLayout layout = (LinearLayout) findViewById(R.id.lista_acontecimientos_layout);
            TextView tvNoAcont = (TextView) findViewById(R.id.textViewAcontecimientos);
            final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerListaAcontecimientos);

            if(c.getCount() > 0) {
                layout.removeView(tvNoAcont);

                // Recorremos todos los resultados de la BBDDD
                while (c.moveToNext()) {
                    id = c.getInt(c.getColumnIndex("id"));
                    nombre = c.getString(c.getColumnIndex("nombre"));
                    inicio = c.getString(c.getColumnIndex("inicio"));

                    // Convertimos a fecha y la formateamos.
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmm");
                    Date fechaInicio = null;
                    try {
                        fechaInicio = dateFormat.parse(inicio);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    SimpleDateFormat formatPrint = new SimpleDateFormat("d/M/y hh:mm");
                    String inicioFormat = formatPrint.format(fechaInicio);
                    // fin formateo.

                    Acontecimiento acon = new Acontecimiento(String.valueOf(id), nombre, inicioFormat);
                    items.add(acon);
                }

                // Creamos el adaptador
                AcontecimientoAdapter adapter = new AcontecimientoAdapter(items);

                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = recyclerView.getChildAdapterPosition(v);
                        SharedPreferences prefs = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("id", items.get(position).getId());
                        editor.commit();

                        Intent intent = new Intent(context, MostrarAcontecimientoActivity.class);
                        context.startActivity(intent);
                    }
                });

                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            }
        }

    }

    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                MyLog.d(TAG,"Permission is granted1");
                return true;
            } else {
                MyLog.d(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            MyLog.d(TAG,"Permission is granted1");
            return true;
        }
    }
}
