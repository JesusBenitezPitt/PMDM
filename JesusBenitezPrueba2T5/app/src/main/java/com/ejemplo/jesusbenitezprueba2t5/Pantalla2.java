package com.ejemplo.jesusbenitezprueba2t5;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Pantalla2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla2);

        ImageView imagen = findViewById(R.id.imagen1);

        Animation animacion1 = AnimationUtils.loadAnimation(this, R.anim.movimiento);
        imagen.setAnimation(animacion1);
    }
}