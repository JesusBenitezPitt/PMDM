package com.example.miapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class Informacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_informacion);

        EditText nombre = findViewById(R.id.nombre_empresa);
        EditText tipo = findViewById(R.id.tipo_auditoria);
        EditText fecha = findViewById(R.id.fecha);

        Button boton = findViewById(R.id.guardar);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Informacion.this, Main.class);
                intent.putExtra("nombre_empresa", nombre.getText().toString());
                intent.putExtra("tipo_auditoria", tipo.getText().toString());
                Bundle b = intent.getExtras();
                startActivity(intent, b);
                Toast.makeText(Informacion.this, "Se ha guardado la información correctamente", Toast.LENGTH_SHORT).show();
            }
        });

        Bundle b = getIntent().getExtras();
        assert b != null;
        nombre.setText(b.get("nombre_empresa").toString());
        tipo.setText(b.get("tipo_auditoria").toString());
        fecha.setText(b.get("fecha").toString());
    }
}