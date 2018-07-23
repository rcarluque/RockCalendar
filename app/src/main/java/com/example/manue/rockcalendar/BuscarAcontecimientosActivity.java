package com.example.manue.rockcalendar;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BuscarAcontecimientosActivity extends AppCompatActivity {
    String TAG = "BuscarAcontecimientoActivity";
    Button btnBuscar;
    EditText editBuscar;
    private Context myContext;

    protected boolean comprobarLongitud(String cadena){
        if(cadena.length()<3){
            return false;
        }else{
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_acontecimientos);
        btnBuscar = (Button) findViewById(R.id.buttonBuscarAcontecimiento);
        editBuscar = (EditText) findViewById(R.id.editTextBuscarAcontecimiento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_buscar_acontecimientos));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        } else {
            MyLog.d("SobreNosotros", "Error al cargar toolbar");
        }



        myContext = this;

        btnBuscar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if (comprobarLongitud(editBuscar.getText().toString())){
                    // código con el cual ocultaremos el teclado al pulsar el botón
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(editBuscar.getWindowToken(), 0);

                    if (isNetDisponible()) {
                        // Permiso aceptado
                        Snackbar.make(view, getResources().getString(R.string.internet_permission_granted), Snackbar.LENGTH_LONG)
                                .show();
                        // new RestAcontecimientos(editBuscar.getText().toString(), myContext).execute();
                        new RestAcontecimientos(editBuscar.getText().toString(), myContext).execute();
                    } else {
                        // Permiso denegado
                        Snackbar.make(view, getResources().getString(R.string.internet_permission_denied), Snackbar.LENGTH_LONG)
                                .show();
                    }
                }else{
                    Snackbar.make(view, "Necesitas al menos 3 caracteres", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }


    private boolean isNetDisponible() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();

        return (actNetInfo != null && actNetInfo.isConnected());
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
}
