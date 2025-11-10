package com.example.miapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.example.miapp.Model.Adaptador;
import com.example.miapp.View.Anadir_Nuevo;
import com.example.miapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Main extends AppCompatActivity {

    private static final int REQUEST_CODE_AÑADIR = 1;
    private static final int REQUEST_CODE_MODIFICAR = 2;
    private static final int REQUEST_CODE_CONSULTAR = 3;
    private static final int REQUEST_CODE_ELIMINAR = 4;

    private int posicionSeleccionada = -1;
    private ListView lista;
//    private RadioButton radioButton_pulsado;
    private ArrayList<Encapsulador> datos = new ArrayList<>();
    private Adaptador adaptador;
    private Toolbar toolBar;

    public static class Encapsulador {
        private int imagen;
        private Uri imagenUri;
        private String empresa;
        private String texto;
        private double rating;
        private Date fecha;
        private boolean acabado;

        public Encapsulador(int idImagen, String empresa, String tipo, double rating, Date fecha) {
            this.imagen = idImagen;
            this.empresa = empresa;
            this.texto = tipo;
            this.rating = rating;
            this.fecha = fecha;
        }

        public Encapsulador(Uri imagenUri, String empresa, String tipo, double rating, Date fecha) {
            this.imagenUri = imagenUri;
            this.empresa = empresa;
            this.texto = tipo;
            this.rating = rating;
            this.fecha = fecha;
            this.imagen = 0;
        }

        public int getImagenId() {
            return imagen;
        }

        public Uri getImagenUri() {
            return imagenUri;
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
        toolBar = findViewById(R.id.toolbar);

        setSupportActionBar(toolBar);

        Calendar calendario = Calendar.getInstance();
        Date fecha = calendario.getTime();

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

        datos.add(new Encapsulador(R.drawable.software, "TechCorp", "Pentest", 4, parsearFecha(formato, "15/08/2025")));
        datos.add(new Encapsulador(R.drawable.industria, "IndusSecure", "Social Engineering", 3, parsearFecha(formato, "01/09/2025")));
        datos.add(new Encapsulador(R.drawable.finanzas, "FinBank", "Red / Infra", 5, parsearFecha(formato, "30/07/2025")));
        datos.add(new Encapsulador(R.drawable.salud, "MediCare", "Compliance / GDPR", 3, parsearFecha(formato, "05/10/2025")));
        datos.add(new Encapsulador(R.drawable.educacion, "UniTech", "Cloud Security", 2, parsearFecha(formato, "20/09/2025")));
        datos.add(new Encapsulador(R.drawable.e_commerce, "ShopZone", "Pentest", 4, parsearFecha(formato, "10/10/2025")));

        adaptador = new Adaptador(this, R.layout.entrada, datos) {
            @Override
            public void onEntrada(Object entrada, View view, int position) {
                if (entrada != null){

                    if (posicionSeleccionada == -1) {
                        LinearLayout layoutMain = (LinearLayout) view.findViewById(R.id.layoutMain);
                        Animation animacion_entrada = AnimationUtils.loadAnimation(Main.this, R.anim.anim_entrada);
                        layoutMain.setAnimation(animacion_entrada);
                    }

                    TextView empresa_texto = (TextView) view.findViewById(R.id.texto_titulo);
                    TextView tipo_texto = (TextView) view.findViewById(R.id.texto_datos);
                    ImageView imagen_entrada = (ImageView) view.findViewById(R.id.imagen);
                    TextView fecha_texto = (TextView) view.findViewById(R.id.fecha);
                    RadioButton radioButton = (RadioButton) view.findViewById(R.id.radiobutton);
                    RatingBar ratingBar = view.findViewById(R.id.ratingBar);
                    ratingBar.setRating((float)((Encapsulador)entrada).getRating());

                    LinearLayout layoutClickable = (LinearLayout) view.findViewById(R.id.layoutClickable);
                    registerForContextMenu(layoutClickable);
                    radioButton.setChecked(position == posicionSeleccionada);

                    layoutClickable.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            posicionSeleccionada = position;
                            adaptador.notifyDataSetChanged();

                            Log.d("Prueba", "" + position + " " + posicionSeleccionada);

//                            Intent intent = new Intent(Main.this, Informacion.class);
//                            int position = lista.getPositionForView(v);
//                            intent.putExtra("posicion", position);
//                            intent.putExtra("nombre_empresa", empresa_texto.getText().toString());
//                            intent.putExtra("tipo_auditoria", tipo_texto.getText().toString());
//                            intent.putExtra("fecha", fecha_texto.getText().toString());
//                            Bundle b = intent.getExtras();
//                            startActivityForResult(intent, REQUEST_CODE_MODIFICAR ,b);
                        }
                    });

                    empresa_texto.setText(((Encapsulador) entrada).getEmpresa());
                    tipo_texto.setText(((Encapsulador) entrada).getTipo());
                    Encapsulador item = (Encapsulador) entrada;
                    try {
                        if (item.getImagenUri() != null) {
                            imagen_entrada.setImageURI(item.getImagenUri());
                        } else {
                            imagen_entrada.setImageResource(item.getImagenId());
                        }
                    } catch (Exception e) {
                        imagen_entrada.setImageResource(R.mipmap.ic_launcher);
                    }
                    fecha_texto.setText(formato.format(((Encapsulador) entrada).getFecha()));
                }
            }

        };
        lista.setAdapter(adaptador);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.op_menu_principal, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d("Prueba", "Insertar: " + R.id.menu_insertar + ", Modificar: " + R.id.menu_modificar);

        if (id == R.id.menu_insertar) {
            Intent intent = new Intent(Main.this, Anadir_Nuevo.class);
            startActivityForResult(intent, REQUEST_CODE_AÑADIR);
        } else if (id == R.id.menu_modificar) {
            modificarElementoSeleccionado();
        }
        return super.onOptionsItemSelected(item);
    }

    private void modificarElementoSeleccionado() {
    }

    private void insertarElemento() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendario = Calendar.getInstance();
        Date fecha = calendario.getTime();

        datos.add(new Encapsulador(R.drawable.software, "NuevaEmpresa", "Nuevo Tipo", 3, fecha));

        adaptador.notifyDataSetChanged();
        lista.smoothScrollToPosition(datos.size() - 1);
    }


    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.op_menu_contextual, menu);
    }

    @Override
    public void onActivityResult(int request_code, int result_code, Intent i) {
        super.onActivityResult(request_code, result_code, i);

        if (result_code == RESULT_OK && request_code == REQUEST_CODE_AÑADIR) {
            Bundle b = i.getExtras();
            if (b != null) {
                String nombre = b.getString("nombre_empresa");
                String tipo = b.getString("tipo_auditoria");
                String fecha = b.getString("fecha");
                String uriString = b.getString("imagen_uri");
                float rating = b.getFloat("rating");

                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                Date fechaDate = parsearFecha(formato, fecha);

                if (uriString != null) {
                    Uri uri = Uri.parse(uriString);
                    datos.add(new Encapsulador(uri, nombre, tipo, rating, fechaDate));
                } else {
                    datos.add(new Encapsulador(R.mipmap.ic_launcher, nombre, tipo, rating, fechaDate));
                }

                adaptador.notifyDataSetChanged();
                lista.smoothScrollToPosition(datos.size() - 1);
            }
        }

        if (result_code == RESULT_OK && request_code == REQUEST_CODE_MODIFICAR) {
            Bundle b = i.getExtras();
            Log.d("Prueba", "" + b);
            if (b != null) {
                int pos = b.getInt("posicion");
                String nombre = b.getString("nombre_empresa");
                String tipo = b.getString("tipo_auditoria");
                String fecha = b.getString("fecha");

                SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");

                Encapsulador empresa = datos.get(pos);
                datos.set(pos, new Encapsulador(empresa.getImagenId(), nombre, tipo, empresa.getRating(), parsearFecha(formater, fecha)));

                adaptador.notifyDataSetChanged();
                lista.smoothScrollToPosition(pos);
            }
        }
    }
}