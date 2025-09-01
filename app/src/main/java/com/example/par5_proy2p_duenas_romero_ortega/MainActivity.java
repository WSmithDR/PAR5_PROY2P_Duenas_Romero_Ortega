package com.example.par5_proy2p_duenas_romero_ortega;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private RadioGroup editBtn_grupo;
    private Button editBtn_ver;
    private Button editBtn_publicar;
    private Button editBtn_tablero;
    private Button editBtn_cerrarSesion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.textoprueba), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editBtn_grupo = findViewById(R.id.btn_grupo);
        editBtn_ver = findViewById(R.id.btn_verComunicados);
        editBtn_publicar = findViewById(R.id.btn_publicarComunicados);
        editBtn_tablero = findViewById(R.id.btn_tablero);
        editBtn_cerrarSesion = findViewById(R.id.btn_cerrarSesion);

        editBtn_ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, VerComunicadosActivity.class));
            }
        });

        editBtn_publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PublicarComunicadoActivity.class));
            }
        });

        editBtn_tablero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MisComunicadosActivity.class));
            }
        });
        editBtn_cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

    }
}