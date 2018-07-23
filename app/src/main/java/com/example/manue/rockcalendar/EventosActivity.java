package com.example.manue.rockcalendar;

/**
 * Created by crafa on 08/03/2018.
 */

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class EventosActivity extends AppCompatActivity implements EventosListFragment.OnFragmentInteractionListener {
    private final String TAG = "EventosActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyLog.d(TAG, "onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_eventos));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        } else {
            MyLog.d(TAG, "Error al cargar toolbar");
        }

        // Comprueba si la actividad está utilizando la versión de diseño
        // contenedor del FrameLayout. Si es así, debemos añadir el primer fragmento
        if (findViewById(R.id.unique_fragment) != null) {

            // Sin embargo, si estamos siendo restaurados de un estado anterior,
            // entonces no necesitamos hacer nada y debemos regresar o bien
            // podríamos terminar con fragmentos superpuestos.
            if (savedInstanceState != null) {
                return;
            }

            EventosListFragment listFrag = new EventosListFragment();

            // En caso de iniciarse esta actividad con instrucciones especiales de un Intent,
            // pasa los extras de la Intención al fragmento como argumentos
            listFrag.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.unique_fragment, listFrag).commit();
        }

        // Método para crear la flecha hacia atrás en el toolbar
    }

    /**
     * Este método está unido al de crear la flecha, aqui elegimos a que actividad queremos que se diriga.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onFragmentInteraction(int position, Evento item){
        MyLog.d(TAG, "onFragmentInteraction...");

        // Captura el fragmento de artículo del diseño de la actividad
        EventoDetallesFragment detallesFrag = (EventoDetallesFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_detalles);

        // Si el fragmento del artículo está disponible, estaremos en un diseño de dos paneles ...
        if (detallesFrag != null) {
            // Llama a un método en el Contenido para actualizar su contenido
            detallesFrag.updateView(position, item.getId());
        } else {
            // Crear fragmento y darle un argumento para el artículo seleccionado
            EventoDetallesFragment newDetallesFrag = new EventoDetallesFragment();
            Bundle args = new Bundle();
            args.putInt("position", position);
            args.putString("id", item.getId());
            newDetallesFrag.setArguments(args);

            // Reemplazamos lo que esté en la vista EventoDetallesFragment con este fragmento,
            // y agrega la transacción a la pila trasera para que el usuario pueda navegar de nuevo
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.unique_fragment, newDetallesFrag);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
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
