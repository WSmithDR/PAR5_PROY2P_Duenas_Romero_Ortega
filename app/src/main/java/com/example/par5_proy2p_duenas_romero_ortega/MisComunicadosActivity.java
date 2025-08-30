package com.example.par5_proy2p_duenas_romero_ortega;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import Models.Comunicado;
import Utils.DatosDePruebaComunicados;

public class MisComunicadosActivity extends AppCompatActivity {
    private ImageButton btnVolver;
    private TableLayout tablaMisComunicados;
    private Button btnOrdernarTitulo;
    private Button btnOrdenarFecha;
    private Button btnGuardarLista;
    private List<Comunicado> listaComunicados;

    private static final String DATE_FORMAT_PATTERN = "dd/MM/yyyy";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.getDefault());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mis_comunicados);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/

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
        this.btnOrdernarTitulo = findViewById(R.id.ordenarPorTituloBtn);
        this.btnOrdenarFecha = findViewById(R.id.ordenarPorFechaBtn);
        this.btnGuardarLista = findViewById(R.id.btnGuardarLista);

        
        //listaComunicados = ComunicadoRepositorio.cargarComunicados(this);
        listaComunicados = new ArrayList<>(DatosDePruebaComunicados.obtenerListaDePrueba());


        renderizarTabla();

        btnOrdernarTitulo.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Collections.sort(listaComunicados, new Comparator<Comunicado>() {
                        @Override
                        public int compare(Comunicado c1, Comunicado c2) {
                            String titulo1 = c1.getTitulo() != null ? c1.getTitulo() : "";
                            String titulo2 = c2.getTitulo() != null ? c2.getTitulo() : "";
                            return titulo1.compareToIgnoreCase(titulo2);
                        }
                    });
                    renderizarTabla();
                }
            }
        );
        
        if (btnOrdenarFecha != null) { // Comprobación de nulidad
            btnOrdenarFecha.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Collections.sort(listaComunicados, new Comparator<Comunicado>() {
                            @Override
                            public int compare(Comunicado c1, Comunicado c2) {
                                Date date1 = null;
                                Date date2 = null;

                                String fechaStr1 = c1.getFecha();
                                String fechaStr2 = c2.getFecha();

                                if (fechaStr1 != null && !fechaStr1.isEmpty()) {
                                    try {
                                        date1 = sdf.parse(fechaStr1);
                                    } catch (ParseException e) {
                                        Log.e("MisComunicadosActivity", "Error parseando fecha: " + fechaStr1, e);
                                    }
                                }

                                if (fechaStr2 != null && !fechaStr2.isEmpty()) {
                                    try {
                                        date2 = sdf.parse(fechaStr2);
                                    } catch (ParseException e) {
                                        Log.e("MisComunicadosActivity", "Error parseando fecha: " + fechaStr2, e);
                                    }
                                }

                                if (date1 == null && date2 == null) {
                                    return 0;
                                }
                                if (date1 == null) {
                                    return 1;
                                }
                                if (date2 == null) {
                                    return -1;
                                }
                                return date1.compareTo(date2);
                            }
                        });
                        renderizarTabla();
                    }
                }
            );
        }
        //btnGuardarLista.setOnClickListener();

    }

    private void renderizarTabla() {
        int childCount = tablaMisComunicados.getChildCount();
        if (childCount > 1) {
            tablaMisComunicados.removeViews(1, childCount - 1);
        }

        if (tablaMisComunicados.getChildCount() > 0) {
            View header = tablaMisComunicados.getChildAt(0);
             tablaMisComunicados.removeViewAt(0);
        }
        
        TableRow headerRow = new TableRow(this);
        TableRow.LayoutParams headerParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        headerParams.setMargins(8, 8, 8, 8);

        TextView textViewHeaderTitulo = new TextView(this);
        textViewHeaderTitulo.setText("Título");
        textViewHeaderTitulo.setTypeface(null, Typeface.BOLD);
        textViewHeaderTitulo.setPadding(8,8,8,8);
        headerRow.addView(textViewHeaderTitulo);

        TextView textViewHeaderFecha = new TextView(this);
        textViewHeaderFecha.setText("Fecha");
        textViewHeaderFecha.setTypeface(null, Typeface.BOLD);
        textViewHeaderFecha.setPadding(8,8,8,8);
        headerRow.addView(textViewHeaderFecha);

        tablaMisComunicados.addView(headerRow, 0);


        TableRow.LayoutParams cellParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        cellParams.setMargins(8, 8, 8, 8);

        for(Comunicado comunicado: listaComunicados){
            TableRow comunicadoRow = new TableRow(this);

            TextView textViewTitulo = new TextView(this);
            String titulo = comunicado.getTitulo() != null ? comunicado.getTitulo() : "Título N/A";
            textViewTitulo.setText(titulo);
            textViewTitulo.setPadding(8,8,8,8);
            comunicadoRow.addView(textViewTitulo);


            TextView textViewFecha = new TextView(this);
            String fecha = comunicado.getFecha() != null ? comunicado.getFecha() : "Fecha N/A";
            textViewFecha.setText(fecha);
            textViewFecha.setPadding(8,8,8,8);
            comunicadoRow.addView(textViewFecha);

            tablaMisComunicados.addView(comunicadoRow);
        }
    }
}
