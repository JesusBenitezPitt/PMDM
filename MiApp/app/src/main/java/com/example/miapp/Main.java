package com.example.miapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.example.miapp.Controller.AppDatabase;
import com.example.miapp.Model.Adaptador;
import com.example.miapp.Model.Empresa;
import com.example.miapp.Model.EmpresaDAO;
import com.example.miapp.Model.Encapsulador;
import com.example.miapp.View.Anadir_Nuevo;
import com.example.miapp.View.Informacion;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

public class Main extends AppCompatActivity {

    private static final int REQUEST_CODE_ANADIR = 1;
    private static final int REQUEST_CODE_MODIFICAR = 2;
    private static final int REQUEST_CODE_CONSULTAR = 3;

    private int posicionSeleccionada = -1;
    private ListView lista;
    private ArrayList<Encapsulador> datos = new ArrayList<>();
    private Adaptador adaptador;
    private Toolbar toolBar;
    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private Date parsearFecha(String fechaString) {
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

        toolBar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "secure-ops").build();
        EmpresaDAO empresaDAO = db.empresaDAO();

//        datos.add(new Encapsulador(R.drawable.software, "TechCorp", "Pentest", 4, parsearFecha("15/08/2025"), "Descripcion", "Pagina web", "Numero de telefono", 1));
//        datos.add(new Encapsulador(R.drawable.industria, "IndusSecure", "Social Engineering", 3, parsearFecha("01/09/2025"), "Descripcion", "Pagina web", "Numero de telefono", 2));
//        datos.add(new Encapsulador(R.drawable.finanzas, "FinBank", "Red / Infra", 5, parsearFecha("30/07/2025"), "Descripcion", "Pagina web", "Numero de telefono", 3));
//        datos.add(new Encapsulador(R.drawable.salud, "MediCare", "Compliance / GDPR", 3, parsearFecha("05/10/2025"), "Descripcion", "Pagina web", "Numero de telefono", 1));
//        datos.add(new Encapsulador(R.drawable.educacion, "UniTech", "Cloud Security", 2, parsearFecha("20/09/2025"), "Descripcion", "Pagina web", "Numero de telefono", 2));
//        datos.add(new Encapsulador(R.drawable.e_commerce, "ShopZone", "Pentest", 4, parsearFecha("10/10/2025"), "Descripcion", "Pagina web", "Numero de telefono", 3));

//        List<Empresa> empresas = new ArrayList<>();
//
//        for (Encapsulador e : datos) {
//            empresas.add(new Empresa(
//                    e.getEmpresa(),
//                    e.getTipo(),
//                    e.getRating(),
//                    e.getFecha(),
//                    e.getDescripcion(),
//                    e.getPagina_web(),
//                    e.getNum_telefono(),
//                    e.getUserId()
//            ));
//        }
//
//        new Thread(() -> {
//            empresaDAO.insertAll(empresas);
//        }).start();

        Intent intentUserId = getIntent();
        int userId = intentUserId.getIntExtra("userId", -1);

        new Thread(() -> {
            List<Empresa> empresas = empresaDAO.getAll(userId);

            runOnUiThread(() -> {
                datos.clear();

                for (Empresa empresa : empresas) {
                    datos.add(new Encapsulador(R.mipmap.ic_launcher, empresa));
                }

                adaptador.notifyDataSetChanged();
            });
        }).start();


        adaptador = new Adaptador(this, R.layout.entrada, datos) {
            @Override
            public void onEntrada(Object entrada, View view, int position) {
                if (entrada != null){

                    LinearLayout layoutMain = view.findViewById(R.id.layoutMain);
                    if (posicionSeleccionada == -1) {
                        Animation animacion_entrada = AnimationUtils.loadAnimation(Main.this, R.anim.anim_entrada);
                        layoutMain.setAnimation(animacion_entrada);
                    }

                    TextView empresa_texto = view.findViewById(R.id.texto_titulo);
                    TextView tipo_texto = view.findViewById(R.id.texto_datos);
                    ImageView imagen_entrada = view.findViewById(R.id.imagen);
                    TextView fecha_texto = view.findViewById(R.id.fecha);
                    RatingBar ratingBar = view.findViewById(R.id.ratingBar);
                    ratingBar.setRating((float)((Encapsulador)entrada).getRating());

                    layoutMain.setOnClickListener(v -> {
                        posicionSeleccionada = position;
                    });

                    layoutMain.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            posicionSeleccionada = position;
                            return false;
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
        registerForContextMenu(lista);

        FloatingActionButton fab = findViewById(R.id.add);
        fab.setOnClickListener(v -> {
            Encapsulador i = datos.get(posicionSeleccionada);
            Intent intent = new Intent(Main.this, Informacion.class);
            intent.putExtra("modo", "consultar");
            intent.putExtra("posicion", posicionSeleccionada);
            intent.putExtra("nombre_empresa", i.getEmpresa());
            intent.putExtra("tipo_auditoria", i.getTipo());
            intent.putExtra("fecha", i.getFecha().toString());
            intent.putExtra("descripcion", i.getDescripcion());
            intent.putExtra("pagina", i.getPagina_web());
            intent.putExtra("num", i.getNum_telefono());
            intent.putExtra("rating", i.getRating());
            startActivityForResult(intent, REQUEST_CODE_CONSULTAR);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.op_menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_insertar) {
            Intent intent = new Intent(Main.this, Anadir_Nuevo.class);
            startActivityForResult(intent, REQUEST_CODE_ANADIR);
        } else if (id == R.id.menu_modificar) {
            if (posicionSeleccionada == -1){
                Toast.makeText(this, "Tienes que seleccionar un elemento para modificarlo.", Toast.LENGTH_SHORT).show();
            } else {
                Encapsulador i = datos.get(posicionSeleccionada);
                Intent intent = new Intent(Main.this, Informacion.class);
                intent.putExtra("modo", "modificar");
                intent.putExtra("posicion", posicionSeleccionada);
                intent.putExtra("nombre_empresa", i.getEmpresa());
                intent.putExtra("tipo_auditoria", i.getTipo());
                intent.putExtra("fecha", i.getFecha());
                intent.putExtra("descripcion", i.getDescripcion());
                intent.putExtra("pagina", i.getPagina_web());
                intent.putExtra("num", i.getNum_telefono());
                intent.putExtra("rating", i.getRating());
                startActivityForResult(intent, REQUEST_CODE_MODIFICAR);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.op_menu_contextual, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast));
        TextView text = layout.findViewById(R.id.toast_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(Main.this, R.style.MiEstiloDialogo);
        builder.setTitle("Eliminar registro");
        builder.setMessage("Estas seguro de que quieres eliminar el registro de la empresa " + datos.get(posicionSeleccionada).getEmpresa());
        builder.setPositiveButton("Si", (dialog, which) -> {
            datos.remove(posicionSeleccionada);
            adaptador.notifyDataSetChanged();
            text.setText("Registro eliminado");

            Toast toast = new Toast(Main.this);
            toast.setView(layout);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            text.setText("Eliminacion cancelada");

            Toast toast = new Toast(Main.this);
            toast.setView(layout);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        });

        if (item.getItemId() == R.id.eliminar_registro) {
            if (info != null && info.position >= 0 && info.position < datos.size()) {
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            return true;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onActivityResult(int request_code, int result_code, Intent i) {
        super.onActivityResult(request_code, result_code, i);

        if (result_code == RESULT_OK && request_code == REQUEST_CODE_ANADIR) {
            Bundle b = i.getExtras();
            if (b != null) {
                String nombre = b.getString("nombre_empresa");
                String tipo = b.getString("tipo_auditoria");
                String fecha = b.getString("fecha");
                float rating = b.getFloat("rating");
                String descripcion = b.getString("descripcion");
                String pagina = b.getString("pagina");
                String num = b.getString("num");

                Date fechaDate = parsearFecha(fecha);

                datos.add(new Encapsulador(R.mipmap.ic_launcher, nombre, tipo, rating, fechaDate, descripcion, pagina, num, 1));

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
                float rating = b.getFloat("rating");
                Log.d("prueba", "" + rating);
                String descripcion = b.getString("descripcion");
                String pagina = b.getString("pagina");
                String num = b.getString("num");

                Encapsulador empresa = datos.get(pos);
                datos.set(pos, new Encapsulador(empresa.getImagenId(), nombre, tipo, rating, parsearFecha(fecha), descripcion, pagina, num, 1));

                adaptador.notifyDataSetChanged();
                lista.smoothScrollToPosition(pos);
            }
        }
    }
}