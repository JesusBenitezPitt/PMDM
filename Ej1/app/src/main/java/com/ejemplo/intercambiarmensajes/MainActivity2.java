package com.ejemplo.intercambiarmensajes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Log.d("Prueba", "MainActivity2 creado");
        Intent data = new Intent();
        String nombre = getIntent().getStringExtra("Nombre");
        Toast.makeText(this, "Nombre: " + nombre, Toast.LENGTH_SHORT).show();
        data.putExtra("Mensaje", "Hola " + nombre);
        setResult(RESULT_OK, data);
        finish();
    }

}