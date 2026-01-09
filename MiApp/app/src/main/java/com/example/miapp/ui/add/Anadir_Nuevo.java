package com.example.miapp.ui.add;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miapp.R;

import java.util.Calendar;

public class Anadir_Nuevo extends AppCompatActivity {

    private static final int REQUEST_CODE_GALERIA = 100;

    private DatePickerDialog datePickerDialog;
    private EditText nombre_empresa, tipo_auditoria, descripcion, pagina, num;
    private Button boton_fecha, boton_añadir, boton_cancel;
    private RatingBar rating_seguridad;
    private FrameLayout container_preview;
    private ImageView imagen_preview, placeholder;
    private Bitmap bitmapReducido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_nuevo);

        initViews();
        initDatePicker();
        initListeners();
    }

    private void initViews() {
        nombre_empresa = findViewById(R.id.nombre_empresa);
        tipo_auditoria = findViewById(R.id.tipo_auditoria);
        descripcion = findViewById(R.id.descripcion);
        pagina = findViewById(R.id.pagina_web);
        num = findViewById(R.id.num_telefono);
        boton_fecha = findViewById(R.id.botonDatePicker);
        rating_seguridad = findViewById(R.id.rating_seguridad);
        boton_añadir = findViewById(R.id.añadir);
        boton_cancel = findViewById(R.id.cancelar);
        container_preview = findViewById(R.id.container_preview);
        imagen_preview = findViewById(R.id.imagen_preview);
        placeholder = findViewById(R.id.icon_placeholder);

        boton_fecha.setText(fechaActual());
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, day) -> {
            boton_fecha.setText(dateToString(day, month + 1, year));
        };

        Calendar cal = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(
                this,
                AlertDialog.THEME_HOLO_LIGHT,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );
    }

    private void initListeners() {
        boton_fecha.setOnClickListener(v -> datePickerDialog.show());

        boton_añadir.setOnClickListener(v -> {
            if (hayCamposInvalidos()) {
                Toast.makeText(this, "Rellena todos los campos para guardar la auditoría.", Toast.LENGTH_SHORT).show();
            } else {
                finish();
            }
        });

        boton_cancel.setOnClickListener(v -> finish());

        container_preview.setOnClickListener(v -> abrirGaleria());
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_GALERIA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_GALERIA) {
            cargarImagen(data.getData());
        }
    }

    private void cargarImagen(Uri imagenUri) {
        if (imagenUri == null) return;
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagenUri);
            bitmapReducido = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            imagen_preview.setImageBitmap(bitmapReducido);
            placeholder.setVisibility(FrameLayout.GONE);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al cargar imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private String fechaActual() {
        Calendar cal = Calendar.getInstance();
        return dateToString(cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.YEAR));
    }

    private String dateToString(int day, int month, int year) {
        return day + "/" + month + "/" + year;
    }

    private boolean hayCamposInvalidos() {
        return nombre_empresa.getText().toString().isEmpty() ||
                tipo_auditoria.getText().toString().isEmpty() ||
                rating_seguridad.getRating() == 0;
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        if (!hayCamposInvalidos()) {
            intent.putExtra("imagenBitmap", bitmapReducido);
            intent.putExtra("nombre_empresa", nombre_empresa.getText().toString());
            intent.putExtra("tipo_auditoria", tipo_auditoria.getText().toString());
            intent.putExtra("fecha", boton_fecha.getText().toString());
            intent.putExtra("rating", rating_seguridad.getRating());
            intent.putExtra("descripcion", descripcion.getText().toString());
            intent.putExtra("pagina", pagina.getText().toString());
            intent.putExtra("num", num.getText().toString());
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED);
        }
        super.finish();
    }
}