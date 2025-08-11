package com.example.par5_proy2p_duenas_romero_ortega;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Login extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button btnlogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            username = findViewById(R.id.username);
            password = findViewById(R.id.password);
            btnlogin = findViewById(R.id.btnlogin);
            btnlogin.setOnClickListener(View -> {
                String user = username.getText().toString().trim();
                String pass = password.getText().toString().trim();
                try {
                    if (validarCredenciales(user, pass)) {
                        Intent i = new Intent(this, OptionsActivity.class);
                        i.putExtra("usuario", user);
                        startActivity(i);
                        finish();
                    } else {
                        throw new CredecialesInvalidasException("Usuario o la contraseña son incorrectos");
                    }
                } catch (CredecialesInvalidasException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            return insets;
        });
    }

    private boolean validarCredenciales(String username, String password) {
        username=username.trim();
        password=password.trim();
        try (InputStream is = getAssets().open("usuarios.txt")){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 3) {
                String id = parts[0].trim();
                String u = parts[1].trim();
                String p = parts[2].trim();
                if (u.equals(username) && p.equals(password)) {
                    return true;
                }
            }
        }
    } catch(IOException e2){
            Toast.makeText(this, "Problemas técnicos. Estamos resolviendo", Toast.LENGTH_LONG).show();
        e2.printStackTrace();
    }
        return false;
    }
}













