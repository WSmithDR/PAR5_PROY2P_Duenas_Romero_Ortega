package com.example.par5_proy2p_duenas_romero_ortega;

import android.content.Intent;
import android.graphics.Typeface; // Importado para el estilo del encabezado
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import Models.Anuncio; // Importar Anuncio
import Models.Comunicado;
import Models.Evento;   // Importar Evento
import Enums.NivelUrgencia; // Importar NivelUrgencia (ajustar si es necesario)
import Persistencia.ComunicadoRepositorio;
// import Persistencia.ComunicadoRepositorio; // Comentado para la prueba

public class MisComunicadosActivity extends AppCompatActivity {
    private ImageButton btnVolver;
    private TableLayout tablaMisComunicados;
    private Button btnOrdernarTitulo;
    private Button btnGuardarLista;


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

        TableRow headerRow = new TableRow(this);

        TextView textViewHeaderTitulo = new TextView(this);
        textViewHeaderTitulo.setText("Titulo");
        textViewHeaderTitulo.setTypeface(null, Typeface.BOLD);
        headerRow.addView(textViewHeaderTitulo);

        TextView textViewHeaderFecha = new TextView(this);
        textViewHeaderFecha.setText("Fecha");
        textViewHeaderFecha.setTypeface(null, Typeface.BOLD);
        TableRow.LayoutParams headerFechaParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        textViewHeaderFecha.setLayoutParams(headerFechaParams);
        headerRow.addView(textViewHeaderFecha);

        tablaMisComunicados.addView(headerRow);

        for(Comunicado comunicado: ComunicadoRepositorio.cargarComunicados(this)){
            TableRow comunicadoRow = new TableRow(this);

            TextView textViewTitulo = new TextView(this);
            String titulo = comunicado.getTitulo() != null ? comunicado.getTitulo() : "Título N/A";
            textViewTitulo.setText(titulo);
            comunicadoRow.addView(textViewTitulo);


            TextView textViewFecha = new TextView(this);
            String fecha = comunicado.getFecha() != null ? comunicado.getFecha() : "Fecha N/A";
            textViewFecha.setText(fecha);
            textViewFecha.setLayoutParams(headerFechaParams); // Reutilizar o crear nuevos params
            comunicadoRow.addView(textViewFecha); 

            tablaMisComunicados.addView(comunicadoRow);
        }
        this.btnOrdernarTitulo = findViewById(R.id.ordenarPorTituloBtn);
        this.btnGuardarLista = findViewById(R.id.btnGuardarLista);

        btnOrdernarTitulo.setOnClickListener(v -> ordenarPorTitulo());
        btnGuardarLista.setOnClickListener(v -> guardarListaSerializada());

    }

    public void ordenarPorTitulo(){}

    public void guardarListaSerializada(){}
}
