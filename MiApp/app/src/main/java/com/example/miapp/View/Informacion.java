package com.example.miapp.View;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.miapp.R;

import java.util.Calendar;

public class Informacion extends AppCompatActivity {

    private EditText nombre;
    private EditText tipo;
    private int posicion;
    private Button boton_fecha;
    private DatePickerDialog datePickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_informacion);

        initDatePickerDialog();

        boton_fecha = findViewById(R.id.botonDatePicker);
        nombre = findViewById(R.id.nombre_empresa);
        tipo = findViewById(R.id.tipo_auditoria);

        Bundle b = getIntent().getExtras();
        posicion = b.getInt("posicion");
        nombre.setText(b.getString("nombre_empresa"));
        tipo.setText(b.getString("tipo_auditoria"));
        boton_fecha.setText(fechaActual());

        Button boton = findViewById(R.id.guardar);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        boton_fecha.setOnClickListener(v -> {
            datePickerDialog.show();
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
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("posicion", posicion);
        intent.putExtra("nombre_empresa", nombre.getText().toString());
        intent.putExtra("tipo_auditoria", tipo.getText().toString());
        intent.putExtra("fecha", boton_fecha.getText().toString());
        setResult(RESULT_OK, intent);
        super.finish();
        Toast.makeText(Informacion.this, "Se ha guardado la información correctamente", Toast.LENGTH_SHORT).show();
    }
}