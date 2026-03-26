package com.ejemplo.examen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class GameView extends SurfaceView implements Runnable {

    private Thread gameThread;
    private volatile boolean running = false;
    private SurfaceHolder holder;
    private Paint pincel;
    private float bolaX = (float) Math.random()*400;
    private float bolaY = (float) Math.random()*400;
    private int radio = 50;
    private float velocidadX = 5;
    private float velocidadY = 5;

    private int anchoPantalla;
    private int altoPantalla;

    public GameView(Context context) {
        super(context);
        holder = getHolder();
        pincel = new Paint();
        pincel.setColor(Color.RED);
        pincel.setAntiAlias(true);
    }

    @Override
    public void run() {
        while (running) {
            if (!holder.getSurface().isValid()) {
                continue;
            }

            anchoPantalla = getWidth();
            altoPantalla = getHeight();

            actualizarMovimiento();
            dibujar();

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void actualizarMovimiento() {
        bolaX += velocidadX;
        bolaY += velocidadY;

        if (bolaX + radio > anchoPantalla || bolaX - radio < 0) {
            velocidadX = -velocidadX;
        }

        if (bolaY + radio > altoPantalla || bolaY - radio < 0) {
            velocidadY = -velocidadY;
        }
    }

    private void dibujar() {
        Canvas canvas = holder.lockCanvas();
        if (canvas != null) {
            try {
                canvas.drawColor(Color.WHITE);
                canvas.drawCircle(bolaX, bolaY, radio, pincel);
            } finally {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            bolaX = event.getX();
            bolaY = event.getY();
        }
        return true;
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
}