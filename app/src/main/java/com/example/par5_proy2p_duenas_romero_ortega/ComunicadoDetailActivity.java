package com.example.par5_proy2p_duenas_romero_ortega;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import Enums.TipoAudiencia;
import Models.Comunicado;
import Persistencia.ComunicadoRepositorio;
import Utils.ImageUtils;

public class ComunicadoDetailActivity extends AppCompatActivity {

    private int comunicadoID;
    private TextView tituloComunicado;
    private TextView areaComunicado;
    private TextView fechaComunicado;
    private TextView tipoComunicado;
    private TextView audienciaComunicado;
    private ImageView comunicadoImage;
    private TextView descripcionComunicado;
    private TextView lugarEvento;
    private TextView nivelUrgenciaAnuncio;

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
                this::volver
        );

        tituloComunicado = findViewById(R.id.comunicado_detail_title);
        areaComunicado = findViewById(R.id.detail_area);
        fechaComunicado = findViewById(R.id.detail_fecha);
        tipoComunicado = findViewById(R.id.detail_tipo);
        audienciaComunicado = findViewById(R.id.detail_audiencia);
        comunicadoImage = findViewById(R.id.detail_imagen);
        descripcionComunicado = findViewById(R.id.detail_descripcion);
        lugarEvento = findViewById(R.id.detail_lugar);
        nivelUrgenciaAnuncio = findViewById(R.id.detail_urgencia);





        Comunicado comunicado = ComunicadoRepositorio.getComunicadoByID(comunicadoID);

        tituloComunicado.setText(comunicado.getTitulo());
        areaComunicado.setText(comunicado.getArea());
        fechaComunicado.setText(comunicado.getFecha());
        tipoComunicado.setText(comunicado.getTipo().toString());

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
        audienciaComunicado.setText(String.join(", ", audiencia));
        //audienciaComunicado.setText(comunicado.getAudiencia());
        descripcionComunicado.setText(comunicado.getDescripcion());
        //lugarEvento.setText(comunicado.getLugar());
        //nivelUrgenciaAnuncio.setText(comunicado.getUrgencia());
        //comunicadoImage.setImageResource(comunicado.getNombreArchivoImagen());
        //imagen.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Uri uriImagen = ImageUtils.obtenerImagenUri(this,comunicado.getNombreArchivoImagen());
        comunicadoImage.setImageURI(uriImagen);
        comunicadoImage.setAdjustViewBounds(true);


    }

    private  void volver(View view){
        Intent intent = new Intent(this, MisComunicadosActivity.class);
        startActivity(intent);
    }
}