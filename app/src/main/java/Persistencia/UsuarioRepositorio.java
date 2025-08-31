package Persistencia;

import android.content.Context;
import Models.Usuario;

import java.util.ArrayList;
import java.util.List;

public final class UsuarioRepositorio {
    private static final String USUARIOS_TXT = "usuarios.txt";
    private static final List<Usuario> usuariosCache = new ArrayList<>();

    private UsuarioRepositorio() {
    }

    // Carga usuarios desde el archivo assets/usuarios.txt y los guarda en memoria.

    public static List<Usuario> cargarUsuariosAssets(Context context) {
        List<String> lineas = ManejadorArchivo.leerArchivoDeAssets(context, USUARIOS_TXT);
        usuariosCache.clear();
        for (String linea : lineas) {
            String[] parts = linea.split(",");
            if (parts.length >= 3) {
                String id = parts[0].trim();
                String user = parts[1].trim();
                String pass = parts[2].trim();
                usuariosCache.add(new Usuario(id, user, pass));
            }
        }
        return usuariosCache;
    }

    // Busca un usuario por userName.

    public static Usuario buscarUsuarioPorUsername(String username) {
        for (Usuario u : usuariosCache) {
            if (u.getUser().equalsIgnoreCase(username)) {
                return u;
            }
        }
        return null;
    }

    // Buscar un usario por id
    public static Usuario buscarUsuarioPorId(String userId) {
        for (Usuario u : usuariosCache) {
            if (u.getId().equals(userId)) {
                return u;
            }
        }
        return null;
    }

    public static List<String> obtenerListaIdsUsuarios(String currentUserId) {
        List<String> ids = new ArrayList<>();
        List<String> otrosIds = new ArrayList<>();
        
        for (Usuario u : usuariosCache) {
            String id = u.getId();
            if (currentUserId != null && id.equals(currentUserId)) {
                ids.add(0, id); // Añade al principio si es el usuario actual
            } else {
                otrosIds.add(id);
            }
        }
        
        // Si no se encontró el currentUserId en la caché, lo añadimos de todos modos
        if (currentUserId != null && !ids.contains(currentUserId)) {
            ids.add(0, currentUserId);
        }
        
        // Añadir el resto de IDs
        ids.addAll(otrosIds);
        
        return ids;
    }
}
