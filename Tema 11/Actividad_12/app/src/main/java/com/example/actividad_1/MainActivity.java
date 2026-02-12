package com.example.actividad_1;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView img = findViewById(R.id.img);
        ObjectAnimator movimiento = ObjectAnimator.ofFloat(img, "translationX", 200f);
        movimiento.setDuration(1000);
        img.setOnClickListener(v -> {
            movimiento.start();
        });
    }
}