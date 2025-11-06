package com.example.miapp.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.miapp.R;

public class Register extends AppCompatActivity {

    private EditText usuario;
    private EditText passwd;
    private EditText confirmPasswd;
    private Button boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usuario = findViewById(R.id.usuario);
        passwd = findViewById(R.id.passwd);
        confirmPasswd = findViewById(R.id.confirmPasswd);
        boton = findViewById(R.id.boton);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comprobarContraseñas(passwd.getText().toString(), confirmPasswd.getText().toString())) {
                    finish();
                }
            }
        });
    }

    @Override
    public void finish(){
        Intent intent = new Intent();
        intent.putExtra("user", usuario.getText().toString());
        intent.putExtra("passwd", passwd.getText().toString());
        setResult(RESULT_OK, intent);
        super.finish();
    }

    private boolean comprobarContraseñas(String p1, String p2){
        return p1.equals(p2);
    }
}