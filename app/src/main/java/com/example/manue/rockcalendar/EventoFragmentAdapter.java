package com.example.manue.rockcalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by crafa on 06/03/2018.
 */

public class EventoFragmentAdapter extends ArrayAdapter<Evento>{
    private final Context context;
    private final ArrayList<Evento> values;

    public EventoFragmentAdapter(Context context, int textViewResourceId, ArrayList<Evento> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_lista_eventos, parent, false);

        TextView tv_nombre = (TextView) rowView.findViewById(R.id.tv_nombre_evento);
        tv_nombre.setText(values.get(position).getNombre());

        TextView tv_inicio = (TextView) rowView.findViewById(R.id.tv_inicio_evento);
        tv_inicio.setText(values.get(position).getInicio());

        return rowView;
    }
}
