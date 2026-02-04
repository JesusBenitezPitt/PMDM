package com.example.actividad_10;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView sensoresInstalados = findViewById(R.id.sensores_instalados);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensores = sensorManager.getSensorList(Sensor.TYPE_ALL);
        String sensoresFormat = "";
        for (Sensor sensor : sensores){
            sensoresFormat += "Nombre: " + sensor.getName() + ", Fabricante: " + sensor.getVendor() + ", Versión: " + sensor.getVersion() + "\n";
        }

        sensoresInstalados.setText(sensoresFormat);
    }
}