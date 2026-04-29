package com.example.proyectofinal.game;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Enemigo aleatorio (robot): elige direcciones al azar en cada celda.
// Usa el walk_sheet compartido (tinte naranja).
public class RandomEnemy extends Enemy {

    private final Random aleatorio = new Random();

    public RandomEnemy(int filaInicio, int columnaInicio, float velocidad) {
        super(filaInicio, columnaInicio, velocidad);
    }

    // ------------------------------------------------------------------ //
    //  IA                                                                  //
    // ------------------------------------------------------------------ //
    @Override
    protected int decidirDireccion(int[][] laberinto, Player jugador) {
        List<Integer> opciones   = new ArrayList<>();
        int           dirOpuesta = opuestaDe(ultimaDir);

        for (int d = 0; d < 4; d++) {
            int nf = filaGrid    + GameConstants.DR[d];
            int nc = columnaGrid + GameConstants.DC[d];
            if (enRango(nf, nc, laberinto) && laberinto[nf][nc] != GameConstants.MURO) {
                if (d != dirOpuesta) opciones.add(d);
            }
        }

        if (opciones.isEmpty()) {
            return (dirOpuesta != GameConstants.DIR_NINGUNA) ? dirOpuesta
                                                             : GameConstants.DIR_NINGUNA;
        }
        return opciones.get(aleatorio.nextInt(opciones.size()));
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
            drawWalkSprite(canvas, desplX, desplY, tamCelda, ultimaDir, 0xAAFF6D00);
        } else {
            float cx = desplX + columnaPixel * tamCelda + tamCelda / 2f;
            float cy = desplY + filaPixel    * tamCelda + tamCelda / 2f;
            dibujarCuerpo(canvas, cx, cy, tamCelda * 0.38f, 0xFFFF6D00);
        }
    }
}
