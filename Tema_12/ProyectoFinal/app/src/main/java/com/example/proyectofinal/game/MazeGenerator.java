package com.example.proyectofinal.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Genera laberintos estilo Pac-Man: sin callejones sin salida, con muchos bucles
// y múltiples caminos de escape en cada punto del mapa.
public class MazeGenerator {

    private final int    filas;
    private final int    columnas;
    private       int[][] laberinto;
    private final Random  aleatorio;

    public MazeGenerator(int filas, int columnas) {
        this.filas    = (filas    % 2 == 0) ? filas    + 1 : filas;
        this.columnas = (columnas % 2 == 0) ? columnas + 1 : columnas;
        this.aleatorio = new Random();
    }

    public int[][] generate(long semilla) {
        aleatorio.setSeed(semilla);
        laberinto = new int[filas][columnas];

        // Inicializar todo como muros
        for (int f = 0; f < filas; f++)
            for (int c = 0; c < columnas; c++)
                laberinto[f][c] = GameConstants.MURO;

        // Base: árbol DFS — garantiza conectividad total desde (1,1)
        tallar(1, 1);

        // Garantizar que la celda de salida esté abierta
        laberinto[filas - 2][columnas - 2] = GameConstants.CAMINO;

        // Abrir el 35 % de los muros interiores que separan dos caminos
        // → suficientes bucles para escapar del perseguidor sin perder la sensación de laberinto
        anadirBucles(0.35);

        // Eliminar TODOS los callejones sin salida que queden:
        // ninguna celda de camino tendrá menos de 2 vecinos transitables.
        // Esto garantiza el estilo Pac-Man donde siempre hay escapatoria.
        eliminarCallejones();

        return laberinto;
    }

    // ------------------------------------------------------------------ //
    //  Generación base: DFS recursivo                                      //
    // ------------------------------------------------------------------ //
    private void tallar(int f, int c) {
        laberinto[f][c] = GameConstants.CAMINO;
        int[][] dirs = {{0, 2}, {2, 0}, {0, -2}, {-2, 0}};
        mezclar(dirs);
        for (int[] dir : dirs) {
            int nf = f + dir[0];
            int nc = c + dir[1];
            if (enRangoInterior(nf, nc) && laberinto[nf][nc] == GameConstants.MURO) {
                laberinto[f + dir[0] / 2][c + dir[1] / 2] = GameConstants.CAMINO;
                tallar(nf, nc);
            }
        }
    }

    // ------------------------------------------------------------------ //
    //  Paso 2: abrir bucles                                                //
    // ------------------------------------------------------------------ //
    private void anadirBucles(double fraccion) {
        List<int[]> candidatos = new ArrayList<>();
        for (int f = 1; f < filas - 1; f++) {
            for (int c = 1; c < columnas - 1; c++) {
                if (laberinto[f][c] == GameConstants.MURO) {
                    boolean horizontal = laberinto[f][c - 1] == GameConstants.CAMINO
                                      && laberinto[f][c + 1] == GameConstants.CAMINO;
                    boolean vertical   = laberinto[f - 1][c] == GameConstants.CAMINO
                                      && laberinto[f + 1][c] == GameConstants.CAMINO;
                    if (horizontal || vertical) candidatos.add(new int[]{f, c});
                }
            }
        }
        // Mezclar y abrir la fracción pedida
        for (int i = candidatos.size() - 1; i > 0; i--) {
            int   j   = aleatorio.nextInt(i + 1);
            int[] tmp = candidatos.get(i);
            candidatos.set(i, candidatos.get(j));
            candidatos.set(j, tmp);
        }
        int cantidad = (int) Math.max(1, candidatos.size() * fraccion);
        for (int i = 0; i < cantidad; i++)
            laberinto[candidatos.get(i)[0]][candidatos.get(i)[1]] = GameConstants.CAMINO;
    }

    // ------------------------------------------------------------------ //
    //  Paso 3: eliminar callejones sin salida (estilo Pac-Man)             //
    // ------------------------------------------------------------------ //
    // Itera hasta que ninguna celda de camino tenga menos de 2 salidas.
    // Para cada callejón busca el muro adyacente que da paso a otro camino
    // y lo abre. Mínimo cambio necesario para romper el callejón.
    private void eliminarCallejones() {
        boolean cambio = true;
        while (cambio) {
            cambio = false;
            for (int f = 1; f < filas - 1; f++) {
                for (int c = 1; c < columnas - 1; c++) {
                    if (laberinto[f][c] != GameConstants.CAMINO) continue;

                    int vecinos = 0;
                    for (int d = 0; d < 4; d++) {
                        int nf = f + GameConstants.DR[d];
                        int nc = c + GameConstants.DC[d];
                        if (enRangoTotal(nf, nc) && laberinto[nf][nc] == GameConstants.CAMINO)
                            vecinos++;
                    }
                    if (vecinos >= 2) continue; // ya tiene al menos 2 salidas

                    // Callejón: abrir el primer muro que conduzca a otro camino
                    for (int d = 0; d < 4; d++) {
                        int nf  = f  + GameConstants.DR[d];
                        int nc  = c  + GameConstants.DC[d];
                        int nf2 = nf + GameConstants.DR[d];
                        int nc2 = nc + GameConstants.DC[d];
                        if (!enRangoTotal(nf, nc)) continue;
                        if (laberinto[nf][nc] != GameConstants.MURO) continue;
                        if (!enRangoTotal(nf2, nc2)) continue;
                        if (laberinto[nf2][nc2] == GameConstants.CAMINO) {
                            laberinto[nf][nc] = GameConstants.CAMINO;
                            cambio = true;
                            break;
                        }
                    }
                }
            }
        }
    }

    // ------------------------------------------------------------------ //
    //  Utilidades                                                           //
    // ------------------------------------------------------------------ //

    /** Interior impares para DFS (evita el borde exterior). */
    private boolean enRangoInterior(int f, int c) {
        return f > 0 && f < filas - 1 && c > 0 && c < columnas - 1;
    }

    /** Rango completo del array (incluye borde). */
    private boolean enRangoTotal(int f, int c) {
        return f >= 0 && f < filas && c >= 0 && c < columnas;
    }

    private void mezclar(int[][] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            int   j   = aleatorio.nextInt(i + 1);
            int[] tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
    }

    // ------------------------------------------------------------------ //
    //  Helpers estáticos usados por otros sistemas                         //
    // ------------------------------------------------------------------ //

    public static List<int[]> getCeldasCamino(int[][] laberinto) {
        List<int[]> celdas = new ArrayList<>();
        for (int f = 0; f < laberinto.length; f++)
            for (int c = 0; c < laberinto[0].length; c++)
                if (laberinto[f][c] == GameConstants.CAMINO)
                    celdas.add(new int[]{f, c});
        return celdas;
    }

    public static int manhattan(int f1, int c1, int f2, int c2) {
        return Math.abs(f1 - f2) + Math.abs(c1 - c2);
    }
}
