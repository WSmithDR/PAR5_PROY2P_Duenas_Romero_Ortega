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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import Enums.OrdComunicado;
import Models.Comunicado;
import Models.Usuario;
import Persistencia.ComunicadoRepositorio;
import Persistencia.PersistenciaOrdenamiento;
import Utils.DatosDePruebaComunicados;

public class MisComunicadosActivity extends AppCompatActivity {
    private ImageButton btnVolver;
    private TableLayout contentTableLayout;
    private Button btnGuardarLista;
    private List<Comunicado> listaComunicados;
    private List<Comunicado> originalListaComunicados;

    private Comunicado.EstadoOrdenamiento ordenPrimario = null;
    private Comunicado.EstadoOrdenamiento ordenSecundario = null;
    private OrdComunicado criterioPrimario = null;

    private TableRow headerRow;
    private TextView headerTitulo;
    private TextView headerFecha;

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
                });

        this.contentTableLayout = findViewById(R.id.contentTableLayout);
        this.btnGuardarLista = findViewById(R.id.btnGuardarLista);

        //originalListaComunicados = new ArrayList<>(DatosDePruebaComunicados.obtenerListaDePrueba(Usuario.logged_user_id));
        originalListaComunicados = ComunicadoRepositorio.cargarComunicados(this, Usuario.logged_user_id);
        //Log.e("originalListaComunicados****************************: ",originalListaComunicados.toString());
        listaComunicados = new ArrayList<>(originalListaComunicados);

        renderizarTabla();

    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarEstadoOrdenamiento();
    }

    @Override
    protected void onPause() {
        super.onPause();
        guardarEstadoOrdenamiento();
    }

    private void guardarEstadoOrdenamiento() {
        if (ordenPrimario != null) {
            PersistenciaOrdenamiento.guardarPreferenciasOrdenamiento(
                    this,
                    Usuario.logged_user_id,
                    ordenPrimario.criterio,
                    ordenPrimario.estado,
                    ordenSecundario != null ? ordenSecundario.criterio : null,
                    ordenSecundario != null ? ordenSecundario.estado : -1);
        }
    }

    private void cargarEstadoOrdenamiento() {
        // Cargar el estado guardado
        PersistenciaOrdenamiento.cargarPreferenciasOrdenamiento(this, Usuario.logged_user_id, this);
        
        // Si hay un ordenamiento activo, aplicarlo a la lista
        if (ordenPrimario != null && !listaComunicados.isEmpty()) {
            Collections.sort(listaComunicados, (c1, c2) -> c1.compareTo(
                    c2,
                    ordenPrimario.criterio,
                    ordenPrimario.esAscendente(),
                    (ordenSecundario != null && ordenSecundario.estaActivo()) ? ordenSecundario.criterio : null,
                    (ordenSecundario != null && ordenSecundario.estaActivo()) ? ordenSecundario.esAscendente() : true));
            
            actualizarTextoEncabezado();
            renderizarTabla();
        }
    }

    public void configurarOrdenamiento(OrdComunicado criterioPrimario, int estadoPrimario,
            OrdComunicado criterioSecundario, int estadoSecundario) {
        this.ordenPrimario = new Comunicado.EstadoOrdenamiento(criterioPrimario, estadoPrimario);
        this.criterioPrimario = criterioPrimario;

        if (criterioSecundario != null && estadoSecundario >= 0) {
            this.ordenSecundario = new Comunicado.EstadoOrdenamiento(criterioSecundario, estadoSecundario);
        }

        actualizarTextoEncabezado();
        if (listaComunicados != null && !listaComunicados.isEmpty()) {
            renderizarTabla();
        }
    }

    private void initHeader() {
        headerRow = findViewById(R.id.headerRow);
        headerTitulo = findViewById(R.id.headerTitulo);
        headerFecha = findViewById(R.id.headerFecha);

        headerTitulo.setOnClickListener(v -> actualizarCriterioOrdenamiento(OrdComunicado.TITULO));
        headerFecha.setOnClickListener(v -> actualizarCriterioOrdenamiento(OrdComunicado.FECHA));
    }

    private void actualizarCriterioOrdenamiento(OrdComunicado criterio) {
        if (criterioPrimario != null) {
            if (criterio == criterioPrimario) {
                ordenPrimario.estado = (ordenPrimario.estado + 1) % 3;

                if (ordenPrimario.estado == 0) {
                    ordenPrimario = null;
                    criterioPrimario = null;
                    if (ordenSecundario != null && ordenSecundario.estaActivo()) {
                        ordenPrimario = ordenSecundario;
                        criterioPrimario = ordenPrimario.criterio;
                        ordenSecundario = null;
                    }
                }
            } else {
                if (ordenSecundario != null && ordenSecundario.criterio == criterio) {
                    ordenSecundario.estado = (ordenSecundario.estado + 1) % 3;
                    if (ordenSecundario.estado == 0) {
                        ordenSecundario = null;
                    }
                } else {
                    ordenSecundario = new Comunicado.EstadoOrdenamiento(criterio, 1);
                }
            }
        } else {
            ordenPrimario = new Comunicado.EstadoOrdenamiento(criterio, 1);
            criterioPrimario = criterio;
        }

        if (ordenPrimario != null && ordenPrimario.estaActivo()) {
            Collections.sort(listaComunicados, (c1, c2) -> c1.compareTo(
                    c2,
                    ordenPrimario.criterio,
                    ordenPrimario.esAscendente(),
                    (ordenSecundario != null && ordenSecundario.estaActivo()) ? ordenSecundario.criterio : null,
                    (ordenSecundario != null && ordenSecundario.estaActivo()) ? ordenSecundario.esAscendente() : true));
        } else {
            listaComunicados = new ArrayList<>(originalListaComunicados);
        }

        actualizarTextoEncabezado();
        renderizarTabla();
    }

    private void actualizarTextoEncabezado() {
        String textoEncabezadoTitulo = getString(R.string.titulo);
        String textoEncabezadoFecha = getString(R.string.fecha);

        if (ordenPrimario != null && ordenPrimario.estaActivo()) {
            if (ordenPrimario.criterio == OrdComunicado.TITULO) {
                textoEncabezadoTitulo += ordenPrimario.esAscendente() ? " ↑" : " ↓";
            } else if (ordenPrimario.criterio == OrdComunicado.FECHA) {
                textoEncabezadoFecha += ordenPrimario.esAscendente() ? " ↑" : " ↓";
            }
        }

        if (ordenSecundario != null && ordenSecundario.estaActivo()) {
            if (ordenSecundario.criterio == OrdComunicado.TITULO) {
                textoEncabezadoTitulo = "*" + textoEncabezadoTitulo + (ordenSecundario.esAscendente() ? "↑" : "↓");
            } else if (ordenSecundario.criterio == OrdComunicado.FECHA) {
                textoEncabezadoFecha = "*" + textoEncabezadoFecha + (ordenSecundario.esAscendente() ? "↑" : "↓");
            }
        }

        headerTitulo.setText(textoEncabezadoTitulo);
        headerFecha.setText(textoEncabezadoFecha);
    }

    private void renderizarTabla() {
        contentTableLayout.removeAllViews();

        if (headerRow == null) {
            initHeader();
        }

        for (Comunicado comunicado : listaComunicados) {
            TableRow row = new TableRow(this);

            TextView tituloView = new TextView(this);
            tituloView.setLayoutParams(new TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1f));
            tituloView.setPadding(16, 16, 16, 16);
            tituloView.setText(comunicado.getTitulo() != null ? comunicado.getTitulo() : "Título N/A");
            row.addView(tituloView);

            TextView fechaView = new TextView(this);
            fechaView.setLayoutParams(new TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1f));
            fechaView.setPadding(16, 16, 16, 16);
            fechaView.setText(comunicado.getFecha() != null ? comunicado.getFecha() : "Fecha N/A");
            row.addView(fechaView);

            contentTableLayout.addView(row);
        }
    }
}
