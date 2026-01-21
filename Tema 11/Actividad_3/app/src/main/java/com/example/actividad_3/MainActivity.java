package com.example.actividad_3;

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
        View miVista = new VistaPropia(this);
        setContentView(miVista);

    }

    private class VistaPropia extends View {
        public VistaPropia (Context ctx) {
            super(ctx);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            // Actividad 11.3
            Paint pincel1 = new Paint();
            Paint pincel2 = new Paint();
            Paint pincel3 = new Paint();
            pincel1.setColor(Color.RED);
            pincel1.setStrokeWidth(20);
            pincel1.setStyle(Paint.Style.STROKE);
            pincel2.setColor(Color.BLUE);
            pincel2.setStrokeWidth(20);
            pincel2.setStyle(Paint.Style.STROKE);
            pincel3.setColor(Color.BLACK);
            pincel3.setStrokeWidth(20);
            pincel3.setStyle(Paint.Style.STROKE);

            canvas.drawOval(100, 200, 1000, 650, pincel2);
            canvas.drawRect(100, 200, 1000, 650, pincel1);
            canvas.drawCircle(550, 430, 150, pincel3);

            Paint pincel = new Paint();
            pincel.setColor(Color.RED);
            pincel.setStrokeWidth(20);
            pincel.setStyle(Paint.Style.STROKE);

            canvas.drawRect(100, 750, 300, 950, pincel);
            pincel.setStyle(Paint.Style.FILL);
            canvas.drawRect(450, 750, 650, 950, pincel);
            pincel.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawRect(800, 750, 1000, 950, pincel);


        }
    }
}