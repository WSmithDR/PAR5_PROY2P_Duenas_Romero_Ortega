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
    private TableLayout contentTableLayout;
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

        this.contentTableLayout = findViewById(R.id.contentTableLayout);
        this.btnGuardarLista = findViewById(R.id.btnGuardarLista);

        
        originalListaComunicados = new ArrayList<>(DatosDePruebaComunicados.obtenerListaDePrueba());
        listaComunicados = new ArrayList<>(originalListaComunicados);


        renderizarTabla();
        

    }

    private TableRow headerRow;
    private TextView headerTitulo;
    private TextView headerFecha;

    private void initHeader() {
        headerRow = findViewById(R.id.headerRow);
        headerTitulo = findViewById(R.id.headerTitulo);
        headerFecha = findViewById(R.id.headerFecha);

        // Set up click listeners for sorting
        headerTitulo.setOnClickListener(v -> {
            fechaSortState = 0; 
            tituloSortState = (tituloSortState + 1) % 3;
            updateSorting();
        });

        headerFecha.setOnClickListener(v -> {
            tituloSortState = 0;
            fechaSortState = (fechaSortState + 1) % 3;
            updateSorting();
        });
    }

    private void updateSorting() {
        if (tituloSortState > 0) {
            Collections.sort(listaComunicados, (c1, c2) -> 
                c1.compareTo(c2, OrdComunicado.TITULO, tituloSortState == 1));
        } else if (fechaSortState > 0) {
            Collections.sort(listaComunicados, (c1, c2) -> 
                c1.compareTo(c2, OrdComunicado.FECHA, fechaSortState == 1));
        } else {
            listaComunicados = new ArrayList<>(originalListaComunicados);
        }
        updateHeaderText();
        renderizarTabla();
    }

    private void updateHeaderText() {
        String tituloText = "Título";
        if (tituloSortState == 1) tituloText += " ↑";
        else if (tituloSortState == 2) tituloText += " ↓";
        headerTitulo.setText(tituloText);

        String fechaText = "Fecha";
        if (fechaSortState == 1) fechaText += " ↑";
        else if (fechaSortState == 2) fechaText += " ↓";
        headerFecha.setText(fechaText);
    }

    private void renderizarTabla() {
        // Clear the content table
        contentTableLayout.removeAllViews();
        
        // Initialize header if needed
        if (headerRow == null) {
            initHeader();
        }

        // Add content rows to the scrollable table
        for (Comunicado comunicado : listaComunicados) {
            TableRow row = new TableRow(this);
            
            // Título cell
            TextView tituloView = new TextView(this);
            tituloView.setLayoutParams(new TableRow.LayoutParams(
                0, 
                TableRow.LayoutParams.WRAP_CONTENT, 
                1f
            ));
            tituloView.setPadding(16, 16, 16, 16);
            tituloView.setText(comunicado.getTitulo() != null ? comunicado.getTitulo() : "Título N/A");
            row.addView(tituloView);
            
            // Fecha cell
            TextView fechaView = new TextView(this);
            fechaView.setLayoutParams(new TableRow.LayoutParams(
                0, 
                TableRow.LayoutParams.WRAP_CONTENT, 
                1f
            ));
            fechaView.setPadding(16, 16, 16, 16);
            fechaView.setText(comunicado.getFecha() != null ? comunicado.getFecha() : "Fecha N/A");
            row.addView(fechaView);
            
            // Add row to table
            contentTableLayout.addView(row);
            
            // Add divider
            View divider = new View(this);
            TableRow dividerRow = new TableRow(this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                1
            );
            params.setMargins(16, 0, 16, 0);
            divider.setLayoutParams(params);
            divider.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            dividerRow.addView(divider);
            contentTableLayout.addView(dividerRow);
        }
    }
}
