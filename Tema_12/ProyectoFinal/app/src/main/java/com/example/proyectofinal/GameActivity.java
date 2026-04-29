package com.example.proyectofinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectofinal.game.GameConstants;
import com.example.proyectofinal.game.GameEngine;
import com.example.proyectofinal.game.GameView;
import com.example.proyectofinal.utils.SoundManager;

// Activity principal de juego: conecta los botones de control, el motor y el sonido.
public class GameActivity extends AppCompatActivity
        implements GameView.GameViewListener, GameEngine.Callback {

    private GameView     vistaJuego;
    private GameEngine   motor;
    private SoundManager sonido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Pantalla completa y mantener pantalla encendida durante la partida
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game);

        sonido = new SoundManager();
        motor  = new GameEngine(this);

        vistaJuego = findViewById(R.id.gameView);
        vistaJuego.init(motor, sonido, this);

        conectarBotonesDpad();
        conectarAreaPausa();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sonido.startMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sonido.pauseMusic();
        // Pausar el motor al ir a segundo plano
        if (motor.getState() == GameEngine.State.PLAYING) {
            motor.togglePause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sonido.release();
    }

    // Asocia los cuatro botones del D-pad a las direcciones del motor.
    // Se usa ACTION_DOWN para registrar la dirección en el momento exacto del toque,
    // eliminando el retardo que producía setOnClickListener (que dispara en ACTION_UP).
    private void conectarBotonesDpad() {
        vincularBoton(R.id.btnArriba,    GameConstants.DIR_ARRIBA);
        vincularBoton(R.id.btnAbajo,     GameConstants.DIR_ABAJO);
        vincularBoton(R.id.btnIzquierda, GameConstants.DIR_IZQUIERDA);
        vincularBoton(R.id.btnDerecha,   GameConstants.DIR_DERECHA);
    }

    private void vincularBoton(int idVista, int direccion) {
        Button btn = findViewById(idVista);
        btn.setOnTouchListener((v, event) -> {
            // ACTION_DOWN: dirección registrada al instante, sin esperar a soltar
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                motor.setPlayerDirection(direccion);
                v.performClick(); // requerido para accesibilidad
            }
            return false; // devolver false mantiene el efecto visual (ripple) del botón
        });
    }

    // Al tocar la vista del juego (área del laberinto) se muestra el menú de pausa.
    private void conectarAreaPausa() {
        vistaJuego.setOnClickListener(v -> mostrarDialogoPausa());
    }

    private void mostrarDialogoPausa() {
        if (motor.getState() != GameEngine.State.PLAYING
                && motor.getState() != GameEngine.State.PAUSED) return;

        motor.togglePause();

        new AlertDialog.Builder(this, R.style.Theme_MazeGame)
            .setTitle("⏸  PAUSA")
            .setItems(
                new CharSequence[]{"▶  Continuar", "🔄  Reiniciar", "🏠  Menú principal"},
                (dialog, opcion) -> {
                    switch (opcion) {
                        case 0:
                            motor.togglePause();
                            break;
                        case 1:
                            motor.restart();
                            sonido.stopMusic();
                            sonido.startMusic();
                            break;
                        case 2:
                            irAlMenuPrincipal();
                            break;
                    }
                })
            .setOnCancelListener(d -> motor.togglePause())
            .show();
    }

    // GameView.GameViewListener: navega a la pantalla de fin de partida
    @Override
    public void onGameOver(int puntuacion, int nivel) {
        runOnUiThread(() -> {
            Intent intent = new Intent(this, GameOverActivity.class);
            intent.putExtra(GameOverActivity.EXTRA_SCORE, puntuacion);
            intent.putExtra(GameOverActivity.EXTRA_LEVEL, nivel);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });
    }

    // GameEngine.Callback: efectos de sonido para cada evento de juego
    @Override
    public void onCoinCollected() {
        sonido.playCoin();
    }

    @Override
    public void onPowerUpCollected(int tipo) {
        sonido.playPowerUp();
    }

    @Override
    public void onTrampaActivada() {
        sonido.playTrampa();
    }

    @Override
    public void onPlayerHit() {
        sonido.playHit();
    }

    @Override
    public void onLevelComplete(int nivel, int puntuacion) {
        sonido.playLevelComplete();
    }

    @Override
    public void onGameOver(int puntuacion) {
        sonido.playGameOver();
        sonido.stopMusic();
    }

    private void irAlMenuPrincipal() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    public void onBackPressed() {
        mostrarDialogoPausa();
    }
}
