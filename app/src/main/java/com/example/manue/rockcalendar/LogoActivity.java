package com.example.manue.rockcalendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import java.util.Locale;

public class LogoActivity extends AppCompatActivity {

    private String TAG = "LogoActivity";
    protected int _splashTime = 2000;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        // Buscamos las preferencias de Configuracion acitivity
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // Comprobamos el lenguage seleccionado
        String cod_lenguaje = prefs.getString("pref_list_idioma", "");
        Resources res = getResources();
        // Cambiamos el lenguaje
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(cod_lenguaje);
        res.updateConfiguration(conf, dm);

        int secondsDelayed = 2;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(LogoActivity.this,
                        ListadoAcontecimientosActivity.class));
                finish();

                if(prefs.getBoolean("pref_switch_acontecimiento", false)) {
                    startActivity(new Intent(LogoActivity.this,
                            MostrarAcontecimientoActivity.class));
                    finish();
                }

            }
        }, secondsDelayed * 1000);
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
}
