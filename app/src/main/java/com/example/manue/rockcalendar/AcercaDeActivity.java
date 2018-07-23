package com.example.manue.rockcalendar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

public class AcercaDeActivity extends AppCompatActivity {

    String TAG = "AcercaDeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_acerca_de);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.acerca_de));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        } else {
            MyLog.d("SobreNosotros", "Error al cargar toolbar");
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

}