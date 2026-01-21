package com.example.actividad_4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MiVista(this));
    }

    private class MiVista extends View {
        public MiVista(Context ctx) {
            super(ctx);
        }

        @Override
        public void onDraw(Canvas canvas){
            String apellido = "Benitez";
            Paint pincel = new Paint();
            pincel.setColor(Color.RED);
            pincel.setTextSize(80);
            pincel.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(apellido, (float) getWidth() / 2, 150, pincel);

            canvas.save();
            canvas.rotate(-15, 100, 300);
            pincel.setColor(Color.BLUE);
            pincel.setTextSize(40);
            pincel.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(apellido, 100, 300, pincel);
            canvas.restore();

            canvas.save();
            canvas.scale(1.5f, 1.5f, (float) getWidth() - 100, 450);
            pincel.setColor(Color.GREEN);
            pincel.setTextSize(60);
            pincel.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(apellido, (float) getWidth() - 100, 450, pincel);
            canvas.restore();

            canvas.save();
            canvas.skew(0.5f, 0);
            pincel.setColor(Color.MAGENTA);
            pincel.setTextSize(50);
            pincel.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(apellido, 100, 600, pincel);
            canvas.restore();

            canvas.save();
            pincel.setColor(Color.argb(127, 0, 255, 255));
            pincel.setTextSize(70);
            pincel.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(apellido, (float) getWidth() / 2, 750, pincel);
            canvas.restore();
        }
    }
}