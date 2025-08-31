package com.example.par5_proy2p_duenas_romero_ortega;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import Enums.OrdComunicado;
import Models.Comunicado;
import Utils.DatosDePruebaComunicados;

public class MisComunicadosActivity extends AppCompatActivity {
    private ImageButton btnVolver;
    private TableLayout tablaMisComunicados;
    private Button btnGuardarLista;
    private List<Comunicado> listaComunicados;
    private List<Comunicado> originalListaComunicados;

    // 0: original, 1: ascending, 2: descending
    private int tituloSortState = 0;
    private int fechaSortState = 0;

    private static final String DATE_FORMAT_PATTERN = "dd/MM/yyyy";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.getDefault());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mis_comunicados);


        this.btnVolver = findViewById(R.id.backButton);
        btnVolver.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MisComunicadosActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
        );

        this.tablaMisComunicados = findViewById(R.id.misComunicadosTableLayout);
        this.btnGuardarLista = findViewById(R.id.btnGuardarLista);

        
        originalListaComunicados = new ArrayList<>(DatosDePruebaComunicados.obtenerListaDePrueba());
        listaComunicados = new ArrayList<>(originalListaComunicados);


        renderizarTabla();
        

    }

    private void renderizarTabla() {
        tablaMisComunicados.removeAllViews();
        
        TableRow headerRow = new TableRow(this);

        TextView textViewHeaderTitulo = new TextView(this);
        String tituloHeaderText = "Título";
        if (tituloSortState == 1) {
            tituloHeaderText += " ↑";
        } else if (tituloSortState == 2) {
            tituloHeaderText += " ↓";
        }
        textViewHeaderTitulo.setText(tituloHeaderText);
        textViewHeaderTitulo.setTypeface(null, Typeface.BOLD);
        textViewHeaderTitulo.setPadding(16,8,16,8);
        textViewHeaderTitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fechaSortState = 0; 
                tituloSortState = (tituloSortState + 1) % 3;

                if (tituloSortState == 0) {
                    listaComunicados = new ArrayList<>(originalListaComunicados);
                } else {
                        Collections.sort(listaComunicados, (c1, c2) -> 
                        c1.compareTo(c2, OrdComunicado.TITULO, tituloSortState == 1));
                }
                renderizarTabla();
            }
        });
        headerRow.addView(textViewHeaderTitulo);

        TextView textViewHeaderFecha = new TextView(this);
        String fechaHeaderText = "Fecha";
        if (fechaSortState == 1) {
            fechaHeaderText += " ↑";
        } else if (fechaSortState == 2) {
            fechaHeaderText += " ↓";
        }
        textViewHeaderFecha.setText(fechaHeaderText);
        textViewHeaderFecha.setTypeface(null, Typeface.BOLD);
        textViewHeaderFecha.setPadding(16,8,16,8);
        textViewHeaderFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tituloSortState = 0; 
                fechaSortState = (fechaSortState + 1) % 3;

                if (fechaSortState == 0) {
                    listaComunicados = new ArrayList<>(originalListaComunicados);
                } else {
                    Collections.sort(listaComunicados, (c1, c2) -> 
                        c1.compareTo(c2, OrdComunicado.FECHA, fechaSortState == 1));
                }
                renderizarTabla();
            }
        });
        headerRow.addView(textViewHeaderFecha);

        tablaMisComunicados.addView(headerRow); 


        for(Comunicado comunicado: listaComunicados){
            TableRow comunicadoRow = new TableRow(this);

            TextView textViewTitulo = new TextView(this);
            String titulo = comunicado.getTitulo() != null ? comunicado.getTitulo() : "Título N/A";
            textViewTitulo.setText(titulo);
            textViewTitulo.setPadding(16,8,16,8); 
            comunicadoRow.addView(textViewTitulo);


            TextView textViewFecha = new TextView(this);
            String fecha = comunicado.getFecha() != null ? comunicado.getFecha() : "Fecha N/A";
            textViewFecha.setText(fecha);
            textViewFecha.setPadding(16,8,16,8); 
            comunicadoRow.addView(textViewFecha);

            tablaMisComunicados.addView(comunicadoRow);
        }
    }
}
