package com.ejemplo.proyecto2;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent ejemplo = new Intent(Intent.ACTION_DIAL); // Abrir el teclado de numero de telefono
        ejemplo.setData(Uri.parse("tel:+34633767827")); // Establecer el numero de telefono
        startActivity(ejemplo);
    }
}