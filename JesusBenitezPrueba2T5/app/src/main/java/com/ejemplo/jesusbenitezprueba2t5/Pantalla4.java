package com.ejemplo.jesusbenitezprueba2t5;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Pantalla4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla4);

        String[] opciones = {
                "APPLE PIE",
                "BANANA BREAD",
                "CUPCAKE",
                "DONUT",
                "ECLAIR",
                "FROYO",
                "GINGERBREAD",
                "HONEYCOMB",
                "ICE CREAM SANDWICH",
                "JELLY BEAN",
                "KITKAT",
                "LOLLIPOP",
                "MARSHMALLOW",
                "NOUGAT",
                "OREO",
                "PIE",
                "ANDROID 10"
        };

        AutoCompleteTextView autocompletado = (AutoCompleteTextView) findViewById(R.id.autocompletar);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, opciones);

        autocompletado.setAdapter(adaptador);
        autocompletado.setThreshold(1);

    }

}