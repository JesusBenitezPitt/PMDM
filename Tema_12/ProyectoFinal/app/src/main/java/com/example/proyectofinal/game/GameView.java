package com.example.proyectofinal.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.proyectofinal.utils.SoundManager;

import java.util.List;

// Vista principal del juego. Contiene el hilo del bucle de juego y todo el renderizado.
// La lógica reside en GameEngine; esta clase solo dibuja el estado actual.
public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private static final long MS_POR_FRAME = 1000L / 60L; // objetivo ~60 fps

    private GameEngine   motor;
    private SoundManager sonido;

    private Thread           hilo;
    private volatile boolean ejecutando = false;

    // Posición y tamaño del laberinto en pantalla (calculado al cambiar dimensiones)
    private float tamCelda;
    private float desplX, desplY;

    // Pinceles reutilizables (no se crean en cada frame)
    private final Paint pincelMuro      = new Paint();
    private final Paint pincelCamino    = new Paint();
    private final Paint pincelSalida    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pincelHudFondo  = new Paint();
    private final Paint pincelHudTexto  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pincelOverlay   = new Paint();
    private final Paint pincelTitulo    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pincelSubtitulo = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pincelBarraFondo= new Paint();
    private final Paint pincelBarraRell = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pincelGracia    = new Paint(Paint.ANTI_ALIAS_FLAG);

    // Interfaz para notificar a la Activity cuando termina la partida
    public interface GameViewListener {
        void onGameOver(int puntuacion, int nivel);
    }

    private GameViewListener oyente;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
        inicializarPinceles();
    }

    // Constructor necesario cuando se infla desde XML
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setFocusable(true);
        inicializarPinceles();
    }

    private void inicializarPinceles() {
        pincelMuro.setColor(0xFF1A237E);
        pincelCamino.setColor(0xFF0D0D1E);

        pincelSalida.setColor(0xFF00E676);
        pincelSalida.setStyle(Paint.Style.FILL);

        pincelHudFondo.setColor(0xCC000011);

        pincelHudTexto.setColor(0xFFFFFFFF);
        pincelHudTexto.setTextAlign(Paint.Align.LEFT);
        pincelHudTexto.setFakeBoldText(true);

        pincelOverlay.setColor(0xCC000000);

        pincelTitulo.setColor(0xFFFFFFFF);
        pincelTitulo.setTextAlign(Paint.Align.CENTER);
        pincelTitulo.setFakeBoldText(true);

        pincelSubtitulo.setColor(0xFFAAAAAA);
        pincelSubtitulo.setTextAlign(Paint.Align.CENTER);

        pincelBarraFondo.setColor(0xFF333333);
        pincelBarraRell.setColor(0xFF00E5FF);
        pincelBarraRell.setAntiAlias(true);

        pincelGracia.setStyle(Paint.Style.STROKE);
        pincelGracia.setColor(0xFF69F0AE);
        pincelGracia.setAntiAlias(true);
    }

    // Vincula el motor, el gestor de sonido y el oyente de fin de partida.
    public void init(GameEngine motor, SoundManager sonido, GameViewListener oyente) {
        this.motor  = motor;
        this.sonido = sonido;
        this.oyente = oyente;
        // Cargar sprites una sola vez (necesitan Context)
        Player.cargarSprite(getContext());
        Enemy.cargarWalkSheet(getContext());
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        calcularLayout();
        ejecutando = true;
        hilo = new Thread(this);
        hilo.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int ancho, int alto) {
        calcularLayout();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        ejecutando = false;
        try { hilo.join(); } catch (InterruptedException ignorado) {}
    }

    // Calcula el tamaño de celda y el desplazamiento para centrar el laberinto.
    private void calcularLayout() {
        if (getWidth() == 0 || getHeight() == 0) return;

        float anchoDisp = getWidth();
        float altoDisp  = getHeight();

        float tsPorAncho = anchoDisp / GameConstants.COLUMNAS_LABERINTO;
        float tsPorAlto  = altoDisp  / GameConstants.FILAS_LABERINTO;
        tamCelda = Math.min(tsPorAncho, tsPorAlto);

        desplX = (anchoDisp - tamCelda * GameConstants.COLUMNAS_LABERINTO) / 2f;
        desplY = (altoDisp  - tamCelda * GameConstants.FILAS_LABERINTO)    / 2f;
    }

    // Bucle principal: actualiza la lógica y dibuja a ~60 fps.
    @Override
    public void run() {
        long ultimoTiempo = System.currentTimeMillis();

        while (ejecutando) {
            long ahora = System.currentTimeMillis();
            long delta = ahora - ultimoTiempo;
            ultimoTiempo = ahora;
            if (delta > 100) delta = 100; // evitar saltos grandes al volver de segundo plano

            if (motor != null) {
                motor.update(delta);

                if (motor.getState() == GameEngine.State.GAME_OVER) {
                    if (oyente != null) {
                        oyente.onGameOver(motor.getScore(), motor.getLevelNumber());
                    }
                    ejecutando = false;
                    return;
                }
            }

            dibujar();

            long tiempoFrame = System.currentTimeMillis() - ahora;
            long espera = MS_POR_FRAME - tiempoFrame;
            if (espera > 0) {
                try { Thread.sleep(espera); } catch (InterruptedException ignorado) {}
            }
        }
    }

    // Punto de entrada del renderizado por frame.
    private void dibujar() {
        Canvas canvas = getHolder().lockCanvas();
        if (canvas == null) return;
        try {
            canvas.drawColor(Color.BLACK);
            if (motor == null || tamCelda == 0) return;

            dibujarLaberinto(canvas);
            dibujarSalida(canvas);
            dibujarMonedas(canvas);
            dibujarPowerUps(canvas);
            dibujarTrampas(canvas);
            dibujarEnemigos(canvas);
            dibujarJugador(canvas);
            dibujarHUD(canvas);
            dibujarOverlay(canvas);
        } finally {
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void dibujarLaberinto(Canvas canvas) {
        int[][] lab = motor.getMaze();
        for (int f = 0; f < lab.length; f++) {
            for (int c = 0; c < lab[0].length; c++) {
                float izq = desplX + c * tamCelda;
                float arr = desplY + f * tamCelda;
                Paint p = (lab[f][c] == GameConstants.MURO) ? pincelMuro : pincelCamino;
                canvas.drawRect(izq, arr, izq + tamCelda, arr + tamCelda, p);
            }
        }
    }

    private void dibujarSalida(Canvas canvas) {
        float ef  = motor.getExitRow();
        float ec  = motor.getExitCol();
        float izq = desplX + ec * tamCelda;
        float arr = desplY + ef * tamCelda;

        // Efecto de pulsación con seno del tiempo
        pincelSalida.setAlpha(180 + (int)(60 * Math.sin(System.currentTimeMillis() / 300.0)));
        canvas.drawRect(izq, arr, izq + tamCelda, arr + tamCelda, pincelSalida);

        // Flecha que indica la dirección de salida
        pincelSalida.setAlpha(255);
        pincelSalida.setStyle(Paint.Style.STROKE);
        pincelSalida.setStrokeWidth(tamCelda * 0.1f);
        float cx = izq + tamCelda / 2f;
        float cy = arr + tamCelda / 2f;
        float s  = tamCelda * 0.25f;
        canvas.drawLine(cx - s, cy - s, cx + s, cy + s, pincelSalida);
        canvas.drawLine(cx, cy + s, cx + s, cy + s, pincelSalida);
        canvas.drawLine(cx + s, cy, cx + s, cy + s, pincelSalida);
        pincelSalida.setStyle(Paint.Style.FILL);
    }

    private void dibujarMonedas(Canvas canvas) {
        for (Collectible m : motor.getCollectibles()) {
            m.draw(canvas, desplX, desplY, tamCelda);
        }
    }

    private void dibujarPowerUps(Canvas canvas) {
        for (PowerUp p : motor.getPowerUps()) {
            p.draw(canvas, desplX, desplY, tamCelda);
        }
    }

    private void dibujarTrampas(Canvas canvas) {
        for (Trampa t : motor.getTrampas()) {
            t.dibujar(canvas, desplX, desplY, tamCelda);
        }
    }

    private void dibujarEnemigos(Canvas canvas) {
        for (Enemy e : motor.getEnemies()) {
            e.draw(canvas, desplX, desplY, tamCelda);
        }
    }

    private void dibujarJugador(Canvas canvas) {
        motor.getPlayer().draw(canvas, desplX, desplY, tamCelda);

        // Aura verde parpadeante durante el periodo de gracia
        long gracia = motor.getGraceTimer();
        if (gracia > 0) {
            float pulso = (float)(0.6 + 0.4 * Math.sin(System.currentTimeMillis() / 120.0));
            pincelGracia.setStrokeWidth(tamCelda * 0.09f);
            pincelGracia.setAlpha((int)(200 * pulso));
            float cx = desplX + motor.getPlayer().getPixelCol() * tamCelda + tamCelda / 2f;
            float cy = desplY + motor.getPlayer().getPixelRow() * tamCelda + tamCelda / 2f;
            float r  = tamCelda * 0.52f;
            canvas.drawCircle(cx, cy, r, pincelGracia);
        }
    }

    // HUD: puntuación, nivel y vidas en la banda superior.
    private void dibujarHUD(Canvas canvas) {
        float altHUD  = tamCelda * 1.1f;
        canvas.drawRect(0, 0, getWidth(), altHUD, pincelHudFondo);

        float tamTexto = tamCelda * 0.5f;
        pincelHudTexto.setTextSize(tamTexto);

        float padX = tamCelda * 0.4f;
        float baseY = altHUD * 0.72f;

        // Puntuación (izquierda)
        pincelHudTexto.setTextAlign(Paint.Align.LEFT);
        pincelHudTexto.setColor(0xFFFFD600);
        canvas.drawText("★ " + motor.getScore(), padX, baseY, pincelHudTexto);

        // Nivel (centro)
        pincelHudTexto.setTextAlign(Paint.Align.CENTER);
        pincelHudTexto.setColor(0xFFFFFFFF);
        canvas.drawText("Nivel " + motor.getLevelNumber(), getWidth() / 2f, baseY, pincelHudTexto);

        // Vidas (derecha en corazones)
        pincelHudTexto.setTextAlign(Paint.Align.RIGHT);
        pincelHudTexto.setColor(0xFFFF6D6D);
        StringBuilder corazones = new StringBuilder();
        for (int i = 0; i < motor.getLives(); i++) corazones.append("♥");
        canvas.drawText(corazones.toString(), getWidth() - padX * 0.5f, baseY, pincelHudTexto);
        pincelHudTexto.setTextAlign(Paint.Align.LEFT);

        dibujarBarrasPowerUps(canvas, altHUD);
    }

    // Barras de progreso para los power-ups activos.
    private void dibujarBarrasPowerUps(Canvas canvas, float inicioY) {
        float altBarra = tamCelda * 0.22f;
        float anchoBarra = getWidth() * 0.3f;
        float y   = inicioY + tamCelda * 0.1f;
        float pad = tamCelda * 0.15f;
        float x   = pad;

        x = dibujarBarra(canvas, "⚡", motor.getSpeedTimer(),  0xFFFFFF00, x, y, anchoBarra, altBarra, pad);
        x = dibujarBarra(canvas, "S",  motor.getShieldTimer(), 0xFF448AFF, x, y, anchoBarra, altBarra, pad);
            dibujarBarra(canvas, "⏱", motor.getSlowTimer(),   0xFFEA80FC, x, y, anchoBarra, altBarra, pad);
    }

    private float dibujarBarra(Canvas canvas, String icono, long timer,
                                int color, float x, float y,
                                float anchoBarra, float altBarra, float pad) {
        if (timer <= 0) return x;

        float relleno = (float) timer / GameConstants.DURACION_POWERUP_MS;
        RectF rectFondo = new RectF(x, y, x + anchoBarra, y + altBarra);
        RectF rectRell  = new RectF(x, y, x + anchoBarra * relleno, y + altBarra);
        float radio = altBarra / 2f;

        canvas.drawRoundRect(rectFondo, radio, radio, pincelBarraFondo);
        pincelBarraRell.setColor(color);
        canvas.drawRoundRect(rectRell, radio, radio, pincelBarraRell);

        Paint pincelIcono = new Paint(Paint.ANTI_ALIAS_FLAG);
        pincelIcono.setColor(0xFFFFFFFF);
        pincelIcono.setTextSize(altBarra * 1.1f);
        canvas.drawText(icono, x + pad * 0.3f, y + altBarra * 0.85f, pincelIcono);

        return x + anchoBarra + pad;
    }

    // Capas de overlay: pausa, nivel completado, muerte.
    private void dibujarOverlay(Canvas canvas) {
        GameEngine.State estado = motor.getState();

        switch (estado) {
            case PAUSED:
                dibujarOverlayCentrado(canvas,
                        "PAUSA", "Toca la pantalla para continuar",
                        0xFFFFFFFF, 0xFFAAAAAA);
                break;

            case LEVEL_COMPLETE: {
                float progreso = (float) motor.getLevelCompleteTimer()
                        / GameConstants.DURACION_NIVEL_COMPLETO_MS;
                pincelOverlay.setAlpha((int)(200 * Math.min(progreso * 3f, 1f)));
                canvas.drawRect(0, 0, getWidth(), getHeight(), pincelOverlay);
                pincelOverlay.setAlpha(200);

                float cx = getWidth() / 2f;
                float cy = getHeight() / 2f;

                pincelTitulo.setTextSize(tamCelda * 0.95f);
                pincelTitulo.setColor(0xFF00E676);
                canvas.drawText("¡NIVEL " + motor.getLevelNumber() + " COMPLETADO!",
                        cx, cy - tamCelda * 1.2f, pincelTitulo);

                pincelSubtitulo.setTextSize(tamCelda * 0.52f);
                pincelSubtitulo.setColor(0xFFFFD600);
                canvas.drawText("+" + (motor.getLevelNumber() * GameConstants.PUNTOS_NIVEL) + " puntos por nivel",
                        cx, cy - tamCelda * 0.4f, pincelSubtitulo);

                // Mostrar bonus por tiempo si lo hay
                if (motor.getBonusTiempo() > 0) {
                    pincelSubtitulo.setColor(0xFF00E5FF);
                    canvas.drawText("+" + motor.getBonusTiempo() + " bonus por velocidad",
                            cx, cy + tamCelda * 0.3f, pincelSubtitulo);
                }
                break;
            }

            case DYING:
                // Flash rojo al recibir daño
                pincelOverlay.setAlpha(80);
                pincelOverlay.setColor(0xFFFF0000);
                canvas.drawRect(0, 0, getWidth(), getHeight(), pincelOverlay);
                pincelOverlay.setColor(0xFF000000);
                break;

            default:
                break;
        }
    }

    private void dibujarOverlayCentrado(Canvas canvas,
                                        String titulo, String subtitulo,
                                        int colorTitulo, int colorSubtitulo) {
        pincelOverlay.setAlpha(200);
        canvas.drawRect(0, 0, getWidth(), getHeight(), pincelOverlay);

        pincelTitulo.setTextSize(tamCelda * 1.1f);
        pincelTitulo.setColor(colorTitulo);
        canvas.drawText(titulo, getWidth() / 2f, getHeight() / 2f - tamCelda * 0.5f, pincelTitulo);

        pincelSubtitulo.setTextSize(tamCelda * 0.5f);
        pincelSubtitulo.setColor(colorSubtitulo);
        canvas.drawText(subtitulo, getWidth() / 2f, getHeight() / 2f + tamCelda * 0.5f, pincelSubtitulo);
    }

    public void togglePause() {
        if (motor != null) motor.togglePause();
    }
}
