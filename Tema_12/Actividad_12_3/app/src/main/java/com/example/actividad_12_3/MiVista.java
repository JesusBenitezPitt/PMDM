package com.example.actividad_12_3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class MiVista extends View {

    float velocidad = 8f;
    int x, y;
    int radio = 80;
    Paint pincel;

    public MiVista(Context context) {
        super(context);

        pincel = new Paint();
        pincel.setColor(Color.RED);

        x = 500;
        y = radio;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Mover la bola
        y += velocidad;

        // Rebote arriba
        if (y - radio <= 0) {
            velocidad = Math.abs(velocidad);
        }

        // Rebote abajo
        if (y + radio >= getHeight()) {
            velocidad = -Math.abs(velocidad);
        }

        // Dibujar la bola
        canvas.drawCircle(x, y, radio, pincel);

        // Animación infinita
        invalidate();
    }
}

