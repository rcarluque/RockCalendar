package com.example.manue.rockcalendar;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by crafa on 06/03/2018.
 */

public class EventosListFragment extends ListFragment {
    private final String TAG = "EventosListFragment";

    private ListView listView;
    private ArrayList<Evento> items;
    private EventoFragmentAdapter eventoAdapter;
    private OnFragmentInteractionListener mListener;

    public EventosListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        MyLog.d(TAG, "onCreate...");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyLog.d(TAG, "onCreateView...");

        View rootView = inflater.inflate(R.layout.fragment_lista_eventos, container, false);
        listView = rootView.findViewById(android.R.id.list);
        return rootView;
    }

    /**
     * Método para cuando la actividad esté creada
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Tenemos que usar un diseño de elementos de lista diferente para dispositivos anteriores a Honeycomb
        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

        if(isReadStoragePermissionGranted()) {

            // -----!! Extraemos los Datos de la BBDD y del SharedPreferences !!-----
            AcontecimientoSQLiteHelper usdbh =
                    new AcontecimientoSQLiteHelper(getActivity(), Environment.getExternalStorageDirectory() + "/rockcalendar.db", null, 1);

            SQLiteDatabase db = usdbh.getReadableDatabase();

            SharedPreferences prefs =
                    getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

            String idAcontecimiento = prefs.getString("id", "no hay id");

            String[] args = new String[]{idAcontecimiento};
            // la sentencia SQL
            String sSQL = "SELECT id, nombre, inicio FROM evento WHERE id_acontecimiento=?";

            String id = null;
            String nombre = null;
            String inicio = null;

            items = new ArrayList<Evento>();

            Cursor c = db.rawQuery(sSQL, args);
            while (c.moveToNext()) {
                id = c.getString(c.getColumnIndex("id"));
                nombre = c.getString(c.getColumnIndex("nombre"));
                inicio = c.getString(c.getColumnIndex("inicio"));

                items.add(new Evento(id, nombre, inicio));
            }

            eventoAdapter = new EventoFragmentAdapter(getActivity(), layout, items);
            listView.setAdapter(eventoAdapter);
        }

    }

    @Override
    public void onStart(){
        MyLog.d(TAG, "onStart...");
        super.onStart();

        // Cuando se encuentre en un diseño de dos paneles, la vista pasará a modo de Lista
        if(getFragmentManager().findFragmentById(R.id.fragment_detalles) != null){
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        MyLog.d(TAG, "onListItemClick...");
        if(mListener != null){
            mListener.onFragmentInteraction(position, items.get(position));
        }
        // Hace que se quede iluminado el punto en el que hemos hecho click
        getListView().setItemChecked(position, true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " debe implementar OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int position, Evento item);
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
