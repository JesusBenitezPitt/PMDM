package com.example.proyectofinal.game;

import android.graphics.Canvas;
import android.graphics.Paint;

// Trampa oculta en el suelo que resta puntos al jugador cuando la pisa.
public class Trampa {

    private final int fila;
    private final int columna;
    private boolean activada = false;

    private static final Paint PINCEL_FONDO  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final Paint PINCEL_X      = new Paint(Paint.ANTI_ALIAS_FLAG);

    static {
        PINCEL_FONDO.setColor(0x55FF1744);

        PINCEL_X.setColor(0xFFFF1744);
        PINCEL_X.setStyle(Paint.Style.STROKE);
        PINCEL_X.setStrokeCap(Paint.Cap.ROUND);
    }

    public Trampa(int fila, int columna) {
        this.fila    = fila;
        this.columna = columna;
    }

    public void dibujar(Canvas canvas, float desplX, float desplY, float tamCelda) {
        if (activada) return;

        float cx     = desplX + columna * tamCelda + tamCelda / 2f;
        float cy     = desplY + fila    * tamCelda + tamCelda / 2f;
        float radio  = tamCelda * 0.28f;
        float grosor = tamCelda * 0.09f;

        // Fondo circular semitransparente
        canvas.drawCircle(cx, cy, radio, PINCEL_FONDO);

        // X roja
        PINCEL_X.setStrokeWidth(grosor);
        float off = radio * 0.55f;
        canvas.drawLine(cx - off, cy - off, cx + off, cy + off, PINCEL_X);
        canvas.drawLine(cx + off, cy - off, cx - off, cy + off, PINCEL_X);
    }

    public boolean isActivada() { return activada; }
    public void    activar()    { activada = true; }
    public int     getFila()    { return fila; }
    public int     getColumna() { return columna; }
}
