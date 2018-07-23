package com.example.manue.rockcalendar;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by crafa on 20/02/2018.
 */

public class RestAcontecimiento extends AsyncTask<String, String, String> {

    String TAG = "RestAcontecimientoSyncTask";

    private Context context;
    private String id;
    private boolean flag = true;
    private HttpURLConnection connectionUrl;
    private String mensaje;
    private ProgressBar pb;

    public RestAcontecimiento(Context context, String id, ProgressBar pb){
        super();
        this.context = context;
        this.id = id;
        this.pb = pb;
    }

    @Override
    protected void onPreExecute(){ pb.setVisibility(View.VISIBLE);}

    protected String doInBackground(String... args) {
        StringBuilder total = new StringBuilder();

        try {
            URL url = new URL(Constantes.REST_URL+"acontecimiento/"+this.id);
            connectionUrl = (HttpURLConnection) url.openConnection();

            InputStream input = new BufferedInputStream(connectionUrl.getInputStream());
            BufferedReader breader = new BufferedReader(new InputStreamReader(input));

            String linea;

            while((linea = breader.readLine()) != null){
                total.append(linea);
            }

            JSONObject jsontotal = new JSONObject(total.toString());
            if(jsontotal.has("acontecimiento")){

                if(checkStoragePermission()) {
                    AcontecimientoSQLiteHelper usdbh =
                            new AcontecimientoSQLiteHelper(context, Environment.getExternalStorageDirectory()+"/rockcalendar.db", null, 1);

                    SQLiteDatabase db = usdbh.getWritableDatabase();

                    if(db != null) {
                        JSONObject jsonAcont = new JSONObject(jsontotal.getString("acontecimiento"));

                        String idAcontecimiento = (jsonAcont.has("id") ? jsonAcont.getString("id") : "");
                        String nombreAcontecimiento = (jsonAcont.has("nombre") ? jsonAcont.getString("nombre") : "");
                        String organizadorAcontecimiento = (jsonAcont.has("organizador") ? jsonAcont.getString("organizador") : "");
                        String descripcionAcontecimiento = (jsonAcont.has("descripcion") ? jsonAcont.getString("descripcion") : "");
                        String tipoAcontecimiento = (jsonAcont.has("tipo") ? jsonAcont.getString("tipo") : "");
                        String inicioAcontecimiento = (jsonAcont.has("inicio") ? jsonAcont.getString("inicio") : "");
                        String finAcontecimiento = (jsonAcont.has("fin") ? jsonAcont.getString("fin") : "");
                        String direccionAcontecimiento = (jsonAcont.has("direccion") ? jsonAcont.getString("direccion") : "");
                        String localidadAcontecimiento = (jsonAcont.has("localidad") ? jsonAcont.getString("localidad") : "");
                        String cod_postaldAcontecimiento = (jsonAcont.has("cod_postal") ? jsonAcont.getString("cod_postal") : "");
                        String provinciaAcontecimiento = (jsonAcont.has("provincia") ? jsonAcont.getString("provincia") : "");
                        String longitudAcontecimiento = (jsonAcont.has("longitud") ? jsonAcont.getString("longitud") : "");
                        String latitudAcontecimiento = (jsonAcont.has("latitud") ? jsonAcont.getString("latitud") : "");
                        String telefonoAcontecimiento = (jsonAcont.has("telefono") ? jsonAcont.getString("telefono") : "");
                        String emailAcontecimiento = (jsonAcont.has("email") ? jsonAcont.getString("email") : "");
                        String webAcontecimiento = (jsonAcont.has("web") ? jsonAcont.getString("web") : "");
                        String facebookAcontecimiento = (jsonAcont.has("facebook") ? jsonAcont.getString("facebook") : "");
                        String twitterAcontecimiento = (jsonAcont.has("twitter") ? jsonAcont.getString("twitter") : "");
                        String instagramAcontecimiento = (jsonAcont.has("instagram") ? jsonAcont.getString("instagram") : "");

                        db.delete("acontecimiento", "id="+idAcontecimiento, null);

                        //Creamos el registro a insertar como objeto ContentValues
                        ContentValues nuevoAcontecimiento = new ContentValues();
                        nuevoAcontecimiento.put("id", idAcontecimiento);
                        nuevoAcontecimiento.put("nombre",nombreAcontecimiento);
                        nuevoAcontecimiento.put("organizador", organizadorAcontecimiento);
                        nuevoAcontecimiento.put("descripcion", descripcionAcontecimiento);
                        nuevoAcontecimiento.put("tipo", tipoAcontecimiento);
                        nuevoAcontecimiento.put("inicio",inicioAcontecimiento);
                        nuevoAcontecimiento.put("fin",finAcontecimiento);
                        nuevoAcontecimiento.put("direccion", direccionAcontecimiento);
                        nuevoAcontecimiento.put("localidad", localidadAcontecimiento);
                        nuevoAcontecimiento.put("cod_postal", cod_postaldAcontecimiento);
                        nuevoAcontecimiento.put("provincia", provinciaAcontecimiento);
                        nuevoAcontecimiento.put("longitud", longitudAcontecimiento);
                        nuevoAcontecimiento.put("latitud", latitudAcontecimiento);
                        nuevoAcontecimiento.put("telefono", telefonoAcontecimiento);
                        nuevoAcontecimiento.put("email", emailAcontecimiento);
                        nuevoAcontecimiento.put("web", webAcontecimiento);
                        nuevoAcontecimiento.put("facebook", facebookAcontecimiento);
                        nuevoAcontecimiento.put("twitter", twitterAcontecimiento);
                        nuevoAcontecimiento.put("instagram", instagramAcontecimiento);

                        //Insertamos el registro en la base de datos
                        db.insert("acontecimiento", null, nuevoAcontecimiento);

                        if(jsontotal.has("eventos")) {
                            JSONArray listaEventos =  new JSONArray(jsontotal.getString("eventos"));

                            db.delete("evento", "id_acontecimiento="+idAcontecimiento, null);

                            for(int i = 0; i < listaEventos.length(); i++){
                                JSONObject objetoEvento = (JSONObject) listaEventos.get(i);
                                String idEvento = (objetoEvento.has("id") ? objetoEvento.getString("id") : "");
                                String idAcontecimientoEvento = (objetoEvento.has("id_acontecimiento") ? objetoEvento.getString("id_acontecimiento") : "");
                                String nombreEvento = (objetoEvento.has("nombre") ? objetoEvento.getString("nombre") : "");
                                String descripcionEvento = (objetoEvento.has("descripcion") ? objetoEvento.getString("descripcion") : "");
                                String inicioEvento = (objetoEvento.has("inicio") ? objetoEvento.getString("inicio") : "");
                                String finEvento = (objetoEvento.has("fin") ? objetoEvento.getString("fin") : "");
                                String direccionEvento = (objetoEvento.has("direccion") ? objetoEvento.getString("direccion") : "");
                                String localidadEvento = (objetoEvento.has("localidad") ? objetoEvento.getString("localidad") : "");
                                String cod_postalEvento = (objetoEvento.has("cod_postal") ? objetoEvento.getString("cod_postal") : "");
                                String provinciaEvento = (objetoEvento.has("provincia") ? objetoEvento.getString("provincia") : "");
                                String longitudEvento = (objetoEvento.has("longitud") ? objetoEvento.getString("longitud") : "");
                                String latitudEvento = (objetoEvento.has("latitud") ? objetoEvento.getString("latitud") : "");

                                //Creamos el registro a insertar como objeto ContentValues
                                ContentValues nuevoEvento = new ContentValues();
                                nuevoEvento.put("id", idEvento);
                                nuevoEvento.put("id_acontecimiento", idAcontecimientoEvento);
                                nuevoEvento.put("nombre", nombreEvento);
                                nuevoEvento.put("descripcion", descripcionEvento);
                                nuevoEvento.put("inicio", inicioEvento);
                                nuevoEvento.put("fin", finEvento);
                                nuevoEvento.put("direccion", direccionEvento);
                                nuevoEvento.put("localidad", localidadEvento);
                                nuevoEvento.put("cod_postal", cod_postalEvento);
                                nuevoEvento.put("provincia", provinciaEvento);
                                nuevoEvento.put("longitud", longitudEvento);
                                nuevoEvento.put("latitud", latitudEvento);

                                db.insert("evento", null, nuevoEvento);
                            }
                        }

                        db.close();

                    }
                } else {
                    Toast.makeText(context, "Permiso denegado", Toast.LENGTH_LONG).show();
                }

            } else if(jsontotal.has("code")) {
                flag = false;
                mensaje = jsontotal.getString("message");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connectionUrl.disconnect();
        }

        return total.toString();
    }

    @Override
    protected void onPostExecute(String resultado) {
        MyLog.d("ONPOSTEXECUTE", resultado);

        //Volvemos a ocultar la barra de progreso
        pb.setVisibility(View.INVISIBLE);

        // Si se ha podido realizar creará un nuevo archivo
        if(flag) {
            // creamos el archivo sharedPreferences
            SharedPreferences prefs = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("id", id);
            editor.commit();

            ((BuscarAcontecimientosActivity)context).finish();
            Intent intent = new Intent(context, MostrarAcontecimientoActivity.class);
            context.startActivity(intent);
            // Si no se ha podido realizar mostrará un Totas con mensaje de error
        } else{
            Toast.makeText(context, mensaje, Toast.LENGTH_LONG);
        }
    }

    /**
     * Método para comprobar permiso de escritura en la BBDD
     * @return
     */
    public boolean checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                MyLog.d(TAG,"Permission is granted");
                return true;
            } else {
                MyLog.d(TAG,"Permission is revoked");
                Activity ac = (Activity)context;
                ActivityCompat.requestPermissions(ac, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            // Los permisos se conceden automaticamente en versiones inferiores de SDA a 23
            MyLog.d(TAG,"Permission is granted");
            return true;
        }
    }

}
