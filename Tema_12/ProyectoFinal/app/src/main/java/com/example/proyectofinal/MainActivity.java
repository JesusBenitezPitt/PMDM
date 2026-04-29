package com.example.proyectofinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectofinal.utils.ScoreManager;

// Pantalla principal: muestra el récord y el botón para iniciar partida.
public class MainActivity extends AppCompatActivity {

    private ScoreManager gestorPuntuacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gestorPuntuacion = new ScoreManager(this);

        TextView tvRecord = findViewById(R.id.tvHighScore);
        tvRecord.setText(String.valueOf(gestorPuntuacion.getBestScore()));

        Button btnJugar = findViewById(R.id.btnPlay);
        btnJugar.setOnClickListener(v -> iniciarPartida());
    }

    private void iniciarPartida() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Actualizar el récord al volver de una partida
        TextView tvRecord = findViewById(R.id.tvHighScore);
        if (tvRecord != null && gestorPuntuacion != null) {
            tvRecord.setText(String.valueOf(gestorPuntuacion.getBestScore()));
        }
    }
}
