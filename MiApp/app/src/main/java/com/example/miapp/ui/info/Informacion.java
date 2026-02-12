package com.example.miapp.ui.info;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.miapp.R;
import com.example.miapp.model.Empresa;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class Informacion extends AppCompatActivity {

    private EditText nombreField, tipoField, descriptionField, paginaField, numField;
    private RatingBar ratingField;
    private Button botonFecha, guardarButton;
    private int posicion;
    private DatePickerDialog datePickerDialog;
    private static final int REQUEST_CODE_GALERIA = 100;
    private FrameLayout container_preview;
    private ImageView imagen_preview, placeholder;
    private Bitmap bitmapReducido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_informacion);

        initViews();
        initDatePicker();
        populateFields();
        configureMode();
    }

    private void initViews() {
        nombreField = findViewById(R.id.nombre_empresa);
        tipoField = findViewById(R.id.tipo_auditoria);
        ratingField = findViewById(R.id.nivel_seguridad);
        descriptionField = findViewById(R.id.descripcion);
        paginaField = findViewById(R.id.pagina_web);
        numField = findViewById(R.id.num_telefono);
        botonFecha = findViewById(R.id.botonDatePicker);
        guardarButton = findViewById(R.id.guardar);
        container_preview = findViewById(R.id.container_preview);
        imagen_preview = findViewById(R.id.imagen_preview);
        placeholder = findViewById(R.id.icon_placeholder);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener listener = (view, year, month, day) -> {
            month += 1;
            botonFecha.setText(dateToString(day, month, year));
        };

        Calendar cal = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,
                listener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        botonFecha.setText(fechaActual());
    }

    private void populateFields() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Empresa e = (Empresa) extras.getSerializable("empresa");
            Log.d("Empresa", e.toString());
            posicion = extras.getInt("posicion");
            if (e != null) {
                nombreField.setText(e.getNombre());
                tipoField.setText(e.getTipo());
                ratingField.setRating((float) e.getRating());
                descriptionField.setText(e.getDescripcion());
                paginaField.setText(e.getPagina_web());
                numField.setText(e.getNum_telefono());
                Bitmap imagen = BitmapFactory.decodeByteArray(e.getImagen(), 0, e.getImagen().length);
                imagen_preview.setImageBitmap(imagen);
            }
        }
    }

    private void configureMode() {
        Bundle extras = getIntent().getExtras();
        if (extras != null && "consultar".equals(extras.getString("modo"))) {
            nombreField.setFocusable(false);
            nombreField.setClickable(false);
            tipoField.setFocusable(false);
            tipoField.setClickable(false);
            ratingField.setEnabled(false);
            descriptionField.setFocusable(false);
            descriptionField.setClickable(false);
            paginaField.setFocusable(false);
            paginaField.setClickable(false);
            numField.setFocusable(false);
            numField.setClickable(false);
            botonFecha.setEnabled(false);
            guardarButton.setText("Volver");
        }
        initListeners(extras.getString("modo"));
    }

    private void initListeners(String modo) {
        guardarButton.setOnClickListener(v -> {
            if (modo.equals("modificar")) {
                guardarCambios();
            }
            finish();
        });
        botonFecha.setOnClickListener(v -> datePickerDialog.show());
        if (modo.equals("modificar")) {
            container_preview.setOnClickListener(v -> abrirGaleria());
        }
    }

    private void guardarCambios() {
        Intent intent = new Intent();
        intent.putExtra("posicion", posicion);
        intent.putExtra("imagenBitmap", bitmapReducido);
        intent.putExtra("nombre_empresa", nombreField.getText().toString());
        intent.putExtra("tipo_auditoria", tipoField.getText().toString());
        intent.putExtra("fecha", botonFecha.getText().toString());
        intent.putExtra("rating", (double) ratingField.getRating());
        intent.putExtra("descripcion", descriptionField.getText().toString());
        intent.putExtra("pagina", paginaField.getText().toString());
        intent.putExtra("num", numField.getText().toString());
        setResult(RESULT_OK, intent);
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
        return dateToString(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
    }

    private String dateToString(int day, int month, int year) {
        return day + "/" + month + "/" + year;
    }

    @Override
    public void finish() {
        super.finish();
    }
}