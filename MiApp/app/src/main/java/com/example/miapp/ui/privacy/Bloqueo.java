package com.example.miapp.ui.privacy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.miapp.R;
import com.example.miapp.ui.main.Main;

import java.util.concurrent.Executor;

public class Bloqueo extends AppCompatActivity {

    private int userId;
    private SharedPreferences session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloqueo);

        getOnBackPressedDispatcher().addCallback(this, new androidx.activity.OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {}
        });

        session = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        userId = getIntent().getIntExtra("userId", -1);

        Button botonDesbloquear = findViewById(R.id.botonDesbloquear);
        botonDesbloquear.setOnClickListener(v -> mostrarHuella());

        mostrarHuella();
    }

    private void mostrarHuella() {
        Executor executor = ContextCompat.getMainExecutor(this);

        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        irAMain();
                    }

                    @Override
                    public void onAuthenticationError(int errorCode, CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                    }
                });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Pantalla de privacidad")
                .setSubtitle("Usa tu huella o método de bloqueo del teléfono para continuar")
                .setAllowedAuthenticators(
                        androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG |
                                androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
                )
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    private void irAMain() {
        Intent intent = new Intent(Bloqueo.this, Main.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
        finish();
    }

}