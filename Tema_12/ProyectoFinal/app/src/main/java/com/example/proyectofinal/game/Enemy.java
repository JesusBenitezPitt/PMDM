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

// Clase base para todos los tipos de enemigo. Gestiona el movimiento suave celda a celda;
// las subclases deciden la dirección en cada paso.
public abstract class Enemy {

    protected int   filaGrid, columnaGrid;
    protected int   filaDestino, columnaDestino;
    protected float filaPixel, columnaPixel;

    protected float velocidadBase;
    protected float factorLento = 1.0f;

    // Cuando es true el enemigo entra en modo dispersión (huye / se mueve al azar)
    // Se activa durante el periodo de gracia del jugador tras perder una vida.
    protected boolean modoHuida = false;

    protected boolean moviendose = false;
    protected int     ultimaDir  = GameConstants.DIR_NINGUNA;

    // ------------------------------------------------------------------ //
    //  Walk-sheet compartido por todos los enemigos                        //
    // ------------------------------------------------------------------ //
    protected static Bitmap WALK_SHEET = null;
    private   static final int WALK_COLS       = 8;
    private   static final int WALK_ROWS       = 4;
    private   static final int WALK_ANIM_FRAMES = 8;

    public static void cargarWalkSheet(Context ctx) {
        if (WALK_SHEET == null)
            WALK_SHEET = BitmapFactory.decodeResource(
                    ctx.getResources(), R.drawable.walk_sheet);
    }

    // Estado de animación por instancia
    protected float tiempoAnim = 0f;
    protected int   frameAnim  = 0;

    protected static final Paint PINCEL_CUERPO = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected static final Paint PINCEL_PUPILA = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected static final Paint PINCEL_BLANCO = new Paint(Paint.ANTI_ALIAS_FLAG);

    static {
        PINCEL_PUPILA.setColor(0xFF000000);
        PINCEL_BLANCO.setColor(0xFFFFFFFF);
    }

    public Enemy(int filaInicio, int columnaInicio, float velocidad) {
        this.filaGrid      = filaInicio;
        this.columnaGrid   = columnaInicio;
        this.filaPixel     = filaInicio;
        this.columnaPixel  = columnaInicio;
        this.filaDestino   = filaInicio;
        this.columnaDestino = columnaInicio;
        this.velocidadBase = velocidad;
    }

    // Actualiza la posición del enemigo según los segundos transcurridos.
    public void update(float deltaSeg, int[][] laberinto, Player jugador) {
        float velocidad = velocidadBase * factorLento;

        if (moviendose) {
            float paso = velocidad * deltaSeg;
            float df   = filaDestino    - filaPixel;
            float dc   = columnaDestino - columnaPixel;
            float dist = (float) Math.sqrt(df * df + dc * dc);

            if (dist <= paso) {
                filaPixel     = filaDestino;
                columnaPixel  = columnaDestino;
                filaGrid      = filaDestino;
                columnaGrid   = columnaDestino;
                moviendose    = false;
                iniciarSiguienteMovimiento(laberinto, jugador);
            } else {
                filaPixel    += (df / dist) * paso;
                columnaPixel += (dc / dist) * paso;
            }

            // Avanzar animación de caminar (8 fps)
            tiempoAnim += deltaSeg;
            frameAnim   = (int)(tiempoAnim * 8f) % WALK_ANIM_FRAMES;
        } else {
            iniciarSiguienteMovimiento(laberinto, jugador);
        }
    }

    private void iniciarSiguienteMovimiento(int[][] laberinto, Player jugador) {
        int dir = decidirDireccion(laberinto, jugador);
        if (dir == GameConstants.DIR_NINGUNA) return;

        int nf = filaGrid    + GameConstants.DR[dir];
        int nc = columnaGrid + GameConstants.DC[dir];
        if (enRango(nf, nc, laberinto) && laberinto[nf][nc] != GameConstants.MURO) {
            filaDestino    = nf;
            columnaDestino = nc;
            ultimaDir      = dir;
            moviendose     = true;
        }
    }

    // Devuelve la dirección que el enemigo quiere tomar al llegar a una nueva celda.
    protected abstract int decidirDireccion(int[][] laberinto, Player jugador);

    public abstract void draw(Canvas canvas, float desplX, float desplY, float tamCelda);

    protected boolean enRango(int f, int c, int[][] laberinto) {
        return f >= 0 && f < laberinto.length && c >= 0 && c < laberinto[0].length;
    }

    // Dibuja el cuerpo genérico del enemigo con ojos grandes; el color lo define la subclase.
    protected void dibujarCuerpo(Canvas canvas, float cx, float cy, float r, int color) {
        PINCEL_CUERPO.setColor(color);
        canvas.drawCircle(cx, cy, r, PINCEL_CUERPO);

        float radioOjo = r * 0.22f;
        canvas.drawCircle(cx - r * 0.3f, cy - r * 0.15f, radioOjo, PINCEL_BLANCO);
        canvas.drawCircle(cx + r * 0.3f, cy - r * 0.15f, radioOjo, PINCEL_BLANCO);

        canvas.drawCircle(cx - r * 0.3f, cy - r * 0.1f, radioOjo * 0.5f, PINCEL_PUPILA);
        canvas.drawCircle(cx + r * 0.3f, cy - r * 0.1f, radioOjo * 0.5f, PINCEL_PUPILA);
    }

    public void  setSlowed(boolean lento)    { factorLento = lento ? 0.4f : 1.0f; }
    public void  setModoHuida(boolean huida) { modoHuida = huida; }
    public boolean isModoHuida()             { return modoHuida; }

    // ------------------------------------------------------------------ //
    //  Helper para subclases: dibuja un frame del walk-sheet               //
    //  walk_sheet.png: 8 columnas × 4 filas, 24×32 px por frame            //
    //  Fila 0 = ABAJO, Fila 1 = ARRIBA, Fila 2 = IZQUIERDA, Fila 3 = DERECHA
    // ------------------------------------------------------------------ //
    private static final Paint PINCEL_SPRITE = new Paint(Paint.ANTI_ALIAS_FLAG);

    protected void drawWalkSprite(Canvas canvas, float desplX, float desplY,
                                   float tamCelda, int dir, Integer tintColor) {
        if (WALK_SHEET == null) return;

        float cx  = desplX + columnaPixel * tamCelda + tamCelda / 2f;
        float cy  = desplY + filaPixel    * tamCelda + tamCelda / 2f;
        float lado = tamCelda * 0.78f;

        // Selección de fila según dirección
        int row;
        switch (dir) {
            case GameConstants.DIR_ABAJO:     row = 0; break;
            case GameConstants.DIR_ARRIBA:    row = 1; break;
            case GameConstants.DIR_IZQUIERDA: row = 2; break;
            default:                          row = 3; break; // DERECHA
        }

        int frameW = WALK_SHEET.getWidth()  / WALK_COLS;
        int frameH = WALK_SHEET.getHeight() / WALK_ROWS;
        int srcX   = frameAnim * frameW;
        int srcY   = row       * frameH;

        Rect  src = new Rect(srcX, srcY, srcX + frameW, srcY + frameH);
        RectF dst = new RectF(cx - lado / 2f, cy - lado / 2f,
                              cx + lado / 2f, cy + lado / 2f);

        if (tintColor != null) {
            PINCEL_SPRITE.setColorFilter(
                    new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_ATOP));
        } else {
            PINCEL_SPRITE.setColorFilter(null);
        }

        canvas.drawBitmap(WALK_SHEET, src, dst, PINCEL_SPRITE);
    }

    public float getPixelRow() { return filaPixel; }
    public float getPixelCol() { return columnaPixel; }
    public int   getGridRow()  { return filaGrid; }
    public int   getGridCol()  { return columnaGrid; }
}
