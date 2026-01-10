package com.example.miapp.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miapp.R;
import com.example.miapp.model.Usuario;
import com.example.miapp.ui.main.Main;
import com.example.miapp.utils.Encriptacion;

import com.google.gson.Gson;

public class Login extends AppCompatActivity {

    private static final int REQUEST_REGISTRAR = 2;

    private EditText usuarioField, passwdField;
    private Button loginButton, registrarButton;
    protected SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        initPrefs();
        initListeners();
    }

    private void initViews() {
        usuarioField = findViewById(R.id.usuario);
        passwdField = findViewById(R.id.passwd);
        loginButton = findViewById(R.id.botonLogin);
        registrarButton = findViewById(R.id.botonRegistrar);
    }

    private void initPrefs() {
        prefs = getSharedPreferences("usuarios", Context.MODE_PRIVATE);

        if (!prefs.contains("inicializado")) {
            SharedPreferences.Editor editor = prefs.edit();

            Usuario u1 = new Usuario(Encriptacion.sha256("Usuario1"), new Usuario.Datos(Encriptacion.sha256("1234"), 1));
            Usuario u2 = new Usuario(Encriptacion.sha256("Usuario2"), new Usuario.Datos(Encriptacion.sha256("4567"), 2));

            Gson gson = new Gson();
            String u1_json = gson.toJson(u1);
            String u2_json = gson.toJson(u2);

            editor.putString(Encriptacion.sha256("Usuario1"), u1_json);
            editor.putString(Encriptacion.sha256("Usuario2"), u2_json);
            editor.apply();
        }
    }

    private void initListeners() {

        loginButton.setOnClickListener(v -> {
            if (intentarLogin()) return;
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
        Log.d("Prueba", " " + valor);
        if (valor != null) {
            Gson gson = new Gson();
            Usuario u = gson.fromJson(valor, Usuario.class);
            Log.d("Prueba", u.getDatos().getPasswd() + "    /     " + passwd);
            if (u.getDatos().getPasswd().equals(Encriptacion.sha256(passwd))) {
                Intent mainIntent = new Intent(Login.this, Main.class);
                mainIntent.putExtra("userId", u.getDatos().getId());
                startActivity(mainIntent);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_REGISTRAR && resultCode == RESULT_OK) {
            Toast.makeText(this, "Usuario creado exitosamente.", Toast.LENGTH_SHORT).show();
        }
    }
}