package Persistencia;

import android.content.Context;
import Models.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para gestionar las operaciones de persistencia de usuarios.
 * Proporciona métodos para cargar usuarios desde assets y realizar búsquedas.
 * Implementa el patrón Singleton para garantizar una única instancia en memoria.
 */

public final class UsuarioRepositorio {
    /** Nombre del archivo que contiene los datos de usuarios en assets */
    private static final String USUARIOS_TXT = "usuarios.txt";
    
    /** Caché en memoria de los usuarios cargados */
    private static final List<Usuario> usuariosCache = new ArrayList<>();


    private UsuarioRepositorio() {
    }

    /**
     * Carga los usuarios desde el archivo de assets y los guarda en memoria caché.
     * El archivo debe estar en formato CSV con las columnas: id,usuario,contraseña
     * 
     * @param context Contexto de la aplicación para acceder a los assets
     * @return Lista de usuarios cargados, nunca nula
     */
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

    /**
     * Busca un usuario por su nombre de usuario (case insensitive).
     * @param username Nombre de usuario a buscar
     * @return Usuario encontrado o null si no existe
     */
    public static Usuario buscarUsuarioPorUsername(String username) {
        for (Usuario u : usuariosCache) {
            if (u.getUser().equalsIgnoreCase(username)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Busca un usuario por su ID único.
     * @param userId ID del usuario a buscar
     * @return Usuario encontrado o null si no existe
     */
    public static Usuario buscarUsuarioPorId(String userId) {
        for (Usuario u : usuariosCache) {
            if (u.getId().equals(userId)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Obtiene una lista de IDs de todos los usuarios, colocando primero el ID del usuario actual.
     * @param currentUserId ID del usuario actual (se colocará primero en la lista)
     * @return Lista de IDs de usuarios
     */
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
