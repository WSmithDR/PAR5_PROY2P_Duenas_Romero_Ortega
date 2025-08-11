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

    private UsuarioRepositorio usuarioRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btnlogin = findViewById(R.id.btnlogin);

        usuarioRepository = new UsuarioRepositorio("usuarios.txt");
        usuarioRepository.cargarUsuariosAssets(this);

        btnlogin.setOnClickListener(v -> iniciarSesion());
    }

    private void iniciarSesion() {
        String username = this.username.getText().toString().trim();
        String password = this.password.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese usuario y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Usuario usuario = Usuario.autenticar(username, password, usuarioRepository);
            Toast.makeText(this, "Bienvenido " + usuario.getUser(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, OptionsActivity.class);
            startActivity(intent);
            finish();

        } catch (CredecialesInvalidasException e) {
            Toast.makeText(this, "Usuario o contraseña son incorrectos: " , Toast.LENGTH_SHORT).show();
        }
    }
}














