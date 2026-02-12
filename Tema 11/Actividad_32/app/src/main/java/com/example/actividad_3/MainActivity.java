package com.example.actividad_3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MiVista miVista = new MiVista(this);
        setContentView(miVista);
    }

    class MiVista extends View {
        float velocidad = 10f;
        int x, y;
        Paint pincel;
        int radio = 80;

        public MiVista(Context context) {
            super(context);
            pincel = new Paint();
            pincel.setColor(Color.RED);
            x = 500;
            y = 100;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            y = y + (int) (velocidad);
            if (y + radio >= canvas.getHeight()) {
                velocidad = -Math.abs(velocidad);
            }
            if (y - radio <= 0) {
                velocidad = Math.abs(velocidad);
            }
            canvas.drawCircle(x, y, radio, pincel);
            invalidate();
        }
    }
}