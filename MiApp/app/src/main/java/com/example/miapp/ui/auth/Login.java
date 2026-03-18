package com.example.miapp.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miapp.R;
import com.example.miapp.model.Usuario;
import com.example.miapp.ui.main.Main;
import com.example.miapp.ui.privacy.Bloqueo;
import com.example.miapp.utils.Encriptacion;

import com.google.gson.Gson;

import android.media.MediaPlayer;

public class Login extends AppCompatActivity {

    private static final int REQUEST_REGISTRAR = 2;

    private EditText usuarioField, passwdField;
    private Button loginButton, registrarButton;
    protected SharedPreferences prefs, session;
    private MediaPlayer mediaPlayer;
    private CheckBox checkBox_sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        startMusic();
        initViews();
        initPrefs();
        initListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMusic();
    }

    private void startMusic() {
        mediaPlayer = MediaPlayer.create(this, R.raw.musica);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    private void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void initViews() {
        usuarioField = findViewById(R.id.usuario);
        passwdField = findViewById(R.id.passwd);
        loginButton = findViewById(R.id.botonLogin);
        registrarButton = findViewById(R.id.botonRegistrar);
        checkBox_sesion = findViewById(R.id.checkbox_sesion);
    }

    private void initPrefs() {
        prefs = getSharedPreferences("usuarios", Context.MODE_PRIVATE);
        session = getSharedPreferences("UserSession", Context.MODE_PRIVATE);

        if (session.getBoolean("isLoggedIn", false)) {
            int userId = session.getInt("userId", -1);
            boolean privacidadActiva = session.getBoolean("privacidad_" + userId, false);

            if (privacidadActiva) {
                Intent intent = new Intent(Login.this, Bloqueo.class);
                intent.putExtra("userId", userId);
                stopMusic();
                startActivity(intent);
                finish();
            } else {
                irAMain(userId);
            }
            return;
        }

        if (!prefs.contains("inicializado")) {
            SharedPreferences.Editor editor = prefs.edit();

            Usuario u1 = new Usuario(Encriptacion.sha256("Usuario1"), new Usuario.Datos(Encriptacion.sha256("1234"), 1));
            Usuario u2 = new Usuario(Encriptacion.sha256("Usuario2"), new Usuario.Datos(Encriptacion.sha256("4567"), 2));
            Usuario u3 = new Usuario(Encriptacion.sha256("Jesus"), new Usuario.Datos(Encriptacion.sha256("1234"), 3));

            Gson gson = new Gson();
            editor.putString(Encriptacion.sha256("Usuario1"), gson.toJson(u1));
            editor.putString(Encriptacion.sha256("Usuario2"), gson.toJson(u2));
            editor.putString(Encriptacion.sha256("Jesus"), gson.toJson(u3));
            editor.putBoolean("inicializado", true);
            editor.apply();
        }
    }

    private void initListeners() {
        loginButton.setOnClickListener(v -> {
            if (intentarLogin()) {
                stopMusic();
                return;
            }
            Toast.makeText(Login.this, "El usuario o la contraseña son incorrectos.", Toast.LENGTH_SHORT).show();
        });

        registrarButton.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Register.class);
            startActivityForResult(intent, REQUEST_REGISTRAR);
        });
    }

    private boolean intentarLogin() {
        String usuario = usuarioField.getText().toString();
        String passwd = passwdField.getText().toString();

        String valor = prefs.getString(Encriptacion.sha256(usuario), null);
        if (valor != null) {
            Gson gson = new Gson();
            Usuario u = gson.fromJson(valor, Usuario.class);
            if (u.getDatos().getPasswd().equals(Encriptacion.sha256(passwd))) {
                if (checkBox_sesion.isChecked()) {
                    SharedPreferences.Editor editor = session.edit();
                    editor.putInt("userId", u.getDatos().getId());
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();
                }
                irAMain(u.getDatos().getId());
                return true;
            }
        }
        return false;
    }

    private void irAMain(int userId) {
        stopMusic();
        Intent mainIntent = new Intent(Login.this, Main.class);
        mainIntent.putExtra("userId", userId);
        startActivity(mainIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_REGISTRAR && resultCode == RESULT_OK) {
            Toast.makeText(this, "Usuario creado exitosamente.", Toast.LENGTH_SHORT).show();
        }
    }
}