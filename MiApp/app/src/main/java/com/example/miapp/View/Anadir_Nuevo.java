package com.example.miapp.View;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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

import java.util.Calendar;

public class Anadir_Nuevo extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private static final int REQUEST_IMAGE = 1001;
    private EditText nombre_empresa;
    private EditText tipo_auditoria;
    private EditText descripcion;
    private EditText pagina;
    private EditText num;
    private Button boton_fecha;
    private RatingBar rating_seguridad;
    private Button boton_añadir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_nuevo);
        initDatePickerDialog();

        nombre_empresa = findViewById(R.id.nombre_empresa);
        tipo_auditoria = findViewById(R.id.tipo_auditoria);
        boton_fecha = findViewById(R.id.botonDatePicker);
        rating_seguridad = findViewById(R.id.rating_seguridad);
        descripcion = findViewById(R.id.descripcion);
        pagina = findViewById(R.id.pagina_web);
        num = findViewById(R.id.num_telefono);

        boton_añadir = findViewById(R.id.añadir);

        boton_fecha.setText(fechaActual());
        
        boton_añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nombre_empresa.getText().toString().isEmpty() || tipo_auditoria.getText().toString().isEmpty() || boton_fecha.getText().toString().isEmpty() || boton_fecha.getText().toString().isEmpty()){
                    Toast.makeText(Anadir_Nuevo.this, "Tienes que rellenar los campos para guardar una nueva auditoria.", Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                }
            }
        });

        boton_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
    }

    private String fechaActual(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month += 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return dateToString(day, month, year);
    }

    private void initDatePickerDialog(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day){
                month += 1;
                String date = dateToString(day, month, year);
                boton_fecha.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String dateToString(int day, int month, int year){
        return day + "/" + month + "/" + year;
    }

    @Override
    public void finish(){
        Intent intent = new Intent();
        intent.putExtra("nombre_empresa", nombre_empresa.getText().toString());
        intent.putExtra("tipo_auditoria", tipo_auditoria.getText().toString());
        intent.putExtra("fecha", boton_fecha.getText().toString());
        intent.putExtra("rating", rating_seguridad.getRating());
        intent.putExtra("descripcion", descripcion.getText().toString());
        intent.putExtra("pagina", pagina.getText().toString());
        intent.putExtra("num", num.getText().toString());
        setResult(RESULT_OK, intent);
        super.finish();
    }
}