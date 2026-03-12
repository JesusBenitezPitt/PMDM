package com.ejemplo.practica_1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class MiVista extends SurfaceView implements Runnable {

    Thread gameThread;
    boolean running = false;
    SurfaceHolder holder;
    float velocidad = 8f;
    int x, y;
    int radio = 80;
    Paint pincel;

    public MiVista(Context context) {
        super(context);

        holder = getHolder();
        pincel = new Paint();
        pincel.setColor(Color.RED);

        x = 500;
        y = radio;
    }

    @Override
    public void run() {
        while (running) {
            if (!holder.getSurface().isValid()) continue;

            Canvas canvas = holder.lockCanvas();
            canvas.drawColor(Color.WHITE);
            canvas.drawCircle(400, 400, 100, pincel);
            holder.unlockCanvasAndPost(canvas);

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void resume() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void pause() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//
//        // Mover la bola
//        y += velocidad;
//
//        // Rebote arriba
//        if (y - radio <= 0) {
//            velocidad = Math.abs(velocidad);
//        }
//
//        // Rebote abajo
//        if (y + radio >= getHeight()) {
//            velocidad = -Math.abs(velocidad);
//        }
//
//        // Dibujar la bola
//        canvas.drawCircle(x, y, radio, pincel);
//
//        // Animación infinita
//        invalidate();
//    }
}

