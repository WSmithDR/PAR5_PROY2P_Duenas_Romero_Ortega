package com.example.par5_proy2p_duenas_romero_ortega;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MisComunicadosActivity extends AppCompatActivity {
    /*private TableLayout tablaMisComunicados;
    private Button btnOrdernarTitulo;
    private Button btnGuardarLista;
    private Button btnVolver;*/

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
    }

    public void ordenarPorTitulo(){}

    public void guardarListaSerializada(){}
}