package com.example.actividad_5;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.widget.SeekBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    private SeekBar seekbar1, seekbar2;
    private MaterialButton boton1, boton2;
    private int sonido1, sonido2;
    private SoundPool soundPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekbar1 = findViewById(R.id.seekbar_1);
        seekbar2 = findViewById(R.id.seekbar_2);

        boton1 = findViewById(R.id.boton_1);
        boton2 = findViewById(R.id.boton_2);

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC,100);

        sonido1 = soundPool.load(this, R.raw.efecto1, 1);
        sonido2 = soundPool.load(this, R.raw.efecto2, 1);

        boton1.setOnClickListener(v -> {
            soundPool.play(sonido1,((float) seekbar1.getProgress() /200), ((float) seekbar1.getProgress() /200), 1,0, ((float) seekbar2.getProgress() /100));
        });

        boton2.setOnClickListener(v -> {
            soundPool.play(sonido2,((float) seekbar1.getProgress() /200), ((float) seekbar1.getProgress() /200), 1,0, ((float) seekbar2.getProgress() /100));
        });

    }
}