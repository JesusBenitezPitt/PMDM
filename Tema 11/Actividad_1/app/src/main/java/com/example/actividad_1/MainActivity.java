package com.example.actividad_1;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.TextView;

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

        TextView textView = findViewById(R.id.text);

        Display pantalla = getWindowManager().getDefaultDisplay();
        Point medida = new Point();
        pantalla.getSize(medida);
        int ancho = medida.x;
        int alto = medida.y;

        textView.setText("Ancho: " + ancho + ", Alto: " + alto);

    }
}