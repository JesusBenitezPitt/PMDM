package com.example.proyectofinal.game;

// Constantes globales compartidas por todas las clases del juego.
public final class GameConstants {

    // Tipos de celda del laberinto
    public static final int MURO   = 0;
    public static final int CAMINO = 1;

    // Alias de compatibilidad (usados internamente por el generador)
    public static final int WALL = MURO;
    public static final int PATH = CAMINO;

    // Direcciones de movimiento
    public static final int DIR_NINGUNA  = -1;
    public static final int DIR_ARRIBA   = 0;
    public static final int DIR_DERECHA  = 1;
    public static final int DIR_ABAJO    = 2;
    public static final int DIR_IZQUIERDA = 3;

    // Alias en inglés para retrocompatibilidad interna
    public static final int DIR_NONE  = DIR_NINGUNA;
    public static final int DIR_UP    = DIR_ARRIBA;
    public static final int DIR_RIGHT = DIR_DERECHA;
    public static final int DIR_DOWN  = DIR_ABAJO;
    public static final int DIR_LEFT  = DIR_IZQUIERDA;

    // Variaciones de fila y columna según dirección (indexadas por DIR_*)
    public static final int[] DR = {-1, 0, 1,  0};
    public static final int[] DC = { 0, 1, 0, -1};

    // Dimensiones del laberinto (deben ser impares para el algoritmo DFS)
    public static final int FILAS_LABERINTO    = 15;
    public static final int COLUMNAS_LABERINTO = 15;

    // Alias en inglés
    public static final int MAZE_ROWS = FILAS_LABERINTO;
    public static final int MAZE_COLS = COLUMNAS_LABERINTO;

    // Parámetros del jugador
    public static final float VELOCIDAD_JUGADOR        = 4.0f;  // celdas por segundo
    public static final float VELOCIDAD_TURBO          = 8.0f;
    public static final long  DURACION_POWERUP_MS      = 5000L;
    public static final int   VIDAS_INICIALES          = 3;
    public static final long  DURACION_MUERTE_MS         = 1500L;
    public static final long  DURACION_NIVEL_COMPLETO_MS = 2000L;
    // Periodo de gracia tras respawn: el enemigo rojo no persigue al jugador
    public static final long  DURACION_GRACIA_MS         = 3000L;

    // Alias en inglés
    public static final float PLAYER_SPEED        = VELOCIDAD_JUGADOR;
    public static final float PLAYER_BOOST_SPEED  = VELOCIDAD_TURBO;
    public static final long  POWERUP_DURATION_MS = DURACION_POWERUP_MS;
    public static final int   PLAYER_START_LIVES  = VIDAS_INICIALES;
    public static final long  DEATH_ANIM_MS       = DURACION_MUERTE_MS;
    public static final long  LEVEL_COMPLETE_MS   = DURACION_NIVEL_COMPLETO_MS;

    // Puntuaciones
    public static final int PUNTOS_MONEDA     = 10;
    public static final int PUNTOS_POWERUP    = 50;
    public static final int PUNTOS_NIVEL      = 100;  // multiplicado por número de nivel
    public static final int PENALIZACION_TRAMPA = -10; // las trampas restan puntos

    // Alias en inglés
    public static final int SCORE_COIN    = PUNTOS_MONEDA;
    public static final int SCORE_POWERUP = PUNTOS_POWERUP;
    public static final int SCORE_LEVEL   = PUNTOS_NIVEL;

    // Bonus de tiempo: segundos máximos para obtener bonus completo
    public static final int SEGUNDOS_BONUS_MAX = 120;

    private GameConstants() {}
}
