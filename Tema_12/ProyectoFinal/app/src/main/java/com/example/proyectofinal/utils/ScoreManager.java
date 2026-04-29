package com.example.proyectofinal.utils;

import android.content.Context;
import android.content.SharedPreferences;

// Persiste y consulta la mejor puntuación usando SharedPreferences.
public class ScoreManager {

    private static final String NOMBRE_PREFS = "maze_game_prefs";
    private static final String CLAVE_MEJOR  = "best_score";

    private final SharedPreferences prefs;

    public ScoreManager(Context context) {
        prefs = context.getApplicationContext()
                       .getSharedPreferences(NOMBRE_PREFS, Context.MODE_PRIVATE);
    }

    public int getMejorPuntuacion() {
        return prefs.getInt(CLAVE_MEJOR, 0);
    }

    // Alias en inglés para compatibilidad con las Activities existentes
    public int getBestScore() {
        return getMejorPuntuacion();
    }

    // Guarda la puntuación solo si supera el récord actual. Devuelve true si es nuevo récord.
    public boolean submitScore(int puntuacion) {
        if (puntuacion > getMejorPuntuacion()) {
            prefs.edit().putInt(CLAVE_MEJOR, puntuacion).apply();
            return true;
        }
        return false;
    }

    public void resetearMejor() {
        prefs.edit().remove(CLAVE_MEJOR).apply();
    }
}
