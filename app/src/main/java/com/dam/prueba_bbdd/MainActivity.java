
package com.dam.prueba_bbdd;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText codigoEt, descripcionEt, precioEt;

    AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(
            this, "Base de datos de Ren", null, 1);

    String codigo, precio, descripcion;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        codigoEt = findViewById(R.id.txt_codigo);
        descripcionEt = findViewById(R.id.txt_descripcion);
        precioEt = findViewById(R.id.txt_precio);
    }

    public void registrar(View view){

        // Creamos la base de datos
        SQLiteDatabase baseDeDatos = adminSQLiteOpenHelper.getWritableDatabase();

        codigo = codigoEt.getText().toString();
        descripcion = descripcionEt.getText().toString();
        precio = precioEt.getText().toString();

        if (!codigo.isEmpty() && !descripcion.isEmpty() && !precio.isEmpty()) {

            ContentValues registro = new ContentValues();
            registro.put("codigo", codigo);
            registro.put("descripcion", descripcion);
            registro.put("precio", precio);

            baseDeDatos.insert("articulos", null, registro);

            //Volver los campos a su estado principal (vacio)
            codigoEt.setText("");
            descripcionEt.setText("");
            precioEt.setText("");

            Toast.makeText(this, "Producto registrado con éxito",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Ningún campo puede estar vacío", Toast.LENGTH_SHORT).show();
        }
        baseDeDatos.close();
    }

    public void Buscar(View view){

        SQLiteDatabase sqLiteDatabase = adminSQLiteOpenHelper.getWritableDatabase();

        codigo = codigoEt.getText().toString();

        if (!codigo.isEmpty()){
            Cursor cursor = sqLiteDatabase.rawQuery(
                    "select descripcion, precio from articulos where codigo =" + codigo, null
            );

            if (cursor.moveToFirst()) {
                descripcionEt.setText(cursor.getString(0));
                precioEt.setText(cursor.getString(1));

            } else {
                Toast.makeText(this, "No existe el articulo", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        } else {
            Toast.makeText(this, "Debe escribir un código", Toast.LENGTH_SHORT).show();
        }

        sqLiteDatabase.close();
    }

    public void eliminar(View view){

        SQLiteDatabase sqLiteDatabase = adminSQLiteOpenHelper.getWritableDatabase();

        codigo = codigoEt.getText().toString();

        if (!codigo.isEmpty()){
            int cantidad = sqLiteDatabase.delete("articulos", "codigo = " + codigo, null);

            //Volver los campos a su estado principal (vacio)
            codigoEt.setText("");
            descripcionEt.setText("");
            precioEt.setText("");

            if (cantidad == 1){
                Toast.makeText(this, "Eliminado con éxito", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Articulo no existe", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Introduzca el código del artículo", Toast.LENGTH_SHORT).show();
        }

        sqLiteDatabase.close();
    }

    public void modificar(View view){

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null,1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();

        codigo = codigoEt.getText().toString();
        descripcion = descripcionEt.getText().toString();
        precio = precioEt.getText().toString();

        if(!codigo.isEmpty() && !descripcion.isEmpty() && !precio.isEmpty()){
            ContentValues registro = new ContentValues();
            registro.put("codigo", codigo);
            registro.put("descripcion", descripcion);
            registro.put("precio", precio);

            int cantidad = baseDeDatos.update("articulos", registro, "codigo = " + codigo, null);

            if(cantidad == 1){
                Toast.makeText(this, "Articulo modificado con éxito", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Articulo no existe", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Ningún campo puede estar vacío", Toast.LENGTH_SHORT).show();
        }
        baseDeDatos.close();

    }

}