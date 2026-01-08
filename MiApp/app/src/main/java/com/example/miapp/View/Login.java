package com.example.miapp.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miapp.Main;
import com.example.miapp.Model.Usuario;
import com.example.miapp.R;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    static SharedPreferences prefs;
    static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText usuario = (EditText) findViewById(R.id.usuario);
        EditText passwd = (EditText) findViewById(R.id.passwd);
        Button login = (Button) findViewById(R.id.botonLogin);
        Button registrar = (Button) findViewById(R.id.botonRegistrar);

        prefs = getPreferences(Context.MODE_PRIVATE);

        editor = prefs.edit();
        editor.putString("Usuario1", "Usuario1,1234,1");
        editor.putString("Usuario2", "Usuario2,4567,2");
        editor.apply();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario usuario1 = new Usuario(usuario.getText().toString(), passwd.getText().toString());
                String valor = prefs.getString(usuario1.getName(), " ");
                Log.d("Prueba", valor);
                String[] datos = valor.split(",");
                Log.d("Prueba", datos[0]);
                Usuario usuario2 = new Usuario(datos[0], datos[1]);
                if (usuario1.equals(usuario2)) {
                    Intent pantalla2 = new Intent(Login.this, Main.class);
                    pantalla2.putExtra("userId", Integer.parseInt(datos[2]));
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
                startActivityForResult(i, 2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent i){
        super.onActivityResult(requestCode, resultCode, i);
        if (resultCode == RESULT_OK && requestCode == 2) {
            Toast.makeText(this, "Usuario creado exitosamente.", Toast.LENGTH_SHORT).show();
        }
    }
}