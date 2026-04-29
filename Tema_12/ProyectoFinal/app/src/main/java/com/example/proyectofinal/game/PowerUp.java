package com.example.proyectofinal.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

// Objeto especial que otorga un efecto temporal al jugador al recogerlo.
public class PowerUp {

    public static final int TIPO_VELOCIDAD = 0; // turbo de movimiento
    public static final int TIPO_ESCUDO    = 1; // inmunidad a enemigos
    public static final int TIPO_LENTO     = 2; // ralentiza a los enemigos

    // Alias en inglés para compatibilidad con GameEngine
    public static final int TYPE_SPEED  = TIPO_VELOCIDAD;
    public static final int TYPE_SHIELD = TIPO_ESCUDO;
    public static final int TYPE_SLOW   = TIPO_LENTO;

    private final int     fila;
    private final int     columna;
    private final int     tipo;
    private       boolean recogido = false;

    private static final Paint PINCEL      = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final Paint PINCEL_TEXT = new Paint(Paint.ANTI_ALIAS_FLAG);

    static {
        PINCEL_TEXT.setTextAlign(Paint.Align.CENTER);
        PINCEL_TEXT.setColor(0xFF000000);
        PINCEL_TEXT.setTextSize(20f);
        PINCEL_TEXT.setFakeBoldText(true);
    }

    public PowerUp(int fila, int columna, int tipo) {
        this.fila    = fila;
        this.columna = columna;
        this.tipo    = tipo;
    }

    public void draw(Canvas canvas, float desplX, float desplY, float tamCelda) {
        if (recogido) return;

        float cx = desplX + columna * tamCelda + tamCelda / 2f;
        float cy = desplY + fila    * tamCelda + tamCelda / 2f;
        float r  = tamCelda * 0.32f;

        switch (tipo) {
            case TIPO_VELOCIDAD:
                PINCEL.setColor(0xFFFFFF00);
                dibujarEstrella(canvas, cx, cy, r, PINCEL);
                break;
            case TIPO_ESCUDO:
                PINCEL.setColor(0xFF448AFF);
                canvas.drawCircle(cx, cy, r, PINCEL);
                PINCEL_TEXT.setTextSize(r * 1.0f);
                canvas.drawText("S", cx, cy + r * 0.35f, PINCEL_TEXT);
                break;
            case TIPO_LENTO:
                PINCEL.setColor(0xFFEA80FC);
                canvas.drawCircle(cx, cy, r, PINCEL);
                PINCEL_TEXT.setTextSize(r * 0.9f);
                canvas.drawText("⏱", cx, cy + r * 0.35f, PINCEL_TEXT);
                break;
        }
    }

    // Dibuja una estrella de 5 puntas centrada en (cx, cy) con radio exterior r.
    private void dibujarEstrella(Canvas canvas, float cx, float cy, float r, Paint pincel) {
        float radioInterior = r * 0.45f;
        Path  camino        = new Path();
        for (int i = 0; i < 10; i++) {
            double angulo = Math.toRadians(-90 + i * 36);
            float  radio  = (i % 2 == 0) ? r : radioInterior;
            float  x      = cx + (float)(radio * Math.cos(angulo));
            float  y      = cy + (float)(radio * Math.sin(angulo));
            if (i == 0) camino.moveTo(x, y);
            else        camino.lineTo(x, y);
        }
        camino.close();
        canvas.drawPath(camino, pincel);
    }

    public boolean isCollected() { return recogido; }
    public void    collect()     { recogido = true; }
    public int     getRow()      { return fila; }
    public int     getCol()      { return columna; }
    public int     getType()     { return tipo; }
}
