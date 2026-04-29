package com.example.proyectofinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectofinal.utils.ScoreManager;

// Pantalla de fin de partida: muestra puntuación, récord y nivel alcanzado.
public class GameOverActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE = "extra_score";
    public static final String EXTRA_LEVEL = "extra_level";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        int puntuacion = getIntent().getIntExtra(EXTRA_SCORE, 0);
        int nivel      = getIntent().getIntExtra(EXTRA_LEVEL, 1);

        ScoreManager gestorPuntuacion = new ScoreManager(this);
        gestorPuntuacion.submitScore(puntuacion);

        TextView tvPuntuacion = findViewById(R.id.tvScore);
        TextView tvRecord     = findViewById(R.id.tvHighScore);
        TextView tvNivel      = findViewById(R.id.tvLevel);

        tvPuntuacion.setText(String.valueOf(puntuacion));
        tvRecord.setText(String.valueOf(gestorPuntuacion.getBestScore()));
        tvNivel.setText("Nivel " + nivel + " alcanzado");

        Button btnReintentar  = findViewById(R.id.btnPlayAgain);
        Button btnMenuPrincipal = findViewById(R.id.btnMainMenu);

        btnReintentar.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        btnMenuPrincipal.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        // El botón atrás lleva al menú principal para evitar volver al juego terminado
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
