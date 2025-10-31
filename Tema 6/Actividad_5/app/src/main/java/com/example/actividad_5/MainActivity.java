package com.example.actividad_5;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        SubMenu submenuSemana = menu.addSubMenu(Menu.NONE, 1, Menu.NONE, "DIAS DE LA SEMANA");
        submenuSemana.add(Menu.NONE, 2, Menu.NONE, "LUNES");
        submenuSemana.add(Menu.NONE, 3, Menu.NONE, "MARTES");
        submenuSemana.add(Menu.NONE, 4, Menu.NONE, "MIERCOLES");
        submenuSemana.add(Menu.NONE, 5, Menu.NONE, "JUEVES");
        SubMenu submenuMes = menu.addSubMenu(Menu.NONE, 6, Menu.NONE, "MESES DEL AÑO");
        submenuMes.add(Menu.NONE, 7, Menu.NONE, "ENERO");
        submenuMes.add(Menu.NONE, 8, Menu.NONE, "FEBRERO");
        submenuMes.add(Menu.NONE, 9, Menu.NONE, "MARZO");
        submenuMes.add(Menu.NONE, 10, Menu.NONE, "ABRIL");
        submenuMes.add(Menu.NONE, 11, Menu.NONE, "MAYO");
        submenuMes.add(Menu.NONE, 12, Menu.NONE, "JUNIO");
        submenuMes.add(Menu.NONE, 13, Menu.NONE, "JULIO");
        submenuMes.add(Menu.NONE, 14, Menu.NONE, "AGOSTO");
        submenuMes.add(Menu.NONE, 15, Menu.NONE, "SEPTIEMBRE");
        submenuMes.add(Menu.NONE, 16, Menu.NONE, "OCTUBRE");
        submenuMes.add(Menu.NONE, 17, Menu.NONE, "NOVIEMBRE");
        submenuMes.add(Menu.NONE, 18, Menu.NONE, "DICIEMBRE");

        getMenuInflater().inflate(R.menu.menu, menu); // generado desde XML
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        TextView texto = (TextView) findViewById(R.id.texto);
        if (id == R.id.lunes) {
            texto.setText("Pulsado Lunes");
        } else if (id == R.id.martes) {
            texto.setText("Pulsado Martes");
        } else if (id == R.id.miercoles) {
            texto.setText("Pulsado Miercoles");
        } else if (id == R.id.jueves) {
            texto.setText("Pulsado Jueves");
        } else if (id == R.id.viernes) {
            texto.setText("Pulsado Viernes");
        } else if (id == R.id.sabado) {
            texto.setText("Pulsado Sabado");
        } else if (id == R.id.domingo) {
            texto.setText("Pulsado Domingo");
        }

        if (id > 1 && id < 6) {
            texto.setText("Pulsado el dia " + item.getTitle());
        } else if (id > 6 && id < 19) {
            texto.setText("Pulsado el mes " + item.getTitle());
        }
        return true;
    }

}