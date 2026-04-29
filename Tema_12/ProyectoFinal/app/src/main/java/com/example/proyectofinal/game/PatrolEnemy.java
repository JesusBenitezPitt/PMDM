package com.example.proyectofinal.game;

import android.graphics.Canvas;

// Enemigo patrullero (soldado): avanza en línea recta y rebota en paredes.
// Usa el walk_sheet compartido (tinte morado).
public class PatrolEnemy extends Enemy {

    private int dirPatrulla = GameConstants.DIR_DERECHA;

    public PatrolEnemy(int filaInicio, int columnaInicio, float velocidad) {
        super(filaInicio, columnaInicio, velocidad);
    }

    // ------------------------------------------------------------------ //
    //  IA                                                                  //
    // ------------------------------------------------------------------ //
    @Override
    protected int decidirDireccion(int[][] laberinto, Player jugador) {
        if (puedeMover(dirPatrulla, laberinto)) return dirPatrulla;

        int invertida = opuestaDe(dirPatrulla);
        if (puedeMover(invertida, laberinto)) {
            dirPatrulla = invertida;
            return dirPatrulla;
        }

        for (int d = 0; d < 4; d++) {
            if (d != dirPatrulla && d != invertida && puedeMover(d, laberinto)) {
                dirPatrulla = d;
                return d;
            }
        }
        return GameConstants.DIR_NINGUNA;
    }

    private boolean puedeMover(int dir, int[][] laberinto) {
        int nf = filaGrid    + GameConstants.DR[dir];
        int nc = columnaGrid + GameConstants.DC[dir];
        return enRango(nf, nc, laberinto) && laberinto[nf][nc] != GameConstants.MURO;
    }

    private int opuestaDe(int dir) {
        switch (dir) {
            case GameConstants.DIR_ARRIBA:    return GameConstants.DIR_ABAJO;
            case GameConstants.DIR_ABAJO:     return GameConstants.DIR_ARRIBA;
            case GameConstants.DIR_IZQUIERDA: return GameConstants.DIR_DERECHA;
            case GameConstants.DIR_DERECHA:   return GameConstants.DIR_IZQUIERDA;
            default:                          return GameConstants.DIR_NINGUNA;
        }
    }

    // ------------------------------------------------------------------ //
    //  Renderizado                                                          //
    // ------------------------------------------------------------------ //
    @Override
    public void draw(Canvas canvas, float desplX, float desplY, float tamCelda) {
        if (WALK_SHEET != null) {
            drawWalkSprite(canvas, desplX, desplY, tamCelda, ultimaDir, 0xAA7B1FA2);
        } else {
            float cx = desplX + columnaPixel * tamCelda + tamCelda / 2f;
            float cy = desplY + filaPixel    * tamCelda + tamCelda / 2f;
            dibujarCuerpo(canvas, cx, cy, tamCelda * 0.38f, 0xFFAA00FF);
        }
    }
}
