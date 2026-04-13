package com.ejemplo.bloque_2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {
    Thread gameThread;
    boolean running;
    SurfaceHolder holder;
    Sprite playerSprite;
    Sprite enemySprite;
    Bitmap playerSheet;
    Bitmap enemySheet;

    int currentFrame = 0;
    int playerX = 0;
    int playerFrameWidth;
    int playerFrameHeight;
    int playerColumns = 8;
    int direccion = 1;
    int velocidad = 15;

    public GameView(Context context) {
        super(context);
        holder = getHolder();

        playerSheet = BitmapFactory.decodeResource(getResources(), R.drawable.player_sprites);
        enemySheet = BitmapFactory.decodeResource(getResources(), R.drawable.enemy_sprites);

        playerFrameWidth = playerSheet.getWidth() / playerColumns;
        playerFrameHeight = playerSheet.getHeight();

        Rect playerSrc = new Rect(0, 0, playerFrameWidth, playerFrameHeight);

        // Ajuste para el enemigo
        int enemyFrameWidth = enemySheet.getWidth() / 4;
        int enemyFrameHeight = enemySheet.getHeight() / 3;
        Rect enemySrc = new Rect(0, 0, enemyFrameWidth, enemyFrameHeight);

        // He bajado el tamaño de dibujado a 300 para que se vea mejor el caballero
        playerSprite = new Sprite(playerSheet, playerSrc, playerX, 400, 300, 300);
        enemySprite = new Sprite(enemySheet, enemySrc, 600, 500, 150, 150);
    }

    @Override
    public void run() {
        while (running) {
            if (!holder.getSurface().isValid()) continue;

            // Movimiento
            playerX += (velocidad * direccion);

            int screenWidth = getWidth();
            if (playerX + 300 > screenWidth) {
                direccion = -1;
                playerX = screenWidth - 300;
            } else if (playerX < 0) {
                direccion = 1;
                playerX = 0;
            }

            // Animación
            currentFrame = (currentFrame + 1) % playerColumns;
            int left = currentFrame * playerFrameWidth;
            Rect newSrc = new Rect(left, 0, left + playerFrameWidth, playerFrameHeight);

            playerSprite.update(playerX, newSrc, direccion == 1);

            Canvas canvas = holder.lockCanvas();
            if (canvas != null) {
                canvas.drawColor(Color.WHITE);
                playerSprite.draw(canvas);
                enemySprite.draw(canvas);
                holder.unlockCanvasAndPost(canvas);
            }

            try {
                Thread.sleep(100);
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