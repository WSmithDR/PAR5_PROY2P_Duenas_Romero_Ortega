package com.example.par5_proy2p_duenas_romero_ortega;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class VerComunicadosActivity extends AppCompatActivity {
    private Button btn_selFecha;
    private TextView selFecha;
    private Button btn_volverVerCom;


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
    }
}