package com.example.miapp.ui.ajustes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricManager;
import androidx.core.content.ContextCompat;

import com.example.miapp.R;

public class Ajustes extends AppCompatActivity {

    private Switch switchPrivacidad;
    private SharedPreferences session;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        Toolbar toolbar = findViewById(R.id.toolbar_ajustes);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Ajustes");
        }

        session = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        userId = getIntent().getIntExtra("userId", -1);

        switchPrivacidad = findViewById(R.id.switch_privacidad);
        switchPrivacidad.setChecked(session.getBoolean("privacidad_" + userId, false));

        switchPrivacidad.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                BiometricManager biometricManager = BiometricManager.from(this);
                int resultado = biometricManager.canAuthenticate(
                        BiometricManager.Authenticators.BIOMETRIC_STRONG |
                                BiometricManager.Authenticators.DEVICE_CREDENTIAL
                );

                if (resultado != BiometricManager.BIOMETRIC_SUCCESS) {
                    Toast.makeText(this,
                            "No tienes ningún método de bloqueo configurado en el dispositivo.",
                            Toast.LENGTH_LONG).show();
                    switchPrivacidad.setChecked(false);
                    return;
                }

                session.edit().putBoolean("privacidad_" + userId, true).apply();
                Toast.makeText(this, "Pantalla de privacidad activada.", Toast.LENGTH_SHORT).show();

            } else {
                session.edit().putBoolean("privacidad_" + userId, false).apply();
                Toast.makeText(this, "Pantalla de privacidad desactivada.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}