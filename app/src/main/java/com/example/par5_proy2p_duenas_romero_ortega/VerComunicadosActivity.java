package com.example.par5_proy2p_duenas_romero_ortega;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Models.Comunicado;
import Models.Evento;
import Models.Usuario;
import Persistencia.ComunicadoRepositorio;
import Utils.DatosDePruebaComunicados;

public class VerComunicadosActivity extends AppCompatActivity {
    private Button btn_selFecha;
    private TextView selFecha;
    private Button btn_volverVerCom;
    private List<Comunicado> comunicados;
    public ArrayList<Comunicado> listaFiltrada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ver_comunicados);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.textoprueba), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_selFecha = findViewById(R.id.Btn_selFecha);
        selFecha = findViewById(R.id.txtFecha);
        btn_volverVerCom = findViewById(R.id.Btn_volverCom);



        //Volver al activity anterior
        btn_volverVerCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VerComunicadosActivity.this, MainActivity.class));
            }
        });

        //Seleccionar fecha
        btn_selFecha.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, y, m, d) -> {
                        String fecha = String.format("%02d/%02d/%04d", d, m + 1, y);
                        selFecha.setText(fecha);
                    }, year, month, day);
            datePickerDialog.show();
        });
    }

    //Acceder al archivo de comunicados
    private List<Comunicado> comunicadosFiltrados(){
        comunicados = DatosDePruebaComunicados.obtenerListaDePrueba(Usuario.logged_user_id);
        listaFiltrada = new ArrayList<>();
        if (selFecha.getText().toString().isEmpty()){
            for(Comunicado comunicado: comunicados) {
                if (comunicado instanceof Evento) {
                    Evento evento = (Evento) comunicado;
                    if (evento.getFecha() != null && evento.getFecha().equals(selFecha.getText().toString())) {
                        this.listaFiltrada.add(evento);
                    }
                }
            }
        }else{
            this.listaFiltrada.addAll(comunicados);
        }
        return this.listaFiltrada;
    }

    //Mostrar comunicados filtrados
    private void mostrarComunicadosFiltrados(List<Comunicado> comunicadosF){
        LinearLayout contenedorCom = findViewById(R.id.layout_Com);
        contenedorCom.removeAllViews();
        for(Comunicado comunicado: comunicadosF){
            LinearLayout comunicadoLayout = new LinearLayout(this);
            comunicadoLayout.setOrientation(LinearLayout.VERTICAL);

            TextView titulo = new TextView(this);
            titulo.setText(comunicado.getTitulo());

            ImageView imagen = new ImageView(this);
            int idImagen = this.getResources().getIdentifier(comunicado.getNombreArchivoImagen(), "drawable", this.getPackageName());
            imagen.setImageResource(idImagen);

            TextView descripcion = new TextView(this);
            descripcion.setText(comunicado.getDescripcion());

            contenedorCom.addView(comunicadoLayout);
            comunicadoLayout.addView(titulo);
            comunicadoLayout.addView(imagen);
            comunicadoLayout.addView(descripcion);
        }
    }
}