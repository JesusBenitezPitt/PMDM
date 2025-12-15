package com.example.actividad_1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);

        EditText editText1 = findViewById(R.id.edit_text1);
        EditText editText2 = findViewById(R.id.edit_text2);

        MaterialButton guardar = findViewById(R.id.guardar);
        MaterialButton recuperar = findViewById(R.id.recuperar);

        TextView textView1 = findViewById(R.id.text_view1);
        TextView textView2 = findViewById(R.id.text_view2);

        guardar.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("texto1", editText1.getText().toString());
            editor.putString("texto2", editText2.getText().toString());
            editor.commit();
            Toast.makeText(this, "Textos guardados.", Toast.LENGTH_SHORT).show();
        });

        recuperar.setOnClickListener(v -> {
            textView1.setText(prefs.getString("texto1", "Valor por defecto."));
            textView2.setText(prefs.getString("texto2", "Valor por defecto."));
        });

    }
}