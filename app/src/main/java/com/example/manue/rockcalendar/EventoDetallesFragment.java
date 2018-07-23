package com.example.manue.rockcalendar;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by crafa on 06/03/2018.
 */

public class EventoDetallesFragment extends Fragment {
    private final String TAG = "EventosListFragment";

    int mCurrentPosition = -1;
    private LinearLayout layoutPrincipal;
    String id;

    public EventoDetallesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyLog.d(TAG, "onCreateView...");

        // Si la actividad se ha recreado (por ejemplo desde la rotación de la pantalla), se restaura
        // la selección de artículo anterior establecida por onSaveInstanceState ().
        // Esto es primordialmente necesario cuando está en el diseño de dos paneles.
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt("position");
            id = savedInstanceState.getString("id");
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detalles_evento, container, false);
    }

    @Override
    public void onStart() {
        MyLog.d(TAG, "onStart...");
        super.onStart();

        // Durante el inicio, comprobamos si hay argumentos pasados ​​al fragmento.
        // onStart es un buen lugar para hacer esto porque el diseño ya ha sido
        // aplicado al fragmento en este punto, para que podamos llamar con seguridad al método
        Bundle args = getArguments();
        if (args != null) {
            // Establecer artículo basado en el argumento pasado en la vista
            updateView(args.getInt("position"), args.getString("id"));
        } else if (mCurrentPosition != -1) {
            // Establece el artículo en función del estado de la instancia guardada definido durante onCreateView
            updateView(mCurrentPosition, id);
        }
    }

    public void updateView(int position, String id){
        MyLog.d(TAG, "updateView...");

        crearLayout(id);
    }

    protected void crearLayout(String idEvento){
        String nombre = null;
        String descripcion = null;
        String inicio = null;
        String fin = null;
        String direccion = null;
        String localidad = null;
        String cod_postal = null;
        String provincia = null;
        String latitud = null;
        String longitud = null;

        if(isReadStoragePermissionGranted()) {

            //Abrimos la base de datos en modo lectura
            // Recibe el contexto y la ruta de la base de datos
            AcontecimientoSQLiteHelper usdbh =
                    new AcontecimientoSQLiteHelper(getActivity(), Environment.getExternalStorageDirectory() + "/rockcalendar.db", null, 1);

            SQLiteDatabase db = usdbh.getReadableDatabase();

            // creamos un array de strings, en el cual introduciremos el campo que queremos recoger del where
            String[] args = new String[]{idEvento};
            String selectSQL = "SELECT * FROM evento WHERE id=?";

            // creamos un cursor con la ejecución del select en la base de datos
            Cursor c = db.rawQuery(selectSQL, args);
            if (c.moveToFirst()) {
                nombre = c.getString(c.getColumnIndex("nombre")); //recogemos los datos de la columna 'nombre' de la base de datos
                descripcion = c.getString(c.getColumnIndex("descripcion"));
                inicio = c.getString(c.getColumnIndex("inicio"));
                fin = c.getString(c.getColumnIndex("fin"));
                direccion = c.getString(c.getColumnIndex("direccion"));
                localidad = c.getString(c.getColumnIndex("localidad"));
                cod_postal = c.getString(c.getColumnIndex("cod_postal"));
                provincia = c.getString(c.getColumnIndex("provincia"));
                latitud = c.getString(c.getColumnIndex("latitud"));
                longitud = c.getString(c.getColumnIndex("longitud"));
            }

            layoutPrincipal = (LinearLayout) getActivity().findViewById(R.id.layout_detalles_eventos);

            layoutPrincipal.setOrientation(LinearLayout.VERTICAL);

            // Eliminamos todas las vistas del layout contenedor, para que cuando se vuelva a pulsar
            // en el evento no aparezcan dobles.
            layoutPrincipal.removeAllViewsInLayout();

            //Añadimos el TextView de nombre del acontecimiento. Y posteriormente hacemos lo mismo con todas las variables posteriores
            createTextView(R.drawable.ic_nombre, nombre, layoutPrincipal);
            createTextView(R.drawable.ic_descripcion, descripcion, layoutPrincipal);
            createTextView(R.drawable.ic_inicio, inicio, layoutPrincipal);
            createTextView(R.drawable.ic_fin, fin, layoutPrincipal);
            createTextView(R.drawable.localidad, direccion, layoutPrincipal);
            createTextView(R.drawable.localidad, localidad, layoutPrincipal);
            createTextView(R.drawable.ic_email, cod_postal, layoutPrincipal);
            createTextView(R.drawable.ic_localidad, provincia, layoutPrincipal);
            createTextView(R.drawable.lngylat, latitud, layoutPrincipal);
            createTextView(R.drawable.lngylat, longitud, layoutPrincipal);
        }
    }

    /**
     * Creamos un método que recibirá el valor del TextView, los params del layout y el tipo de layout.
     * @param texto
     * @param layoutPrincipal
     */
    protected void createTextView(int imgCod, String texto, LinearLayout layoutPrincipal) {
        if (!texto.isEmpty()) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            // creamos otro linear layout al cual le asignamos un estilo, después los parametros y la orientación
            LinearLayout layoutSecundario = new LinearLayout(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
            layoutSecundario.setLayoutParams(params);
            layoutSecundario.setOrientation(LinearLayout.HORIZONTAL);

            // creamos la imagen view, que será el icono
            ImageView iv = new ImageView(getActivity());
            // asignamos el codigo de imagen a la imagen (esto hará que encuentre la imagen y la muestre)
            iv.setImageResource(imgCod);

            // creamos el text view con el nombre que reciba
            TextView newTV = new TextView(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
            // le asignamos el valor que reciva al tv
            newTV.setText(texto.toString());
            // le asignamos los parametros al tv
            newTV.setLayoutParams(params);

            newTV.setGravity(Gravity.CENTER);

            layoutSecundario.addView(iv);
            layoutSecundario.addView(newTV);

            layoutPrincipal.addView(layoutSecundario);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        MyLog.d(TAG, "onSaveInstanceState...");
        super.onSaveInstanceState(outState);

        // Guarda la selección de artículos actual en caso de que necesitemos recrear el fragmento
        outState.putInt("position", mCurrentPosition);
        outState.putString("id", id);
    }

    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                MyLog.d(TAG,"Permission is granted1");
                return true;
            } else {

                MyLog.d(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            MyLog.d(TAG,"Permission is granted1");
            return true;
        }
    }
}