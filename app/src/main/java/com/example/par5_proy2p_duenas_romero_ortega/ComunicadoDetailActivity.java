package com.example.par5_proy2p_duenas_romero_ortega;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import Enums.AreaComunicado;
import Enums.TipoAudiencia;
import Enums.TipoComunicado;
import Models.Anuncio;
import Models.Comunicado;
import Models.Evento;
import Persistencia.ComunicadoRepositorio;
import Utils.ImageUtils;

public class ComunicadoDetailActivity extends AppCompatActivity {

    private int comunicadoID;
    private TextView txtViewTituloComunicado;
    private TextView txtViewAreaComunicado;
    private TextView txtViewFechaComunicado;
    private TextView txtViewTipoComunicado;
    private TextView txtViewAudienciaComunicado;
    private ImageView imgViewComunicadoImage;
    private TextView txtViewDescripcionComunicado;
    private LinearLayout containerLugarEvento;
    private TextView txtViewLugarEvento;
    private LinearLayout containerNivelUrgenciaAnuncio;
    private TextView txtViewNivelUrgenciaAnuncio;

    private ImageButton btnVolver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comunicado_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        this.comunicadoID = intent.getIntExtra("comunicadoID", -1);

        if (this.comunicadoID == -1) {
            finish();
            return;
        }

        this.btnVolver = findViewById(R.id.comDetailBackButton);
        btnVolver.setOnClickListener(
                v->finish()
        );

        txtViewTituloComunicado = findViewById(R.id.comunicado_detail_title);
        txtViewAreaComunicado = findViewById(R.id.detail_area);
        txtViewFechaComunicado = findViewById(R.id.detail_fecha);
        txtViewTipoComunicado = findViewById(R.id.detail_tipo);
        txtViewAudienciaComunicado = findViewById(R.id.detail_audiencia);
        imgViewComunicadoImage = findViewById(R.id.detail_imagen);
        txtViewDescripcionComunicado = findViewById(R.id.detail_descripcion);
        containerLugarEvento = findViewById(R.id.lugar_container);
        txtViewLugarEvento = findViewById(R.id.detail_lugar);
        containerNivelUrgenciaAnuncio = findViewById(R.id.urgencia_container);
        txtViewNivelUrgenciaAnuncio = findViewById(R.id.detail_urgencia);





        Comunicado comunicado = ComunicadoRepositorio.getComunicadoByID(comunicadoID);

        txtViewTituloComunicado.setText(comunicado.getTitulo());


        String[] areasArray = getResources().getStringArray(R.array.areas_comunicado);
        AreaComunicado areaComunicado = comunicado.getArea();
        String areaSeleccionada;
        switch (areaComunicado){
            case ACADEMICO:
                areaSeleccionada=areasArray[0];
                break;
            case ADMINISTRATIVO:
                areaSeleccionada=areasArray[1];
                break;
            case CULTURAL:
                areaSeleccionada=areasArray[2];
                break;
            case GENERAL:
                areaSeleccionada=areasArray[3];
                break;
            default:
                areaSeleccionada = getString(R.string.no_specific_area);
                break;
        }

        txtViewAreaComunicado.setText(areaSeleccionada);
        txtViewFechaComunicado.setText(comunicado.getFecha());

        switch (comunicado.getTipo()){
            case ANUNCIO:
                txtViewTipoComunicado.setText(getString(R.string.anuncio));
                break;
            case EVENTO:
                txtViewTipoComunicado.setText(getString(R.string.evento));
                break;
            default:
                return;
        }

        List<String> audiencia = new ArrayList<>();
        for(TipoAudiencia tipoAudiencia: comunicado.getAudiencia()){
            if (tipoAudiencia == TipoAudiencia.ESTUDIANTES){
                audiencia.add(getString(R.string.estudiantes));
            }
            if (tipoAudiencia == TipoAudiencia.PROFESORES){
                audiencia.add(getString(R.string.profesores));
            }
            if (tipoAudiencia == TipoAudiencia.ADMINISTRATIVO){
                audiencia.add(getString(R.string.administrativo));
            }
        }
        txtViewAudienciaComunicado.setText(String.join(", ", audiencia));
        txtViewDescripcionComunicado.setText(comunicado.getDescripcion());



        Uri uriImagen = ImageUtils.obtenerImagenUri(this,comunicado.getNombreArchivoImagen());
        imgViewComunicadoImage.setImageURI(uriImagen);
        imgViewComunicadoImage.setAdjustViewBounds(true);
        imgViewComunicadoImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

        if (comunicado.getTipo() == TipoComunicado.EVENTO){
            Evento evento = (Evento)comunicado;
            String lugar = evento.getLugar();
            Log.e("*********************Lugar: ", lugar);
            containerLugarEvento.setVisibility(View.VISIBLE);
            txtViewLugarEvento.setText(lugar);
        }else{
            containerLugarEvento.setVisibility(View.GONE);
            txtViewLugarEvento.setVisibility(View.GONE);
        }

        if (comunicado.getTipo() == TipoComunicado.ANUNCIO){
            Anuncio anuncio = (Anuncio)comunicado;
            String nivelUrgencia = anuncio.getNivelUrgencia().name();
            Log.e("*********************NivelUrgencia: ", nivelUrgencia);
            containerNivelUrgenciaAnuncio.setVisibility(View.VISIBLE);
            txtViewNivelUrgenciaAnuncio.setText(nivelUrgencia);
        }else{
            containerNivelUrgenciaAnuncio.setVisibility(View.GONE);
            txtViewNivelUrgenciaAnuncio.setVisibility(View.GONE);
        }



    }
}