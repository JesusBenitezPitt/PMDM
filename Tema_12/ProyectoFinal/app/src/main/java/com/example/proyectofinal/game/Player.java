package com.example.proyectofinal.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;

import com.example.proyectofinal.R;

// Jugador controlado por el usuario.
// Animación: walk_sheet.png compartido (CC0) — 6 frames × 4 direcciones, sin rotación.
public class Player {

    // ------------------------------------------------------------------ //
    //  Estado de posición y movimiento                                     //
    // ------------------------------------------------------------------ //
    private int   filaGrid, columnaGrid;
    private float filaPixel, columnaPixel;
    private int   filaDestino, columnaDestino;

    private int     dirPendiente = GameConstants.DIR_NINGUNA;
    private int     dirActual    = GameConstants.DIR_NINGUNA;
    private boolean moviendose   = false;

    private boolean turbo    = false;
    private boolean escudado = false;

    private boolean muriendo    = false;
    private float   faseParpado = 0f;

    // ------------------------------------------------------------------ //
    //  Recursos gráficos (static = se cargan una sola vez)                 //
    //  walk_sheet.png: 8 cols × 4 filas, 24×32 px/frame                    //
    //  Fila 0=ABAJO, Fila 1=ARRIBA, Fila 2=IZQUIERDA, Fila 3=DERECHA       //
    // ------------------------------------------------------------------ //
    private static Bitmap walkSheet = null;
    private static final int WALK_COLS      = 8;
    private static final int WALK_ROWS      = 4;
    private static final int WALK_ANIM_FRAMES = 8;

    private static final Paint PINCEL        = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final Paint PINCEL_ESCUDO = new Paint(Paint.ANTI_ALIAS_FLAG);

    // Estado de animación por instancia
    private float tiempoAnim = 0f;
    private int   frameAnim  = 0;

    static {
        PINCEL_ESCUDO.setStyle(Paint.Style.STROKE);
        PINCEL_ESCUDO.setColor(0xFF448AFF);
    }

    public static void cargarSprite(Context ctx) {
        if (walkSheet == null) {
            walkSheet = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.walk_sheet);
        }
    }

    // ------------------------------------------------------------------ //
    //  Constructor                                                          //
    // ------------------------------------------------------------------ //
    public Player(int filaInicio, int columnaInicio) {
        filaGrid       = filaInicio;
        columnaGrid    = columnaInicio;
        filaPixel      = filaInicio;
        columnaPixel   = columnaInicio;
        filaDestino    = filaInicio;
        columnaDestino = columnaInicio;
    }

    // ------------------------------------------------------------------ //
    //  Lógica                                                               //
    // ------------------------------------------------------------------ //
    public void update(float deltaSeg, int[][] laberinto) {
        if (moviendose) {
            float velocidad = turbo ? GameConstants.VELOCIDAD_TURBO : GameConstants.VELOCIDAD_JUGADOR;
            float paso = velocidad * deltaSeg;
            float df   = filaDestino    - filaPixel;
            float dc   = columnaDestino - columnaPixel;
            float dist = (float) Math.sqrt(df * df + dc * dc);

            if (dist <= paso) {
                filaPixel      = filaDestino;
                columnaPixel   = columnaDestino;
                filaGrid       = filaDestino;
                columnaGrid    = columnaDestino;
                moviendose     = false;
                intentarMover(dirPendiente, laberinto);
            } else {
                filaPixel    += (df / dist) * paso;
                columnaPixel += (dc / dist) * paso;
            }

            // Avanzar animación de caminar (8 fps)
            tiempoAnim += deltaSeg;
            frameAnim   = (int)(tiempoAnim * 8f) % WALK_ANIM_FRAMES;
        } else {
            intentarMover(dirPendiente, laberinto);
        }

        if (muriendo) faseParpado += deltaSeg * 8f;
    }

    private void intentarMover(int dir, int[][] laberinto) {
        if (dir == GameConstants.DIR_NINGUNA) return;
        int nf = filaGrid    + GameConstants.DR[dir];
        int nc = columnaGrid + GameConstants.DC[dir];
        if (enRango(nf, nc, laberinto) && laberinto[nf][nc] != GameConstants.MURO) {
            filaDestino    = nf;
            columnaDestino = nc;
            dirActual      = dir;
            moviendose     = true;
        }
    }

    // ------------------------------------------------------------------ //
    //  Renderizado                                                          //
    // ------------------------------------------------------------------ //
    public void draw(Canvas canvas, float desplX, float desplY, float tamCelda) {
        if (muriendo && ((int) faseParpado % 2 == 1)) return;

        float cx  = desplX + columnaPixel * tamCelda + tamCelda / 2f;
        float cy  = desplY + filaPixel    * tamCelda + tamCelda / 2f;
        float lado = tamCelda * 0.82f;

        // Escudo: anillo azul
        if (escudado) {
            PINCEL_ESCUDO.setStrokeWidth(tamCelda * 0.07f);
            canvas.drawCircle(cx, cy, lado * 0.58f, PINCEL_ESCUDO);
        }

        if (walkSheet != null) {
            // Selección de fila según dirección (walk_sheet: Fila0=ABAJO, 1=IZQ, 2=DER, 3=ARRIBA)
            int row;
            switch (dirActual) {
                case GameConstants.DIR_ABAJO:     row = 0; break;
                case GameConstants.DIR_ARRIBA:    row = 1; break;
                case GameConstants.DIR_IZQUIERDA: row = 2; break;
                default:                          row = 3; break; // DERECHA
            }

            int frameW = walkSheet.getWidth()  / WALK_COLS;
            int frameH = walkSheet.getHeight() / WALK_ROWS;
            int srcX   = frameAnim * frameW;
            int srcY   = row       * frameH;

            Rect  src = new Rect(srcX, srcY, srcX + frameW, srcY + frameH);
            RectF dst = new RectF(cx - lado / 2f, cy - lado / 2f,
                                  cx + lado / 2f, cy + lado / 2f);

            // Tinte dorado si turbo activo
            PINCEL.setColorFilter(turbo
                ? new PorterDuffColorFilter(0xAAFFD600, PorterDuff.Mode.SRC_ATOP)
                : null);

            canvas.drawBitmap(walkSheet, src, dst, PINCEL);
        } else {
            // Fallback
            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
            p.setColor(turbo ? 0xFFFFD600 : 0xFF2196F3);
            canvas.drawCircle(cx, cy, lado / 2f, p);
        }
    }

    // ------------------------------------------------------------------ //
    //  Helpers                                                              //
    // ------------------------------------------------------------------ //
    private boolean enRango(int f, int c, int[][] laberinto) {
        return f >= 0 && f < laberinto.length && c >= 0 && c < laberinto[0].length;
    }

    public void respawn(int fila, int columna) {
        filaGrid       = fila;   columnaGrid    = columna;
        filaPixel      = fila;   columnaPixel   = columna;
        filaDestino    = fila;   columnaDestino = columna;
        moviendose     = false;
        dirPendiente   = GameConstants.DIR_NINGUNA;
        dirActual      = GameConstants.DIR_NINGUNA;
        muriendo       = false;
        faseParpado    = 0f;
        turbo          = false;
        escudado       = false;
        tiempoAnim     = 0f;
        frameAnim      = 0;
    }

    public void setPendingDirection(int dir) { dirPendiente = dir; }
    public void setSpeedBoost(boolean v)     { turbo    = v; }
    public void setShielded(boolean v)       { escudado = v; }
    public void setDying(boolean v)          { muriendo = v; if (!v) faseParpado = 0f; }

    public int     getGridRow()  { return filaGrid; }
    public int     getGridCol()  { return columnaGrid; }
    public float   getPixelRow() { return filaPixel; }
    public float   getPixelCol() { return columnaPixel; }
    public boolean isMoving()    { return moviendose; }
    public boolean isShielded()  { return escudado; }
    public boolean isDying()     { return muriendo; }
}
