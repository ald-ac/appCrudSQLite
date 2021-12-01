package com.appsmoviles.crudsqlite.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appsmoviles.crudsqlite.R;
import com.appsmoviles.crudsqlite.objetos.Caballero;

import java.util.ArrayList;

//Establecer que extiende de un ArrayAdapter<Caballero>
public class MyAdaptadorListView extends ArrayAdapter<Caballero> {
    Context context;
    ArrayList<Caballero> Caballeros;

    public MyAdaptadorListView(Context c, ArrayList<Caballero> caballeros) {
        super(c, R.layout.row_list_view, R.id.textView1, caballeros);
        this.context = c;
        this.Caballeros = caballeros;
    }

    @NonNull
    @Override //Renderizando vista con row_list_view.xml
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.row_list_view, parent, false);
        TextView tv1 = row.findViewById(R.id.textView1);
        TextView tv2 = row.findViewById(R.id.textView2);
        TextView tv3 = row.findViewById(R.id.textView3);

        tv1.setText(Caballeros.get(position).getId());
        tv2.setText(Caballeros.get(position).getNombre());
        tv3.setText(Caballeros.get(position).getLugarNacimiento());
        return row;
    }
}
