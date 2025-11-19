package com.ejemplo.actividad_3;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        EditText text = findViewById(R.id.text);
        SeekBar seekBar = findViewById(R.id.seekBar);
        Button button = findViewById(R.id.button);

        button.setOnClickListener(v -> {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast));

            TextView texto = layout.findViewById(R.id.texto_toast);
            texto.setText(seekBar.getProgress() + "%");

            Toast toast = new Toast(MainActivity.this);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.setView(layout);
            toast.show();
        });

//        button.setOnClickListener(v -> {
//            Toast.makeText(MainActivity.this, text.getText().toString(), Toast.LENGTH_SHORT).show();
//        });
    }
}