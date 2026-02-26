package com.ejemplo.examen;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.WindowMetrics;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ejemplo.examen.R;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    static final double SHAKE_THRESHOLD = 12.0;
    long lastShakeTime = 0;
    int contador = 0;
    double x = 0, y = 0, z = 0, a = 0, amax = 0;
    double gravedad = SensorManager.STANDARD_GRAVITY;
    TextView tvax, tvay, tvaz, tva, tvaMax, tvG;
    ImageView balon;
    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable updateTask;
    SensorManager sensorManager;
    Sensor accelerometro;
    int screenWidth, screenHeight;
    Vibrator vibrador;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvax = (TextView) findViewById(R.id.textViewAX);
        tvay = (TextView) findViewById(R.id.textViewAY);
        tvaz = (TextView) findViewById(R.id.textViewAZ);
        tva = (TextView) findViewById(R.id.textViewA);
        tvaMax = (TextView) findViewById(R.id.textViewAmax);
        tvG = (TextView) findViewById(R.id.textViewG);
        balon = (ImageView) findViewById(R.id.balon);
        // inicia un SensorManager y el vibrator para que el movil vibre.
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        vibrador = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // define un sensor acelerometro
        accelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            // Para Android 11 (API 30) en adelante
            WindowMetrics windowMetrics = getWindowManager().getCurrentWindowMetrics();
            Rect bounds = windowMetrics.getBounds();
            screenWidth = bounds.width();
            screenHeight = bounds.height();
        }

        setupUpdateTask();
    }

    @Override
    public void onResume(){
        super.onResume();
        // Comprobamos si la variable de accelerometro no es null.
        if (accelerometro != null){
            // Registra el sensor para que comience a escuchar cuando la pantalla esta en el foco.
            sensorManager.registerListener(this, accelerometro, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        // Desregistramos el sensor cuando la app pasa a un plano secundario.
        sensorManager.unregisterListener(this);
        /**
         * Esto se hace así ya que se logra ahorrar batería, se liberan recursos y evita fugas de memoria.
         * Prefiero hacerlo en el onPause para que la aplicación desregistre el sensor cuando no esta en primer plano.
         */
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        // componentes de la aceleracion
        x = event.values[0];
        y = event.values[1];
        z = event.values[2];
        // modulo de la aceleracion
        a = Math.sqrt(x * x + y * y + z * z);
        // aceleracion maxima
        if (a > amax) amax = a;

        // Deteccion de una sacudida.
        if (a > SHAKE_THRESHOLD){
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastShakeTime > 1000) {
                lastShakeTime = currentTime;
                Toast.makeText(this, "Se ha detectado una sacudida.", Toast.LENGTH_SHORT).show();
            }
        }

        // 2. Detección de inclinación horizontal (eje x).
        if (x < -3.0) {
            Log.d("SENSOR", "Inclinación: DERECHA");
        } else if (x > 3.0) {
            Log.d("SENSOR", "Inclinación: IZQUIERDA");
        }

        // 2. Detección de inclinación vertical (eje y).
        if (y < -3.0) {
            Log.d("SENSOR", "Inclinación: HACIA ARRIBA");
        } else if (y > 3.0) {
            Log.d("SENSOR", "Inclinación: HACIA ABAJO");
        }

        // 3. Detección de cualquier movimiento significativo.
        if (Math.abs(x) > 1.0 || Math.abs(y) > 1.0 || Math.abs(z - 9.8) > 1.0) {
            Log.d("SENSOR", "Estado: EN MOVIMIENTO");
        }

        // 4. Detección de una sacudida.
        if (a > SHAKE_THRESHOLD) {
            Log.d("SENSOR", "Se ha detectado una sacudida.");
        }
    }

    private void setupUpdateTask() {
        updateTask = new Runnable() {
            @Override
            public void run() {
                // Lógica que antes estaba en onProgressUpdate
                tvax.setText(String.valueOf(x));
                tvay.setText(String.valueOf(y));
                tvaz.setText(String.valueOf(z));
                tva.setText(String.valueOf(a));
                tvaMax.setText(String.valueOf(amax));
                tvG.setText(gravedad + "\nCONTADOR: " + contador);

                moverBalon();

                contador++;

                // En lugar de while(true), reprogramamos la tarea cada 100ms
                // Esto es mucho más eficiente y evita bloquear hilos
                mainHandler.postDelayed(this, 100);
            }
        };

        // Iniciamos la tarea por primera vez
        mainHandler.post(updateTask);
    }

    private void moverBalon() {
        SoundPool soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC,100);

        int sonido1 = soundPool.load(this, R.raw.efecto1, 1);

        float nuevaX = balon.getX() - (float) (x * 5);
        float nuevaY = balon.getY() + (float) (y * 5);

        if (nuevaX <= 0) {
            Log.d("Prueba", "Reproduciendo sonido, pelota fuera del rango visible.");
            soundPool.play(sonido1, 1, 1, 1, 0, 1);
        } else if (nuevaX >= screenWidth - balon.getWidth()) {
            Log.d("Prueba", "Reproduciendo sonido, pelota fuera del rango visible.");
            soundPool.play(sonido1, 1, 1, 1, 0, 1);
        }

        if (nuevaY <= 0) {
            Log.d("Prueba", "Reproduciendo sonido, pelota fuera del rango visible.");
            soundPool.play(sonido1, 1, 1, 1, 0, 1);
        } else if (nuevaY >= screenHeight - balon.getHeight()) {
            Log.d("Prueba", "Reproduciendo sonido, pelota fuera del rango visible.");
            soundPool.play(sonido1, 1, 1, 1, 0, 1);
        }

        balon.setX(nuevaX);
        balon.setY(nuevaY);

        if (a > SHAKE_THRESHOLD) {
            Log.d("Prueba", "Movil vibrando y pelota vuelta al punto original.");
            ejecutarVibracion(); // Vibración del móvil

            // Aplicamos un pequeño desplazamiento relativo (translation)
            // para dar un efecto visual de "temblor" o "rebote"
            balon.setTranslationX(50f);
            balon.setTranslationY(50f);

            // Volvemos a la traslación original tras 50ms para que parezca un golpe
            new Handler().postDelayed(() -> {
                balon.setTranslationX(0);
                balon.setTranslationY(0);
            }, 50);
        }
    }

    private void ejecutarVibracion() {
        if (vibrador != null && vibrador.hasVibrator()) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrador.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrador.vibrate(50);
            }
        }
    }
}