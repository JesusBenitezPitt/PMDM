package com.example.actividad_8;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout rojo = findViewById(R.id.layout_rojo);
        LinearLayout azul = findViewById(R.id.layout_azul);
        LinearLayout verde = findViewById(R.id.layout_verde);
        LinearLayout amarillo = findViewById(R.id.layout_amarillo);

        rojo.setOnTouchListener(this);
        azul.setOnTouchListener(this);
        verde.setOnTouchListener(this);
        amarillo.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        String nombreLayout = "";

        int id = v.getId();
        if (id == R.id.layout_rojo) nombreLayout = "Rojo";
        else if (id == R.id.layout_azul) nombreLayout = "Azul";
        else if (id == R.id.layout_verde) nombreLayout = "Verde";
        else if (id == R.id.layout_amarillo) nombreLayout = "Amarillo";

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Toast.makeText(this, "Has pulsado el layout " + nombreLayout, Toast.LENGTH_SHORT).show();
                return true;

            case MotionEvent.ACTION_UP:
                Toast.makeText(this, "Has dejado de pulsar el layout " + nombreLayout, Toast.LENGTH_SHORT).show();
                return true;
        }

        return false;
    }
}