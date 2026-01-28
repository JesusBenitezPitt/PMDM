package com.example.actividad_6;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ImageButton grabar, parar, reproducir;
    private MediaRecorder mediaRecorder;
    private String audioFilePath;
    private MediaPlayer mediaPlayer;
    private static final int REQUEST_RECORD_AUDIO = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grabar = findViewById(R.id.grabar);
        parar = findViewById(R.id.parar);
        reproducir = findViewById(R.id.reproducir);

        grabar.setOnClickListener(v -> {
            if (tienePermisoMicrofono()) {
                audioFilePath = getExternalFilesDir(Environment.DIRECTORY_MUSIC) + "/audio.m4a";
                grabar();
            } else {
                pedirPermisoMicrofono();
            }
        });

        parar.setOnClickListener(v -> {
            pararGrabacion();
        });

        reproducir.setOnClickListener(v -> {
            reproducirGrabacion();
        });

    }

    private void grabar() {
        try {
            if (mediaRecorder != null) return;

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso de micrófono requerido", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Grabando audio...", Toast.LENGTH_SHORT).show();
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setOutputFile(audioFilePath);
            mediaRecorder.prepare();
            mediaRecorder.start();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al iniciar grabación", Toast.LENGTH_SHORT).show();
        }
    }

    private void pararGrabacion() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    private void reproducirGrabacion() {
        try {
            mediaPlayer = MediaPlayer.create(this, Uri.fromFile(new File(audioFilePath)));
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean tienePermisoMicrofono() {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void pedirPermisoMicrofono() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                REQUEST_RECORD_AUDIO
        );
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show();

                audioFilePath = getExternalFilesDir(Environment.DIRECTORY_MUSIC) + "/audio.m4a";
                grabar();

            } else {
                Toast.makeText(this, "Permiso de micrófono denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}