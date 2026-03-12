package com.ejemplo.practica_1;

import static android.opengl.ETC1.getHeight;

import android.graphics.Canvas;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    MiVista vista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vista = new MiVista(this);
        setContentView(vista);
    }

    @Override
    protected void onResume() {
        super.onResume();
        vista.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        vista.pause();
    }
}

