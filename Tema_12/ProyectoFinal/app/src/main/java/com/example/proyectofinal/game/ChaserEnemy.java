package com.example.proyectofinal.game;

import android.graphics.Canvas;

import java.util.LinkedList;
import java.util.Queue;

// Enemigo perseguidor (zombi): BFS para encontrar el camino más corto al jugador.
// Usa el walk_sheet compartido (tinte rojo). En modo huida parpadea en azul.
public class ChaserEnemy extends Enemy {

    public ChaserEnemy(int filaInicio, int columnaInicio, float velocidad) {
        super(filaInicio, columnaInicio, velocidad);
    }

    // ------------------------------------------------------------------ //
    //  IA                                                                  //
    // ------------------------------------------------------------------ //
    @Override
    protected int decidirDireccion(int[][] laberinto, Player jugador) {
        if (modoHuida) return movimientoDispersion(laberinto);
        return bfs(laberinto, filaGrid, columnaGrid,
                   jugador.getGridRow(), jugador.getGridCol());
    }

    private int movimientoDispersion(int[][] laberinto) {
        int opuesto = (ultimaDir >= 0) ? (ultimaDir + 2) % 4 : -1;
        int base    = (ultimaDir >= 0) ? ultimaDir : 0;
        int[] prefs = { base, (base+1)%4, (base+3)%4,
                        opuesto >= 0 ? opuesto : (base+2)%4 };
        for (int d : prefs) {
            int nf = filaGrid + GameConstants.DR[d];
            int nc = columnaGrid + GameConstants.DC[d];
            if (enRango(nf, nc, laberinto) && laberinto[nf][nc] != GameConstants.MURO)
                return d;
        }
        return GameConstants.DIR_NINGUNA;
    }

    private int bfs(int[][] laberinto, int fi, int ci, int fd, int cd) {
        if (fi == fd && ci == cd) return GameConstants.DIR_NINGUNA;
        int filas = laberinto.length, cols = laberinto[0].length;
        int[][] primerDir = new int[filas][cols];
        for (int[] r : primerDir) java.util.Arrays.fill(r, -1);
        boolean[][] visitado = new boolean[filas][cols];
        visitado[fi][ci] = true;
        Queue<int[]> cola = new LinkedList<>();
        for (int d = 0; d < 4; d++) {
            int nf = fi + GameConstants.DR[d], nc = ci + GameConstants.DC[d];
            if (enRango(nf, nc, laberinto) && laberinto[nf][nc] != GameConstants.MURO
                    && !visitado[nf][nc]) {
                visitado[nf][nc] = true;
                primerDir[nf][nc] = d;
                if (nf == fd && nc == cd) return d;
                cola.add(new int[]{nf, nc});
            }
        }
        while (!cola.isEmpty()) {
            int[] actual = cola.poll();
            int f = actual[0], c = actual[1];
            for (int d = 0; d < 4; d++) {
                int nf = f + GameConstants.DR[d], nc = c + GameConstants.DC[d];
                if (enRango(nf, nc, laberinto) && laberinto[nf][nc] != GameConstants.MURO
                        && !visitado[nf][nc]) {
                    visitado[nf][nc] = true;
                    primerDir[nf][nc] = primerDir[f][c];
                    if (nf == fd && nc == cd) return primerDir[nf][nc];
                    cola.add(new int[]{nf, nc});
                }
            }
        }
        return GameConstants.DIR_NINGUNA;
    }

    // ------------------------------------------------------------------ //
    //  Renderizado                                                          //
    // ------------------------------------------------------------------ //
    @Override
    public void draw(Canvas canvas, float desplX, float desplY, float tamCelda) {
        if (WALK_SHEET != null) {
            Integer tint;
            if (modoHuida) {
                // Parpadeo azul durante el periodo de gracia
                boolean parpadeo = (System.currentTimeMillis() / 300) % 2 == 0;
                tint = parpadeo ? 0xCC1565C0 : 0xCC42A5F5;
            } else {
                tint = 0xAAFF1744; // tinte rojo
            }
            drawWalkSprite(canvas, desplX, desplY, tamCelda, ultimaDir, tint);
        } else {
            float cx = desplX + columnaPixel * tamCelda + tamCelda / 2f;
            float cy = desplY + filaPixel    * tamCelda + tamCelda / 2f;
            dibujarCuerpo(canvas, cx, cy, tamCelda * 0.38f,
                          modoHuida ? 0xFF1565C0 : 0xFFFF1744);
        }
    }
}
