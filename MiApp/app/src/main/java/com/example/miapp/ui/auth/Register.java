package com.example.miapp.ui.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miapp.R;
import com.example.miapp.model.Usuario;
import com.example.miapp.utils.Encriptacion;

import com.google.gson.Gson;

public class Register extends AppCompatActivity {

    private EditText usuarioField, passwdField, confirmPasswdField;
    private Button registrarButton;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        initPrefs();
        initListeners();
    }

    private void initViews() {
        usuarioField = findViewById(R.id.usuario);
        passwdField = findViewById(R.id.passwd);
        confirmPasswdField = findViewById(R.id.confirmPasswd);
        registrarButton = findViewById(R.id.boton);
    }

    private void initPrefs() {
        prefs = getSharedPreferences("usuarios", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    private void initListeners() {
        registrarButton.setOnClickListener(v -> {
            String passwd = passwdField.getText().toString();
            String confirm = confirmPasswdField.getText().toString();

            if (!comprobarPasswd(passwd, confirm)) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            if (usuarioExistente(usuarioField.getText().toString())) {
                Toast.makeText(this, "El usuario ya existe.", Toast.LENGTH_SHORT).show();
                return;
            }

            registrarUsuario(usuarioField.getText().toString(), passwd);
            setResult(RESULT_OK);
            finish();
        });
    }

    private boolean comprobarPasswd(String p1, String p2) {
        return p1.equals(p2);
    }

    private boolean usuarioExistente(String usuario) {
        String valor = prefs.getString(Encriptacion.sha256(usuario), null);
        return valor != null;
    }

    private void registrarUsuario(String usuario, String passwd) {
        int nextId = prefs.getInt("nextUserId", 3);

        Usuario u = new Usuario(Encriptacion.sha256(usuario), new Usuario.Datos(Encriptacion.sha256(passwd), nextId));

        Gson gson = new Gson();
        String datos_json = gson.toJson(u);

        editor.putString(Encriptacion.sha256(usuario), datos_json);
        editor.putInt("nextUserId", nextId + 1);
        editor.apply();
    }
}