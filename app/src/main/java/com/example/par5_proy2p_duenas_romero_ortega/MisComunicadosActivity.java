package com.example.par5_proy2p_duenas_romero_ortega;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView; // Importar TextView

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import Models.Comunicado;
import Persistencia.ComunicadoRepositorio;

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
        Context context = getApplicationContext();
        for(Comunicado comunicado: ComunicadoRepositorio.cargarComunicados(context)){
            TableRow comunicadoRow = new TableRow(this);


            TextView textViewTitulo = new TextView(this);
            textViewTitulo.setText(comunicado.getTitulo());
            comunicadoRow.addView(textViewTitulo);


            TextView textViewFecha = new TextView(this);
            textViewFecha.setText(comunicado.getFecha());
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