package com.example.actividad_9;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private TextView textView_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView_info = findViewById(R.id.text_info);
        LinearLayout area = findViewById(R.id.layout_principal);

        area.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            v.performClick();
        }

        int action = event.getActionMasked();
        int punteros = event.getPointerCount();

        String info = "Acción: " + obtenerNombreAccion(action) + "\n";
        info += "Dedos detectados: " + punteros + "\n\n";

        for (int i = 0; i < punteros; i++) {
            int id = event.getPointerId(i);
            int x = (int) event.getX(i);
            int y = (int) event.getY(i);

            info += "Puntero ID " + id + " -> X: " + x + " Y: " + y + "\n";
        }

        textView_info.setText(info);

        return true;
    }

    private String obtenerNombreAccion(int action) {
        switch (action) {
            case MotionEvent.ACTION_DOWN: return "DOWN";
            case MotionEvent.ACTION_POINTER_DOWN: return "POINTER DOWN";
            case MotionEvent.ACTION_MOVE: return "MOVE";
            case MotionEvent.ACTION_UP: return "UP";
            case MotionEvent.ACTION_POINTER_UP: return "POINTER UP";
            default: return "OTRA";
        }
    }
}