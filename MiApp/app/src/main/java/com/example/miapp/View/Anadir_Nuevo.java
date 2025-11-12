package com.example.miapp.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.miapp.R;

public class Anadir_Nuevo extends AppCompatActivity {

    private static final int REQUEST_IMAGE = 1001;
    private EditText nombre_empresa;
    private EditText tipo_auditoria;
    private DatePicker fecha_auditoria;
    private RatingBar rating_seguridad;
    private ImageView imageLogo;
    private Button boton_añadir;
    private Uri imagenUriSeleccionada = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_nuevo);

        imageLogo = findViewById(R.id.imageLogo);
        nombre_empresa = findViewById(R.id.nombre_empresa);
        tipo_auditoria = findViewById(R.id.tipo_auditoria);
        fecha_auditoria = findViewById(R.id.fecha);
        rating_seguridad = findViewById(R.id.rating_seguridad);

        boton_añadir = findViewById(R.id.añadir);

        imageLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });
        
        boton_añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Prueba", "Nombre: " + nombre_empresa.getText().toString() + "\nTipo: " + tipo_auditoria.getText().toString() + "\nFecha: " + fecha_auditoria.getText().toString() + "\nRating: " + rating_seguridad.getRating() + "\nImagen: " + imageLogo.toString());
                if(nombre_empresa.getText().toString().isEmpty() || tipo_auditoria.getText().toString().isEmpty() || fecha_auditoria.getText().toString().isEmpty() || fecha_auditoria.getText().toString().isEmpty()){
                    Toast.makeText(Anadir_Nuevo.this, "Tienes que rellenar los campos para guardar una nueva auditoria.", Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    public void finish(){
        Intent intent = new Intent();
        intent.putExtra("nombre_empresa", nombre_empresa.getText().toString());
        intent.putExtra("tipo_auditoria", tipo_auditoria.getText().toString());
        intent.putExtra("fecha", fecha_auditoria.getText().toString());
        intent.putExtra("rating", rating_seguridad.getRating());
        intent.putExtra("imagen_uri", imageLogo.toString());
        setResult(RESULT_OK, intent);
        super.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            imageLogo.setImageURI(imageUri);
        }
    }
}