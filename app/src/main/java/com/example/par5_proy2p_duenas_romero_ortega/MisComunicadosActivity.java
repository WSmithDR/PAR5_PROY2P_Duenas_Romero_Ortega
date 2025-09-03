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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import Enums.OrdComunicado;
import Interfaces.VerComunicadoDetails;
import Models.Comunicado;
import Models.Usuario;
import Persistencia.ComunicadoRepositorio;
import Persistencia.PersistenciaOrdenamiento;
import Utils.DatosDePruebaComunicados;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.widget.Toast;
import android.graphics.Color;


public class MisComunicadosActivity extends AppCompatActivity implements VerComunicadoDetails {
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

        btnGuardarLista.setOnClickListener(v -> guardarListaComunicadosSerializados());
    }
    /**
     * Método llamado cuando la actividad se reanuda. Carga el estado de ordenamiento guardado.
     */
    @Override
    protected void onResume() {
        super.onResume();
        cargarEstadoOrdenamiento();
    }

    /**
     * Método llamado cuando la actividad se pausa. Guarda el estado actual del ordenamiento.
     */
    @Override
    protected void onPause() {
        super.onPause();
        guardarEstadoOrdenamiento();
    }

    /**
     * Guarda el estado actual del ordenamiento en las preferencias compartidas.
     */
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

    /**
     * Carga el estado de ordenamiento desde las preferencias compartidas
     * y aplica el ordenamiento a la lista de comunicados.
     */
    private void cargarEstadoOrdenamiento() {
        
        PersistenciaOrdenamiento.cargarPreferenciasOrdenamiento(this, Usuario.logged_user_id, this);
        
        if (ordenPrimario != null && !listaComunicados.isEmpty()) {
            listaComunicados.sort((c1, c2) -> c1.compareTo(
                    c2,
                    ordenPrimario.criterio,
                    ordenPrimario.esAscendente(),
                    (ordenSecundario != null && ordenSecundario.estaActivo()) ? ordenSecundario.criterio : null,
                    ordenSecundario == null || !ordenSecundario.estaActivo() || ordenSecundario.esAscendente()));
            
            actualizarTextoEncabezado();
            renderizarTabla();
        }
    }

    /**
     * Configura los criterios de ordenamiento primario y secundario.
     * @param criterioPrimario Criterio de ordenamiento principal
     * @param estadoPrimario Estado del ordenamiento principal (0: ninguno, 1: ascendente, 2: descendente)
     * @param criterioSecundario Criterio de ordenamiento secundario (puede ser null)
     * @param estadoSecundario Estado del ordenamiento secundario
     */
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

    /**
     * Inicializa la fila de encabezado de la tabla y configura los listeners
     * para ordenar al hacer clic en los encabezados.
     */
    private void initHeader() {
        headerRow = findViewById(R.id.headerRow);
        headerTitulo = findViewById(R.id.headerTitulo);
        headerFecha = findViewById(R.id.headerFecha);

        headerTitulo.setOnClickListener(v -> actualizarCriterioOrdenamiento(OrdComunicado.TITULO));
        headerFecha.setOnClickListener(v -> actualizarCriterioOrdenamiento(OrdComunicado.FECHA));
    }

    /**
     * Actualiza los criterios de ordenamiento cuando se hace clic en un encabezado.
     * @param criterio Criterio de ordenamiento seleccionado
     */
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
            listaComunicados.sort((c1, c2) -> c1.compareTo(
                    c2,
                    ordenPrimario.criterio,
                    ordenPrimario.esAscendente(),
                    (ordenSecundario != null && ordenSecundario.estaActivo()) ? ordenSecundario.criterio : null,
                    ordenSecundario == null || !ordenSecundario.estaActivo() || ordenSecundario.esAscendente()));
        } else {
            listaComunicados = new ArrayList<>(originalListaComunicados);
        }

        actualizarTextoEncabezado();
        renderizarTabla();
    }

    private void actualizarTextoEncabezado() {
        String tituloText = getString(R.string.titulo);
        String fechaText = getString(R.string.fecha);

        headerTitulo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        headerFecha.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        if (ordenPrimario != null && ordenPrimario.estaActivo()) {
            int iconRes = ordenPrimario.esAscendente() ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float;
            if (ordenPrimario.criterio == OrdComunicado.TITULO) {
                headerTitulo.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconRes, 0);
            } else if (ordenPrimario.criterio == OrdComunicado.FECHA) {
                headerFecha.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconRes, 0);
            }
        }

        if (ordenSecundario != null && ordenSecundario.estaActivo()) {
            int iconSecRes = ordenSecundario.esAscendente() ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float;
            if (ordenSecundario.criterio == OrdComunicado.TITULO) {
                tituloText = "*" + getString(R.string.titulo);
                headerTitulo.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconSecRes, 0);
            } else if (ordenSecundario.criterio == OrdComunicado.FECHA) {
                fechaText = "*" + getString(R.string.fecha);
                headerFecha.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconSecRes, 0);
            }
        }

        headerTitulo.setText(tituloText);
        headerFecha.setText(fechaText);
    }

    /**
     * Renderiza la tabla de comunicados con los datos actuales.
     * Crea dinámicamente las filas de la tabla para cada comunicado.
     */
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
            tituloView.setText(comunicado.getTitulo() != null ? comunicado.getTitulo() : "N/A");
            row.addView(tituloView);
            tituloView.setTextSize(20);
            tituloView.setTextColor(getColor(R.color.texto));


            TextView fechaView = new TextView(this);
            fechaView.setLayoutParams(new TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1f));
            fechaView.setPadding(16, 16, 16, 16);
            fechaView.setText(comunicado.getFecha() != null ? comunicado.getFecha() : "N/A");
            fechaView.setTextSize(20);
            row.addView(fechaView);
            fechaView.setTextColor(getColor(R.color.texto));


            contentTableLayout.addView(row);

            row.setOnClickListener(v -> showComunicadoDetails(MisComunicadosActivity.this, comunicado));
        }
    }



    /**
     * Guarda la lista de comunicados actual en un archivo.
     * Muestra un mensaje de éxito o error al finalizar.
     */
    private void guardarListaComunicadosSerializados() {
        File file = new File(getFilesDir(), "comunicados_" + Usuario.logged_user_id + ".dat");
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(listaComunicados);
            Toast.makeText(this, R.string.lista_guardada, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, R.string.error_guardar_lista, Toast.LENGTH_SHORT).show();
        }
    }

}
