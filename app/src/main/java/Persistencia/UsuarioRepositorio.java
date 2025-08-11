package Persistencia;

import android.content.Context;
import Dominio.Usuario;
import Persistencia.ManejadorArchivo;


import java.util.ArrayList;
import java.util.List;

public class UsuarioRepositorio {
    private String usuariotxt;

    private List<Usuario> usuariosCache;

    public UsuarioRepositorio(String usuariotxt) {
        this.usuariotxt = usuariotxt;
        this.usuariosCache = new ArrayList<>();
    }

    //Carga usuarios desde el archivo assets/usuarios.txt y los guarda en memoria.

    public List<Usuario> cargarUsuariosAssets(Context context) {
        List<String> lineas = ManejadorArchivo.leerArchivo(context, usuariotxt);
        usuariosCache.clear();
        for (String linea : lineas) {
            String[] parts = linea.split(",");
            if (parts.length >= 3) {
                String id = parts[0].trim();
                String user = parts[1].trim();
                String pass=parts[2].trim();
                usuariosCache.add(new Usuario(user, pass));
            }
        }
        return usuariosCache;
    }

    //Busca un usuario por nombre.

    public Usuario buscarUsuario(String username) {
        for (Usuario u : usuariosCache) {
            if (u.getUser().equalsIgnoreCase(username)) {
                return u;
            }
        }
        return null;
    }
}

