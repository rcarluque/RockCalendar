package com.example.manue.rockcalendar;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MostrarAcontecimientoActivity extends AppCompatActivity {
    private final String TAG = "MostrarAcontecimientosActivity";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_acontecimiento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;

        creaAcontecimiento();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mostrar_acontecimiento, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_borrar_acontecimiento:
                borrarAcontecimiento();
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

    public void creaAcontecimiento() {
        SharedPreferences pref = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String idAcontecimiento = pref.getString("id", "no existe id");
        String nombre = null;
        String organizador = null;
        String descripcion = null;
        String tipo = null;
        String inicio = null;
        String fin = null;
        String direccion = null;
        String localidad = null;
        String cod_postal = null;
        String provincia = null;
        String latitud = null;
        String longitud = null;
        String telefono = null;
        String email = null;
        String web = null;
        String facebook = null;
        String twitter = null;
        String instagram = null;

        if(idAcontecimiento.equals("no existe id")) {
            onBackPressed();
        } else {
            // creamos un array de strings, en el cual introduciremos el campo que queremos recoger del where
            String[] args = new String[]{idAcontecimiento};
            // la sentencia SQL
            String sSQL = "SELECT * FROM acontecimiento WHERE id=?";

            // Comprobamos los permisos
            if(isReadStoragePermissionGranted()) {
                // Abrimos la BD
                AcontecimientoSQLiteHelper usdbh =
                        new AcontecimientoSQLiteHelper(context, Environment.getExternalStorageDirectory()+"/rockcalendar.db", null, 1);
                SQLiteDatabase db = usdbh.getReadableDatabase();
                // Creamos el cursor para recorrer la BD
                Cursor c = db.rawQuery(sSQL, args);

                if(c.getCount() == 0){
                    onBackPressed();
                } else {
                    if(c.moveToFirst()){
                        nombre = c.getString(c.getColumnIndex("nombre")); //recogemos los datos de la columna 'nombre' de la base de datos
                        organizador = c.getString(c.getColumnIndex("organizador"));
                        descripcion = c.getString(c.getColumnIndex("descripcion"));
                        tipo = c.getString(c.getColumnIndex("tipo"));
                        inicio = c.getString(c.getColumnIndex("inicio"));
                        fin = c.getString(c.getColumnIndex("fin"));
                        direccion = c.getString(c.getColumnIndex("direccion"));
                        localidad = c.getString(c.getColumnIndex("localidad"));
                        cod_postal = c.getString(c.getColumnIndex("cod_postal"));
                        provincia = c.getString(c.getColumnIndex("provincia"));
                        latitud = c.getString(c.getColumnIndex("latitud"));
                        longitud = c.getString(c.getColumnIndex("longitud"));
                        telefono = c.getString(c.getColumnIndex("telefono"));
                        email = c.getString(c.getColumnIndex("email"));
                        web = c.getString(c.getColumnIndex("web"));
                        facebook = c.getString(c.getColumnIndex("facebook"));
                        twitter = c.getString(c.getColumnIndex("twitter"));
                        instagram = c.getString(c.getColumnIndex("instagram"));
                    }

                    LinearLayout layoutPadre = (LinearLayout) findViewById(R.id.mostrar_acontecimiento_layout);

                    layoutPadre.scrollBy(0, 5);

                    try {
                        // creamos el formatero de como lo recoge en la base de datos
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmm");
                        Date fechaInicio = dateFormat.parse(inicio);
                        Date fechaFin = dateFormat.parse(fin);

                        // creamos el formato en el que lo va a mostrar
                        SimpleDateFormat formatPrint = new SimpleDateFormat("d/M/y hh:mm");
                        String inicioFormat = formatPrint.format(fechaInicio);
                        String finFormat = formatPrint.format(fechaFin);

                        // Creamos las vistas.
                        mostrarDatos(layoutPadre, nombre, R.drawable.ic_nombre);
                        mostrarDatos(layoutPadre, organizador, R.drawable.organizador);
                        mostrarDatos(layoutPadre, descripcion, R.drawable.ic_descripcion);
                        mostrarDatos(layoutPadre, tipo, R.drawable.ic_tipo);
                        mostrarDatos(layoutPadre, inicioFormat, R.drawable.ic_inicio);
                        mostrarDatos(layoutPadre, finFormat, R.drawable.ic_fin);
                        mostrarDatos(layoutPadre, direccion, R.drawable.localidad);
                        mostrarDatos(layoutPadre, localidad, R.drawable.ic_localidad);
                        mostrarDatos(layoutPadre, provincia, R.drawable.ic_localidad);
                        mostrarDatos(layoutPadre, cod_postal, R.drawable.ic_localidad);
                        mostrarDatos(layoutPadre, latitud, R.drawable.lngylat);
                        mostrarDatos(layoutPadre, longitud, R.drawable.lngylat);
                        mostrarDatos(layoutPadre, web, R.drawable.ic_web);
                        mostrarDatos(layoutPadre, facebook, R.drawable.ic_facebook);
                        mostrarDatos(layoutPadre, twitter, R.drawable.ic_twitter);
                        mostrarDatos(layoutPadre, instagram, R.drawable.ic_instagram);

                        String [] datos = new String[2];
                        datos[0] = nombre;
                        datos[1] = organizador;

                        botonLlamada(layoutPadre, R.drawable.ic_telf, telefono);
                        botonEmail(layoutPadre, R.drawable.ic_email, email, nombre, datos);

                        creaBotonEventos(layoutPadre, idAcontecimiento);

                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }

                }

            } // Fin de permisos

        }

    }

    /**
     * Creamos un método que recibirá el nombre del TV, el valor del TV, los params del alyout y el tipo de layout.
     *
     * @param layout
     * @param texto
     * @param imgCod
     */
    protected void mostrarDatos(LinearLayout layout, String texto, int imgCod) {
        if (!texto.isEmpty()) {
            // Obtenemos los params del layout
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            // creamos otro linear layout al cual le asignamos un estilo, después los parametros y la orientación
            LinearLayout layoutHijo = new LinearLayout(new ContextThemeWrapper(this, R.style.AppTheme));
            layoutHijo.setLayoutParams(params);
            layoutHijo.setOrientation(LinearLayout.HORIZONTAL);

            // creamos la imagen view, que será el icono
            ImageView iv = new ImageView(this);
            // asignamos el codigo de imagen a la imagen (esto hará que encuentre la imagen y la muestre)
            iv.setImageResource(imgCod);

            // Creamos un text view con sus parametros.
            TextView tv = new TextView(new ContextThemeWrapper(this, R.style.AppTheme));
            tv.setText(texto);
            tv.setLayoutParams(params);
            tv.setGravity(Gravity.CENTER);

            layoutHijo.addView(iv);
            layoutHijo.addView(tv);

            layout.addView(layoutHijo);
        }
    }

    private void botonLlamada(LinearLayout layout, int imgCod, final String texto){
        if(!texto.isEmpty()) {
            Button button = new Button(this);
            button.setText(texto);
            Drawable icon = this.getResources().getDrawable(imgCod);
            button.setCompoundDrawablesWithIntrinsicBounds( icon, null, null, null );

            layout.addView(button);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", texto, null));
                    startActivity(intent);
                }
            });
        }
    }

    private void botonEmail(LinearLayout layout, int imgCod, final String texto, final String nombre, final String[] body){
        if(!texto.isEmpty()) {
            Button button = new Button(this);
            button.setText(texto);
            Drawable icon = this.getResources().getDrawable(imgCod);
            button.setCompoundDrawablesWithIntrinsicBounds( icon, null, null, null );

            layout.addView(button);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", texto, null));

                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, nombre);
                    emailIntent.putExtra(Intent.EXTRA_TEXT, body);
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));

                }
            });
        }
    }

    private void creaBotonEventos(LinearLayout layout, String id){
        AcontecimientoSQLiteHelper usdbh =
                new AcontecimientoSQLiteHelper(context, Environment.getExternalStorageDirectory()+"/rockcalendar.db", null, 1);

        // creamos la variable de la base de datos
        SQLiteDatabase db = usdbh.getReadableDatabase();

        // creamos un array de strings, en el cual introduciremos el campo que queremos recoger del where
        String[] args = new String[]{id};
        String sSQL = "SELECT nombre FROM evento WHERE id_acontecimiento=?";
        Cursor c = db.rawQuery(sSQL, args);

        if (c.getCount() > 0) {
            Button btEventos = new Button(this);
            btEventos.setText(R.string.ver_eventos);
            layout.addView(btEventos);

            btEventos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EventosActivity.class);
                    context.startActivity(intent);
                }
            });
        }

    }

    private void borrarAcontecimiento() {
        new AlertDialog.Builder(context)
                .setTitle(R.string.title_borra_acontecimiento)
                .setMessage(R.string.body_borra_acontecimiento)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // en caso de pulsar si borramos el acontecimiento
                        if(isWriteStoragePermissionGranted()) {
                            SharedPreferences pref = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                            String idAcontecimiento = pref.getString("id", "no existe id");

                            AcontecimientoSQLiteHelper usdbh =
                                    new AcontecimientoSQLiteHelper(context, Environment.getExternalStorageDirectory()+"/rockcalendar.db", null, 1);

                            SQLiteDatabase db = usdbh.getWritableDatabase();

                            db.delete("acontecimiento", "id="+idAcontecimiento, null);
                            db.delete("evento", "id_acontecimiento="+idAcontecimiento, null);
                        }
                    }
                })
                .setNegativeButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // en caso de pulsar no
                        System.out.println("--->> No");
                    }
                })
                .show();
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                MyLog.d(TAG,"Permission is granted1");
                return true;
            } else {

                MyLog.d(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            MyLog.d(TAG,"Permission is granted1");
            return true;
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
