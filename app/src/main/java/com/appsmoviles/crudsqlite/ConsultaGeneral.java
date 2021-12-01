package com.appsmoviles.crudsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import com.appsmoviles.crudsqlite.adaptadores.MyAdaptadorListView;
import com.appsmoviles.crudsqlite.objetos.Caballero;
import com.appsmoviles.crudsqlite.sql.ConexionSQLiteHelper;
import com.appsmoviles.crudsqlite.sql.Utilidades;

import java.util.ArrayList;

public class ConsultaGeneral extends AppCompatActivity {
    ListView lvCaballeros;
    ArrayList<Caballero> caballeros = new ArrayList<Caballero>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_general);

        lvCaballeros = findViewById(R.id.listViewCaballeros);
        //Consultar en BD y llenar array local
        consultaCaballeros();

        //Adaptador y ListView
        MyAdaptadorListView adaptador = new MyAdaptadorListView(getApplicationContext(), caballeros);
        lvCaballeros.setAdapter(adaptador);
    }

    public void consultaCaballeros() {
        ConexionSQLiteHelper con = new ConexionSQLiteHelper(getApplicationContext(), "bd_caballeros", null, 1);
        SQLiteDatabase db = con.getWritableDatabase();

        String id, nombre, lugar;

        Cursor cursor = db. rawQuery("SELECT * FROM " + Utilidades.TABLA_CABALLEROS, null);
        //Llenar array de caballeros
        while(cursor.moveToNext()) {
            id = cursor.getString(0);
            nombre = cursor.getString(1);
            lugar = cursor.getString(2);
            caballeros.add(new Caballero(id, nombre, lugar));
        }
    }
}