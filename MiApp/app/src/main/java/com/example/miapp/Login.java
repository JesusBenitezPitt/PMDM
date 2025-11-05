package com.example.miapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText usuario = (EditText) findViewById(R.id.usuario);
        EditText passwd = (EditText) findViewById(R.id.passwd);
        Button login = (Button) findViewById(R.id.botonLogin);
        Button registrar = (Button) findViewById(R.id.botonRegistrar);

        List<Usuario> lista_usuarios = new ArrayList<>();
        lista_usuarios.add(new Usuario("Usuario1", "1234"));
        lista_usuarios.add(new Usuario("Usuario2", "5678"));

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lista_usuarios.contains(new Usuario(usuario.getText().toString(), passwd.getText().toString()))) {
                    Intent pantalla2 = new Intent(Login.this, Main.class);
                    startActivity(pantalla2);
                } else {
                    Toast.makeText(Login.this, "El usuario o la contraseña son incorrectos.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Register.class);
            }
        });

    }
}