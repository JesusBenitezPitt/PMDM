package com.ejemplo.bloque_2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {
    Bitmap spriteSheet;
    Rect src;
    Rect dst;
    boolean facingRight = true;

    public Sprite(Bitmap spriteSheet, Rect src, int x, int y, int width, int height) {
        this.spriteSheet = spriteSheet;
        this.src = src;
        this.dst = new Rect(x, y, x + width, y + height);
    }

    public void update(int x, Rect newSrc, boolean facingRight) {
        this.src = newSrc;
        this.facingRight = facingRight;
        int width = dst.width();
        int height = dst.height();
        this.dst.set(x, dst.top, x + width, dst.top + height);
    }

    public void draw(Canvas canvas) {
        if (facingRight) {
            canvas.drawBitmap(spriteSheet, src, dst, null);
        } else {
            canvas.save();
            canvas.scale(-1, 1, dst.centerX(), dst.centerY());
            canvas.drawBitmap(spriteSheet, src, dst, null);
            canvas.restore();
        }
    }
}