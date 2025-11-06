package com.example.miapp.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.miapp.R;

public class Informacion extends AppCompatActivity {

    private EditText nombre;
    private EditText tipo;
    private EditText fecha;
    private int posicion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_informacion);

        nombre = findViewById(R.id.nombre_empresa);
        tipo = findViewById(R.id.tipo_auditoria);
        fecha = findViewById(R.id.fecha);

        Bundle b = getIntent().getExtras();
        posicion = b.getInt("posicion");
        nombre.setText(b.getString("nombre_empresa"));
        tipo.setText(b.getString("tipo_auditoria"));
        fecha.setText(b.getString("fecha"));

        Button boton = findViewById(R.id.guardar);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    @Override
    public void finish() {

        Intent intent = new Intent();
        intent.putExtra("posicion", getIntent().getIntExtra("posicion", -1));
        intent.putExtra("nombre_empresa", nombre.getText().toString());
        intent.putExtra("tipo_auditoria", tipo.getText().toString());
        intent.putExtra("fecha", fecha.getText().toString());
        setResult(RESULT_OK, intent);
        super.finish();
        Toast.makeText(Informacion.this, "Se ha guardado la información correctamente", Toast.LENGTH_SHORT).show();
    }
}