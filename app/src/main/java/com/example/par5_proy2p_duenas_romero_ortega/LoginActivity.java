package com.example.par5_proy2p_duenas_romero_ortega;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import Models.Usuario;
import Exceptions.CredecialesInvalidasException;
import Persistencia.ComunicadoRepositorio;
import Persistencia.ManejadorArchivo;
import Persistencia.UsuarioRepositorio;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button btnlogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btnlogin = findViewById(R.id.btnlogin);

        UsuarioRepositorio.cargarUsuariosAssets(this);

        btnlogin.setOnClickListener(v -> iniciarSesion());
    }

    private void iniciarSesion() {
        String username = this.username.getText().toString().trim();
        String password = this.password.getText().toString().trim();

        if (username.trim().isEmpty()) {
            Toast.makeText(this, getString(R.string.username_error), Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.trim().isEmpty()) {
            Toast.makeText(this, getString(R.string.pass_error), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Usuario usuario = Usuario.autenticar(username, password);
            Toast.makeText(this, "Bienvenido " + usuario.getUser(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            Usuario.logged_user_id = usuario.getId();
            startActivity(intent);
            finish();

        } catch (CredecialesInvalidasException e) {
            Toast.makeText(this, e.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }
}














