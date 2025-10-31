package com.ejemplo.ciclodevida;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Etiqueta para los mensajes en el log
    private static final String TAG = "CicloVidaActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: La actividad ha sido creada");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: La actividad ha comenzado");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: La actividad está en primer plano y interactuando con el usuario");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: La actividad está en pausa, el usuario no puede interactuar con ella");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: La actividad ha dejado de estar visible");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: La actividad está siendo reiniciada, el usuario ha regresado a ella");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: La actividad está siendo destruida");
    }
}
