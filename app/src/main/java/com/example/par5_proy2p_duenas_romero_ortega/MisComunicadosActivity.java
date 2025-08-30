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
    private Button btnGuardarLista;
    private List<Comunicado> listaComunicados;

    private boolean tituloAscending = true;
    private boolean fechaAscending = true;
    private boolean tituloOrdenado = false;
    private boolean fechaOrdenado = false;

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

        
        listaComunicados = new ArrayList<>(DatosDePruebaComunicados.obtenerListaDePrueba());


        renderizarTabla();
        

    }

    private void renderizarTabla() {
        int childCount = tablaMisComunicados.getChildCount();
        if (childCount > 1) {
            tablaMisComunicados.removeViews(1, childCount - 1);
        }

        tablaMisComunicados.removeAllViews();
        
        TableRow headerRow = new TableRow(this);
        TableRow.LayoutParams headerParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );

        TextView textViewHeaderTitulo = new TextView(this);
        String tituloHeaderText = "Título";
        if (tituloOrdenado) {
            tituloHeaderText += tituloAscending ? " ↑" : " ↓"; // ↑ : ↓
        }
        textViewHeaderTitulo.setText(tituloHeaderText);
        textViewHeaderTitulo.setTypeface(null, Typeface.BOLD);
        textViewHeaderTitulo.setPadding(16,8,16,8);
        textViewHeaderTitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tituloOrdenado) {
                    tituloAscending = !tituloAscending;
                } else {
                    tituloAscending = true; 
                }
                tituloOrdenado = true;
                fechaOrdenado = false;
                Collections.sort(listaComunicados, new Comparator<Comunicado>() {
                    @Override
                    public int compare(Comunicado c1, Comunicado c2) {
                        String titulo1 = c1.getTitulo() != null ? c1.getTitulo() : "";
                        String titulo2 = c2.getTitulo() != null ? c2.getTitulo() : "";
                        if (tituloAscending) {
                            return titulo1.compareToIgnoreCase(titulo2);
                        } else {
                            return titulo2.compareToIgnoreCase(titulo1);
                        }
                    }
                });
                renderizarTabla();
            }
        });
        headerRow.addView(textViewHeaderTitulo);

        TextView textViewHeaderFecha = new TextView(this);
        String fechaHeaderText = "Fecha";
        if (fechaOrdenado) {
            fechaHeaderText += fechaAscending ? " ↑" : " ↓"; // ↑ : ↓
        }
        textViewHeaderFecha.setText(fechaHeaderText);
        textViewHeaderFecha.setTypeface(null, Typeface.BOLD);
        textViewHeaderFecha.setPadding(16,8,16,8);
        textViewHeaderFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fechaOrdenado) {
                    fechaAscending = !fechaAscending;
                } else {
                    fechaAscending = true;
                }
                fechaOrdenado = true;
                tituloOrdenado = false;
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
                            return fechaAscending ? 1 : -1; 
                        }
                        if (date2 == null) {
                            return fechaAscending ? -1 : 1;
                        }

                        if (fechaAscending) {
                            return date1.compareTo(date2);
                        } else {
                            return date2.compareTo(date1);
                        }
                    }
                });
                renderizarTabla();
            }
        });
        headerRow.addView(textViewHeaderFecha);

        tablaMisComunicados.addView(headerRow); 


        TableRow.LayoutParams cellParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );

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
