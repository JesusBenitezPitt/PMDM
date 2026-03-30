package com.example.practica_2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.Random;

public class ObjetoMovil {

    private Bitmap imagen;
    private float posX, posY;
    private float velX, velY;
    private float ancho, alto;
    private RectF limitesObjeto;

    public ObjetoMovil(Context context, int anchoPantalla, int altoPantalla) {
        Bitmap imagenOriginal = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_sevillafc);

        int nuevoAncho = 200;
        int nuevoAlto = 200;

        this.imagen = Bitmap.createScaledBitmap(imagenOriginal, nuevoAncho, nuevoAlto, true);

        this.ancho = imagen.getWidth();
        this.alto = imagen.getHeight();

        if (imagenOriginal != this.imagen) {
            imagenOriginal.recycle();
        }

        limitesObjeto = new RectF();

        Random rand = new Random();
        this.posX = rand.nextInt(anchoPantalla - (int)this.ancho);
        this.posY = rand.nextInt(altoPantalla - (int)this.alto);

        this.velX = rand.nextFloat() * 30 - 15;
        this.velY = rand.nextFloat() * 30 - 15;
    }

    public void actualizar(int anchoPantalla, int altoPantalla) {
        posX += velX;
        posY += velY;

        if (posX <= 0 || posX + ancho >= anchoPantalla) {
            velX = -velX;
        }
        if (posY <= 0 || posY + alto >= altoPantalla) {
            velY = -velY;
        }

        limitesObjeto.set(posX, posY, posX + ancho, posY + alto);
    }

    public void dibujar(Canvas canvas) {
        canvas.drawBitmap(imagen, posX, posY, null);
    }

    public boolean verificarToque(float toqueX, float toqueY) {
        return limitesObjeto.contains(toqueX, toqueY);
    }

    public void reaccionarAlToque() {
        velX *= 1.2f;
        velY *= 1.2f;
    }
}