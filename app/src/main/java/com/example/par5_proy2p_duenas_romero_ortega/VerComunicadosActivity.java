package com.example.par5_proy2p_duenas_romero_ortega;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Interfaces.VerComunicadoDetails;
import Models.Comunicado;
import Models.Evento;
import Models.Usuario;
import Persistencia.ComunicadoRepositorio;
import Utils.ImageUtils;

public class VerComunicadosActivity extends AppCompatActivity implements VerComunicadoDetails {
    private Button btn_selFecha;
    private TextView selFecha;
    private ImageButton btnVolver;
    private List<Comunicado> comunicados;
    public ArrayList<Comunicado> listaFiltrada;
    private Uri imagenUri;

    private static final int MAX_DESC_PREVIEW_LENGTH = 100;

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

        selFecha.setText("");

        mostrarComunicadosFiltrados(comunicadosFiltrados());


        //Volver al activity anterior
        this.btnVolver = findViewById(R.id.backButton);
        btnVolver.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(VerComunicadosActivity.this, MainActivity.class);
                        startActivity(intent);
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

                        //Verficar que hay un comunicado en esa fecha
                        if (!hayComunicadosEnFecha(fecha, comunicados)) {
                            Toast.makeText(VerComunicadosActivity.this, "No hay comunicados en esta fecha", Toast.LENGTH_SHORT).show();
                        }else{
                            selFecha.setText(fecha);
                            mostrarComunicadosFiltrados(comunicadosFiltrados());
                        }
                    }, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });
    }

    /**
     * Filtra los comunicados según la fecha seleccionada.
     * Si no hay fecha seleccionada, devuelve todos los comunicados.
     * @return Lista de comunicados filtrados
     */
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

    /**
     * Muestra los comunicados filtrados en la interfaz de usuario.
     * Crea dinámicamente las vistas para cada comunicado incluyendo título, imagen y descripción.
     * @param comunicadosF Lista de comunicados a mostrar
     */
    private void mostrarComunicadosFiltrados(List<Comunicado> comunicadosF) {
        LinearLayout contenedorCom = findViewById(R.id.layout_Com);
        contenedorCom.removeAllViews();

        for (int i = 0; i < comunicadosF.size(); i++) {
            Comunicado comunicado = comunicadosF.get(i);

            LinearLayout comunicadoLayout = new LinearLayout(this);
            comunicadoLayout.setOrientation(LinearLayout.VERTICAL);
            comunicadoLayout.setPadding(10, 10, 10, 10);

            // Título
            TextView titulo = new TextView(this);
            titulo.setText(comunicado.getTitulo());
            titulo.setTextSize(25);
            titulo.setTextColor(getColor(R.color.texto));
            titulo.setTypeface(null, Typeface.BOLD);
            titulo.setPadding(0, 0, 0, 8);
            titulo.setGravity(Gravity.CENTER);

            // Imagen
            ImageView imagen = new ImageView(this);
            imagen.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Uri uriImagen = ImageUtils.obtenerImagenUri(this,comunicado.getNombreArchivoImagen());
            imagen.setImageURI(uriImagen);
            imagen.setAdjustViewBounds(true);

            // Descripción
            TextView descripcion = new TextView(this);
            String descOriginal = comunicado.getDescripcion();
            if(descOriginal != null && descOriginal.length() > MAX_DESC_PREVIEW_LENGTH){
                String shortDesc = descOriginal.substring(0, MAX_DESC_PREVIEW_LENGTH)+"...";
                descripcion.setText(shortDesc);
            }else{
                descripcion.setText(descOriginal);
            }
            descripcion.setTextSize(25);
            descripcion.setTextColor(getColor(R.color.texto));

            comunicadoLayout.addView(titulo);
            comunicadoLayout.addView(imagen);
            comunicadoLayout.addView(descripcion);

            // Si es un evento, mostrar la fecha
            if (comunicado instanceof Evento) {
                Evento evento = (Evento) comunicado;

                TextView fecha = new TextView(this);
                String fomatedFecha = getString(R.string.fecha_label) + " " +evento.getFecha();
                fecha.setText(fomatedFecha);
                fecha.setTextSize(20);
                fecha.setTextColor(getColor(R.color.texto));
                fecha.setPadding(0, 8, 0, 0);

                comunicadoLayout.addView(fecha);
            }

            comunicadoLayout.setOnClickListener(v -> showComunicadoDetails(VerComunicadosActivity.this, comunicado));

            // Agregar el comunicado al contenedor
            contenedorCom.addView(comunicadoLayout);


            if (i < comunicadosF.size() - 1) {
                View divider = new View(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 2);
                params.setMargins(0, 16, 0, 16);
                divider.setLayoutParams(params);
                divider.setBackgroundColor(getColor(R.color.linea_divisora_color));

                contenedorCom.addView(divider);
            }
        }
    }



    /**
     * Verifica si hay eventos programados para una fecha específica.
     * @param fecha Fecha a verificar en formato dd/MM/yyyy
     * @param comunicados Lista de comunicados a verificar
     * @return true si hay al menos un evento en la fecha especificada, false en caso contrario
     */
    private boolean hayComunicadosEnFecha(String fecha, List<Comunicado> comunicados) {
        for (Comunicado comunicado : comunicados) {
            if (comunicado instanceof Evento) {
                Evento evento = (Evento) comunicado;
                if (evento.getFecha() != null && evento.getFecha().equals(fecha)) {
                    return true;
                }
            }
        }
        return false;
    }
}