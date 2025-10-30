package com.example.miapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Main extends AppCompatActivity {

    private ListView lista;
    private TextView texto;
    private RadioButton radioButton_pulsado;

    public static class Encapsulador {
        private int imagen;
        private String empresa;
        private String texto;
        private double rating;
        private Date fecha;
        private boolean acabado;

        public Encapsulador(int idImagen, String empresa, String tipo, double rating, Date fecha, boolean acabado) {
            this.imagen = idImagen;
            this.empresa = empresa;
            this.texto = tipo;
            this.rating = rating;
            this.fecha = fecha;
            this.acabado = acabado;
        }

        public int getImagenId() {
            return imagen;
        }

        public String getEmpresa() {
            return empresa;
        }

        public String getTipo() {
            return texto;
        }

        public double getRating() {
            return rating;
        }

        public Date getFecha() {
            return fecha;
        }

        public boolean getAcabado() {
            return acabado;
        }
    }

    private Date parsearFecha(SimpleDateFormat formato, String fechaString) {
        try {
            return formato.parse(fechaString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lista = findViewById(R.id.principa_lista_view);

        Calendar calendario = Calendar.getInstance();
        Date fecha = calendario.getTime();

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

        ArrayList<Encapsulador> datos = new ArrayList<>();
        datos.add(new Encapsulador(R.drawable.software, "TechCorp", "Pentest", 4, parsearFecha(formato, "15/08/2025"), true));
        datos.add(new Encapsulador(R.drawable.industria, "IndusSecure", "Social Engineering", 3, parsearFecha(formato, "01/09/2025"), false));
        datos.add(new Encapsulador(R.drawable.finanzas, "FinBank", "Red / Infra", 5, parsearFecha(formato, "30/07/2025"), true));
        datos.add(new Encapsulador(R.drawable.salud, "MediCare", "Compliance / GDPR", 3, parsearFecha(formato, "05/10/2025"), false));
        datos.add(new Encapsulador(R.drawable.educacion, "UniTech", "Cloud Security", 2, parsearFecha(formato, "20/09/2025"), true));
        datos.add(new Encapsulador(R.drawable.e_commerce, "ShopZone", "Pentest", 4, parsearFecha(formato, "10/10/2025"), false));

        lista.setAdapter(new Adaptador(this, R.layout.entrada, datos) {
            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null){
                    TextView empresa = (TextView) view.findViewById(R.id.texto_titulo);
                    TextView tipo = (TextView) view.findViewById(R.id.texto_datos);
                    ImageView imagen_entrada = (ImageView) view.findViewById(R.id.imagen);
                    TextView fecha = (TextView) view.findViewById(R.id.fecha);
                    CheckBox acabado = (CheckBox) view.findViewById(R.id.checkbox);

                    LinearLayout layoutClickable = (LinearLayout) view.findViewById(R.id.layoutClickable);
                    layoutClickable.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(Main.this, "Has seleccionado la empresa: " + empresa.getText().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    LinearLayout layoutMain = (LinearLayout) view.findViewById(R.id.layoutMain);
                    Animation animacion_entrada = AnimationUtils.loadAnimation(Main.this, R.anim.anim_entrada);
                    layoutMain.setAnimation(animacion_entrada);

                    empresa.setText(((Encapsulador) entrada).getEmpresa());
                    tipo.setText(((Encapsulador) entrada).getTipo());
                    imagen_entrada.setImageResource(((Encapsulador) entrada).getImagenId());
                    fecha.setText(formato.format(((Encapsulador) entrada).getFecha()));
                    if (((Encapsulador) entrada).getAcabado()) {
                        acabado.setChecked(true);
                    } else {
                        acabado.setChecked(false);
                    }
                }
            }
        });

    }
}