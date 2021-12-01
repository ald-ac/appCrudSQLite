package com.appsmoviles.crudsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.appsmoviles.crudsqlite.objetos.Caballero;
import com.appsmoviles.crudsqlite.sql.ConexionSQLiteHelper;
import com.appsmoviles.crudsqlite.sql.Utilidades;

public class MainActivity extends AppCompatActivity {
    Button btnInsertar, btnConsultar, btnActualizar, btnEliminar;
    EditText etId, etNombre, etLugarNacimiento;
    ImageView ivLimpiar, ivCamara;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //UI
        btnInsertar = findViewById(R.id.btn_insertar);
        btnConsultar = findViewById(R.id.btn_consulta);
        btnActualizar = findViewById(R.id.btn_actualizar);
        btnEliminar = findViewById(R.id.btnBorrar);

        etId = findViewById(R.id.et_id);
        etNombre = findViewById(R.id.et_nombre);
        etLugarNacimiento = findViewById(R.id.et_lugarnacimiento);

        ivLimpiar = findViewById(R.id.iv_limpiar);
        ivCamara = findViewById(R.id.iv_camara);

        //Accion insertar
        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = etId.getText().toString();
                String nombre = etNombre.getText().toString();
                String lugar = etLugarNacimiento.getText().toString();
                if(!id.equals("") && !nombre.equals("") && !lugar.equals("")) {
                    try {
                        agregarCaballero(id, nombre, lugar);
                    }catch (SQLiteConstraintException se) {
                        Toast.makeText(getApplicationContext(), "Error en base de datos: Posible llave duplicada", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Todos los campos deben ser llenados", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Accion consulta individual
        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = etId.getText().toString();
                if(!id.equals("")) {
                    if(!consultaIndividual(id)) {
                        Toast.makeText(getApplicationContext(), "Caballero inexistente", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Campo Id obligatorio", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Accion consulta general
        btnConsultar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                startActivity(new Intent(getApplicationContext(), ConsultaGeneral.class));
                return false;
            }
        });
    }

    //ACCIONES SQL
    //METODO AGREGAR CABALLERO EN DB
    public void agregarCaballero(String id, String nombre, String lugar) {
        ConexionSQLiteHelper con = new ConexionSQLiteHelper(getApplicationContext(), "bd_caballeros", null, 1);

        SQLiteDatabase db = con.getWritableDatabase();

        String agregarCaballero = "INSERT INTO " + Utilidades.TABLA_CABALLEROS
                + "(" + Utilidades.CAMPO_ID
                + ", " + Utilidades.CAMPO_NOMBRE
                + ", " + Utilidades.CAMPO_LUGAR
                + ") VALUES ( "
                + "'" + id + "',"
                + "'" + nombre + "',"
                + "'" + lugar
                + "')";
        db.execSQL(agregarCaballero);
        db.close();
    }

    //METODO CONSULTA CABALLERO INDIVIDUAL
    public boolean consultaIndividual(String id) {
        ConexionSQLiteHelper con = new ConexionSQLiteHelper(getApplicationContext(), "bd_caballeros", null, 1);
        SQLiteDatabase db = con.getWritableDatabase();

        Cursor cursor = db. rawQuery("SELECT * FROM " + Utilidades.TABLA_CABALLEROS + " WHERE id = '" + id + "'", null);
        if(cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                //Asignar datos encontrados a la UI
                etNombre.setText(cursor.getString(1));
                etLugarNacimiento.setText(cursor.getString(2));
            }
            return true;
        } else {
            return false;
        }
    }
}