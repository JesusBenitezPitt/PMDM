package com.example.actividad_7;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private MediaRecorder recorder;
    private MediaPlayer player;
    private Button record;
    private Button pause;
    private Button play;
    private String ruta;
    private boolean recording = false;
    private static final int PERMISSION_REQUEST_CODE = 1000;
    private final String[] permisos = {
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verificarPermisos();

        SurfaceView video = findViewById(R.id.surfaceView);
        video.getHolder().addCallback(this);
        video.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        record = (Button) findViewById(R.id.boton_grabar);
        record.setOnClickListener(v -> {
            try {
                recorder.reset();

                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);

                ruta = getExternalFilesDir(Environment.DIRECTORY_MOVIES) + "/miVideo.mp4";

                recorder.setOutputFile(ruta);
                recorder.setPreviewDisplay(((SurfaceView) findViewById(R.id.surfaceView)).getHolder().getSurface());

                recorder.prepare();
                recorder.start();

                recording = true;
                Toast.makeText(this, "Grabando...", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al grabar", Toast.LENGTH_LONG).show();
            }
        });


        pause = (Button) findViewById(R.id.boton_parar);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recording) {
                    recorder.stop();
                    recorder.reset();
                    recording = false;
                } else {
                    player.stop();
                    player.reset();
                    play.setText("PLAY");
                }
            }
        });

        play = (Button) findViewById(R.id.boton_reproducir);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!player.isPlaying()) {
                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            record.setEnabled(true);
                            pause.setEnabled(false);
                            play.setEnabled(true);
                        }
                    });

                    if (player.getCurrentPosition() == player.getDuration()) {
                        try {
                            player.setDataSource(ruta);
                            player.prepare();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    player.start();
                } else {
                    player.pause();
                }
            }
        });
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        if (recorder == null) {
            recorder = new MediaRecorder();
            recorder.setPreviewDisplay(holder.getSurface());
        }

        if (player == null) {
            player = new MediaPlayer();
            player.setDisplay(holder);
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        recorder.release();
        player.release();
    }

    // --- Gestión de Permisos ---
    private void verificarPermisos() {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permisos, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Se necesitan permisos para funcionar", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
