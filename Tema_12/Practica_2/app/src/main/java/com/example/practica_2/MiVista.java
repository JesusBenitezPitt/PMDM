package com.example.practica_2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.practica_2.ObjetoMovil;

import java.util.ArrayList;
import java.util.List;

public class MiVista extends SurfaceView implements Runnable {

    private Thread gameThread;
    private volatile boolean running = false;
    private SurfaceHolder holder;
    private Context context;
    private List<ObjetoMovil> listaObjetos;
    private int numeroDeObjetos = 5;
    private int anchoPantalla, altoPantalla;
    private boolean inicializado = false;
    private Paint pincelTexto;

    public MiVista(Context context) {
        super(context);
        this.context = context;
        holder = getHolder();
        listaObjetos = new ArrayList<>();

        pincelTexto = new Paint();
        pincelTexto.setColor(Color.BLACK);
        pincelTexto.setTextSize(40);
        pincelTexto.setAntiAlias(true);
    }

    private void inicializarObjetos() {
        anchoPantalla = getWidth();
        altoPantalla = getHeight();

        for (int i = 0; i < numeroDeObjetos; i++) {
            listaObjetos.add(new ObjetoMovil(context, anchoPantalla, altoPantalla));
        }
        inicializado = true;
    }

    @Override
    public void run() {
        while (running) {
            if (!holder.getSurface().isValid()) {
                continue;
            }

            if (!inicializado) {
                inicializarObjetos();
            }

            for (ObjetoMovil objeto : listaObjetos) {
                objeto.actualizar(anchoPantalla, altoPantalla);
            }

            dibujar();

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void dibujar() {
        Canvas canvas = holder.lockCanvas();
        if (canvas != null) {
            try {
                canvas.drawColor(Color.WHITE);
                for (ObjetoMovil objeto : listaObjetos) {
                    objeto.dibujar(canvas);
                }

            } finally {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float xToque = event.getX();
            float yToque = event.getY();

            for (int i = listaObjetos.size() - 1; i >= 0; i--) {
                ObjetoMovil objeto = listaObjetos.get(i);
                if (objeto.verificarToque(xToque, yToque)) {
                    objeto.reaccionarAlToque();
                }
            }
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