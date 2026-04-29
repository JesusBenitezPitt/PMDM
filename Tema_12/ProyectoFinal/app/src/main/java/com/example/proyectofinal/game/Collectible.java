package com.example.proyectofinal.game;

import android.graphics.Canvas;
import android.graphics.Paint;

// Moneda recogible que suma puntos al jugador al pisarla.
public class Collectible {

    private final int fila;
    private final int columna;
    private boolean recogida = false;

    // Pinceles estáticos para evitar creaciones en cada frame
    private static final Paint PINCEL_MONEDA  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final Paint PINCEL_BRILLO  = new Paint(Paint.ANTI_ALIAS_FLAG);

    static {
        PINCEL_MONEDA.setColor(0xFFFFD600);
        PINCEL_BRILLO.setColor(0xFFFFFF99);
    }

    public Collectible(int fila, int columna) {
        this.fila    = fila;
        this.columna = columna;
    }

    public void draw(Canvas canvas, float desplX, float desplY, float tamCelda) {
        if (recogida) return;

        float cx    = desplX + columna * tamCelda + tamCelda / 2f;
        float cy    = desplY + fila    * tamCelda + tamCelda / 2f;
        float radio = tamCelda * 0.18f;

        canvas.drawCircle(cx, cy, radio, PINCEL_MONEDA);
        // Pequeño destello decorativo
        canvas.drawCircle(cx - radio * 0.3f, cy - radio * 0.3f, radio * 0.25f, PINCEL_BRILLO);
    }

    public boolean isCollected() { return recogida; }
    public void    collect()     { recogida = true; }
    public int     getRow()      { return fila; }
    public int     getCol()      { return columna; }
}
