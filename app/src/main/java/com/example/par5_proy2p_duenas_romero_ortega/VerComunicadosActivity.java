package com.example.par5_proy2p_duenas_romero_ortega;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Models.Comunicado;
import Models.Evento;
import Models.Usuario;
import Persistencia.ComunicadoRepositorio;

public class VerComunicadosActivity extends AppCompatActivity {
    private Button btn_selFecha;
    private TextView selFecha;
    private Button btn_volverVerCom;
    private List<Comunicado> comunicados;
    public ArrayList<Comunicado> listaFiltrada;
    private Uri imagenUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ver_comunicados);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.textoprueba), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_selFecha = findViewById(R.id.Btn_selFecha);
        selFecha = findViewById(R.id.txtFecha);
        btn_volverVerCom = findViewById(R.id.Btn_volverCom);

        selFecha.setText("");

        mostrarComunicadosFiltrados(comunicadosFiltrados());


        //Volver al activity anterior
        btn_volverVerCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VerComunicadosActivity.this, MainActivity.class));
            }
        });

        //Seleccionar fecha
        btn_selFecha.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, y, m, d) -> {
                        String fecha = String.format("%02d/%02d/%04d", d, m + 1, y);
                        selFecha.setText(fecha);
                        mostrarComunicadosFiltrados(comunicadosFiltrados());
                    }, year, month, day);
            datePickerDialog.show();
        });
    }

    //Acceder al archivo de comunicados
    private List<Comunicado> comunicadosFiltrados(){
        comunicados = ComunicadoRepositorio.cargarComunicados(this, Usuario.logged_user_id);
        String fechaSel = selFecha.getText().toString();
        listaFiltrada = new ArrayList<>();
        if(fechaSel == null || fechaSel.isEmpty()){
            listaFiltrada.addAll(comunicados);
        }else {
            for (Comunicado comunicado : comunicados) {
                if (comunicado instanceof Evento) {
                    Evento evento = (Evento) comunicado;
                    if (evento.getFecha() != null && evento.getFecha().equals(fechaSel)) {
                        listaFiltrada.add(evento);
                    }
                }
            }
        }
        return listaFiltrada;
    }

    //Mostrar comunicados filtrados
    private void mostrarComunicadosFiltrados(List<Comunicado> comunicadosF) {
        LinearLayout contenedorCom = findViewById(R.id.layout_Com);
        contenedorCom.removeAllViews();

        for (Comunicado comunicado : comunicadosF) {
            // Layout de cada comunicado
            LinearLayout comunicadoLayout = new LinearLayout(this);
            comunicadoLayout.setOrientation(LinearLayout.VERTICAL);
            comunicadoLayout.setPadding(10, 10, 10, 10);

            // Título
            TextView titulo = new TextView(this);
            titulo.setText(comunicado.getTitulo());
            titulo.setTextSize(18);
            titulo.setPadding(0, 0, 0, 8);
            titulo.setGravity(Gravity.CENTER);

            // Imagen
            ImageView imagen = new ImageView(this);
            imagen.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Uri uriImagen = obtenerImagenUri(comunicado.getNombreArchivoImagen());
            imagen.setImageURI(uriImagen);

            // Descripción
            TextView descripcion = new TextView(this);
            descripcion.setText(comunicado.getDescripcion());
            descripcion.setTextSize(14);

            comunicadoLayout.addView(titulo);
            comunicadoLayout.addView(imagen);
            comunicadoLayout.addView(descripcion);

            //Si es un evento, mostrar la fecha
            if (comunicado instanceof Evento) {
                Evento evento = (Evento) comunicado;

                TextView fecha = new TextView(this);
                fecha.setText("Fecha: " + evento.getFecha());
                fecha.setTextSize(12);
                fecha.setPadding(0, 8, 0, 0);

                comunicadoLayout.addView(fecha);
            }

            // Agregar al contenedor principal
            contenedorCom.addView(comunicadoLayout);

        }
    }

    private Uri obtenerImagenUri(String nombreArchivo) {
        File file = new File(getFilesDir(), nombreArchivo);
        return Uri.fromFile(file);
    }





}