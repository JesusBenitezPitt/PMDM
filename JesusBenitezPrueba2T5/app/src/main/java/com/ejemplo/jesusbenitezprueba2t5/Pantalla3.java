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

public class Pantalla3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla3);

        ImageView imagen = findViewById(R.id.imagen2);

        Animation animacion2 = AnimationUtils.loadAnimation(this, R.anim.aparicion);
        imagen.setAnimation(animacion2);
    }
}