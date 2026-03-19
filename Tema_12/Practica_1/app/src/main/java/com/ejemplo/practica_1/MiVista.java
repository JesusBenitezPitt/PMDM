package com.ejemplo.practica_1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MiVista extends SurfaceView implements Runnable {

    Thread gameThread;
    boolean running = false;
    SurfaceHolder holder;
    Paint pincel;

    public MiVista(Context context) {
        super(context);

        holder = getHolder();
        pincel = new Paint();
        pincel.setColor(Color.RED);
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
}

