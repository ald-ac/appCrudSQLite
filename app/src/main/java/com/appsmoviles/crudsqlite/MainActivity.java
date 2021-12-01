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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
        //Desactivar botones
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);

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
                if (!id.equals("") && !nombre.equals("") && !lugar.equals("")) {
                    try {
                        agregarCaballero(id, nombre, lugar);
                    } catch (SQLiteConstraintException se) {
                        Toast.makeText(getApplicationContext(), "Error en base de datos: Posible llave duplicada", Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(getApplicationContext(), "Insercion hecha", Toast.LENGTH_LONG).show();
                    limpiarCampos();
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
                if (!id.equals("")) {
                    if (!consultaIndividual(id)) {
                        Toast.makeText(getApplicationContext(), "Caballero inexistente", Toast.LENGTH_SHORT).show();
                    } else { //Si existe caballero
                        //Habilitar botones
                        btnActualizar.setEnabled(true);
                        btnEliminar.setEnabled(true);
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

        //Accion actualizar
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = etId.getText().toString();
                String nombre = etNombre.getText().toString();
                String lugar = etLugarNacimiento.getText().toString();
                if (!id.equals("") && !nombre.equals("") && !lugar.equals("")) {
                    try {
                        actualizarCaballero(id, nombre, lugar);
                    } catch (SQLiteConstraintException se) {
                        Toast.makeText(getApplicationContext(), "Error durante actualizacion", Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(getApplicationContext(), "Actualizacion hecha", Toast.LENGTH_LONG).show();
                    limpiarCampos();
                    //Desactivar botones despues de actualizar
                    btnActualizar.setEnabled(false);
                    btnEliminar.setEnabled(false);
                } else {
                    Toast.makeText(getApplicationContext(), "Todos los campos deben ser llenados", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Accion eliminar
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = etId.getText().toString();
                if (!id.equals("")) {
                    try {
                        eliminarCaballero(id);
                    } catch (SQLiteConstraintException se) {
                        Toast.makeText(getApplicationContext(), "Error durante eliminacion", Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(getApplicationContext(), "Eliminacion hecha", Toast.LENGTH_LONG).show();
                    limpiarCampos();
                    //Desactivar botones despues de eliminar
                    btnActualizar.setEnabled(false);
                    btnEliminar.setEnabled(false);
                } else {
                    Toast.makeText(getApplicationContext(), "Campo Id obligatorio", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Accion limpiar campos
        ivLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpiarCampos();
            }
        });

        //Accion escanear Id para consulta
        ivCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Se instancia un objeto de la clase IntentIntegrator
                IntentIntegrator scanIntegrator = new IntentIntegrator(MainActivity.this);
                //Se procede con el proceso de scaneo
                scanIntegrator.initiateScan();
            }
        });
    }

    //Resultado de Escaneo
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //Se obtiene el resultado del proceso de scaneo y se parsea
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            //Quiere decir que se obtuvo resultado por lo tanto:
            //Extrae contenido del codigo
            String id = scanningResult.getContents();
            if (!consultaIndividual(id)) { //Consultar por id escaneado
                Toast.makeText(getApplicationContext(), "Caballero inexistente", Toast.LENGTH_SHORT).show();
            } else { //Si existe caballero
                //Habilitar botones
                btnActualizar.setEnabled(true);
                btnEliminar.setEnabled(true);
            }
            //Desplegamos en pantalla el nombre del formato del código de barra scaneado
            //String scanFormat = scanningResult.getFormatName();
            //Toast.makeText(getApplicationContext(),"Formato: " + scanFormat, Toast.LENGTH_SHORT).show();
        } else {
            //Quiere decir que NO se obtuvo resultado
            Toast.makeText(getApplicationContext(),"No se ha recibido datos del scaneo!", Toast.LENGTH_SHORT).show();
        }
    }

    //LIMPIAR EDITTEXT
    public void limpiarCampos() {
        etNombre.setText("");
        etId.setText("");
        etLugarNacimiento.setText("");
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
                etId.setText(cursor.getString(0));
                etNombre.setText(cursor.getString(1));
                etLugarNacimiento.setText(cursor.getString(2));
            }
            return true;
        } else {
            return false;
        }
    }

    //METODO ACTUALIZAR CABALLERO
    public void actualizarCaballero(String id, String nombre, String lugar) {
        ConexionSQLiteHelper con = new ConexionSQLiteHelper(getApplicationContext(), "bd_caballeros", null, 1);

        SQLiteDatabase db = con.getWritableDatabase();

        String actualizarCaballero = "UPDATE " + Utilidades.TABLA_CABALLEROS
                + " SET " + Utilidades.CAMPO_NOMBRE + "='" + nombre + "'"
                + ", " + Utilidades.CAMPO_LUGAR + "='" + lugar + "'"
                + " WHERE " + Utilidades.CAMPO_ID + "= '" + id + "'";
        db.execSQL(actualizarCaballero);
        db.close();
    }

    //METODO ELIMINAR CABALLERO
    public void eliminarCaballero(String id) {
        ConexionSQLiteHelper con = new ConexionSQLiteHelper(getApplicationContext(), "bd_caballeros", null, 1);

        SQLiteDatabase db = con.getWritableDatabase();

        String eliminarCaballero = "DELETE FROM " + Utilidades.TABLA_CABALLEROS
                + " WHERE " + Utilidades.CAMPO_ID
                + "= '" + id+"'";
        db.execSQL(eliminarCaballero);
        db.close();
    }
}