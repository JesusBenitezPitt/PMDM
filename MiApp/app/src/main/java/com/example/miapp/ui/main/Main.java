package com.example.miapp.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
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

import com.example.miapp.R;
import com.example.miapp.data.repository.EmpresaRepository;
import com.example.miapp.model.Empresa;
import com.example.miapp.model.EmpresaEntity;
import com.example.miapp.ui.adapter.Adaptador;
import com.example.miapp.ui.add.Anadir_Nuevo;
import com.example.miapp.ui.info.Informacion;
import com.example.miapp.utils.ImagenUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Main extends AppCompatActivity {

    private static final int REQUEST_CODE_ANADIR = 1;
    private static final int REQUEST_CODE_MODIFICAR = 2;
    private static final int REQUEST_CODE_CONSULTAR = 3;

    private ListView lista;
    private ArrayList<Empresa> datos = new ArrayList<>();
    private Adaptador adaptador;
    private Toolbar toolBar;
    private EmpresaRepository empresaRepo;
    private int posicionSeleccionada = -1;
    private int userId;
    private SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

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

        empresaRepo = new EmpresaRepository(getApplicationContext());

        userId = getIntent().getIntExtra("userId", -1);

        adaptador = new Adaptador(this, R.layout.entrada, datos) {
            @Override
            public void onEntrada(Object entrada, View view, int position) {
                if (entrada != null) {
                    Empresa item = (Empresa) entrada;
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
                    ratingBar.setRating((float) item.getRating());

                    layoutMain.setOnClickListener(v -> posicionSeleccionada = position);
                    layoutMain.setOnLongClickListener(v -> {
                        posicionSeleccionada = position;
                        return false;
                    });

                    empresa_texto.setText(item.getNombre());
                    tipo_texto.setText(item.getTipo());

                    try {
                        if (item.getImagenUri() != null) {
                            imagen_entrada.setImageURI(item.getImagenUri());
                        } else {
                            imagen_entrada.setImageDrawable(ImagenUtil.bytesToDrawable(Main.this, item.getImagen()));
                        }
                    } catch (Exception e) {
                        imagen_entrada.setImageResource(R.mipmap.ic_launcher);
                    }

                    fecha_texto.setText(formato.format(item.getFecha()));
                }
            }
        };
        lista.setAdapter(adaptador);
        registerForContextMenu(lista);

        FloatingActionButton fab = findViewById(R.id.add);
        fab.setOnClickListener(v -> abrirInformacion("consultar"));

        cargarDatos(userId);
    }

    private void cargarDatos(int userId) {
        empresaRepo.obtenerEmpresasID(userId, empresas -> runOnUiThread(() -> {
            datos.clear();
            for (EmpresaEntity e : empresas) {
                datos.add(new Empresa(e.imagen, e));
            }
            adaptador.notifyDataSetChanged();
            posicionSeleccionada = -1;
        }));
    }

    private void abrirInformacion(String modo) {
        if (posicionSeleccionada == -1) return;
        Empresa e = datos.get(posicionSeleccionada);
        Intent intent = new Intent(Main.this, Informacion.class);
        intent.putExtra("modo", modo);
        intent.putExtra("posicion", posicionSeleccionada);
        intent.putExtra("empresa", e);

        if ("modificar".equals(modo)) {
            startActivityForResult(intent, REQUEST_CODE_MODIFICAR);
        } else {
            startActivityForResult(intent, REQUEST_CODE_CONSULTAR);
        }
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
            startActivityForResult(new Intent(this, Anadir_Nuevo.class), REQUEST_CODE_ANADIR);
        } else if (id == R.id.menu_modificar) {
            if (posicionSeleccionada == -1) {
                Toast.makeText(this, "Tienes que seleccionar un elemento para modificarlo.", Toast.LENGTH_SHORT).show();
            } else {
                abrirInformacion("modificar");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.op_menu_contextual, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() == R.id.eliminar_registro && info != null && info.position >= 0 && info.position < datos.size()) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast));
            TextView text = layout.findViewById(R.id.toast_text);

            Empresa empresaAEliminar = datos.get(posicionSeleccionada);
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Main.this, R.style.MiEstiloDialogo);
            builder.setTitle("Eliminar registro");
            builder.setMessage("Estas seguro de que quieres eliminar el registro de la empresa " + empresaAEliminar.getNombre());
            builder.setPositiveButton("Si", (dialog, which) -> {
                EmpresaEntity entity = new EmpresaEntity();
                entity.id = empresaAEliminar.getEmpresaId();

                empresaRepo.eliminarEmpresas(entity, rows -> {
                    cargarDatos(userId);
                    runOnUiThread(() -> Toast.makeText(this, "Registro eliminado", Toast.LENGTH_SHORT).show());
                });

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
            builder.create().show();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) return;

        Bundle b = data.getExtras();
        if (b == null) return;

        if (requestCode == REQUEST_CODE_ANADIR) {
            String nombre = b.getString("nombre_empresa");
            String tipo = b.getString("tipo_auditoria");
            Date fecha = parsearFecha(b.getString("fecha"));
            float rating = b.getFloat("rating");
            String descripcion = b.getString("descripcion");
            String pagina = b.getString("pagina");
            String num = b.getString("num");

            int imagen = R.mipmap.ic_launcher;
            byte[] img = ImagenUtil.drawableToBytes(this, imagen);

            EmpresaEntity nuevaEmpresa = new EmpresaEntity(img, nombre, tipo, rating, fecha, descripcion, pagina, num, userId);
            empresaRepo.insertarEmpresa(nuevaEmpresa, id -> runOnUiThread(() -> {
                cargarDatos(userId);
            }));
        }

        if (requestCode == REQUEST_CODE_MODIFICAR) {
            int pos = b.getInt("posicion");
            Empresa empresaOriginal = datos.get(pos);

            EmpresaEntity empresaActualizada = new EmpresaEntity(
                    empresaOriginal.getImagen(),
                    b.getString("nombre_empresa"),
                    b.getString("tipo_auditoria"),
                    b.getDouble("rating"),
                    parsearFecha(b.getString("fecha")),
                    b.getString("descripcion"),
                    b.getString("pagina"),
                    b.getString("num"),
                    empresaOriginal.getUserId()
            );
            empresaActualizada.id = empresaOriginal.getEmpresaId();

            empresaRepo.actualizarEmpresas(empresaActualizada, rows -> runOnUiThread(() -> {
                cargarDatos(userId);
                Toast.makeText(this, "Registro actualizado", Toast.LENGTH_SHORT).show();
            }));
        }
    }
}