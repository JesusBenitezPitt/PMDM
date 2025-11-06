package com.example.miapp.Controller;

import android.content.Intent;
import android.os.Bundle;
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

    private List<Usuario> lista_usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText usuario = (EditText) findViewById(R.id.usuario);
        EditText passwd = (EditText) findViewById(R.id.passwd);
        Button login = (Button) findViewById(R.id.botonLogin);
        Button registrar = (Button) findViewById(R.id.botonRegistrar);

        lista_usuarios = new ArrayList<>();
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
                startActivityForResult(i, 2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent i){
        super.onActivityResult(requestCode, resultCode, i);
        Bundle b = i.getExtras();
        if (b != null) {
            Usuario user = new Usuario(b.getString("user"), b.getString("passwd"));
            if (lista_usuarios.contains(user)){
                Toast.makeText(this, "El usuario ya existe.", Toast.LENGTH_SHORT).show();
            } else {
                lista_usuarios.add(user);
            }
        }
    }
}