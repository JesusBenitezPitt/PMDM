package com.ejemplo.examen;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    GameView vista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vista = new GameView(this);
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

