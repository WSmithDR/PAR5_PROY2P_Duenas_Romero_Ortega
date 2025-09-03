package Persistencia;

import android.content.Context;
import android.util.Log;

import com.example.par5_proy2p_duenas_romero_ortega.MisComunicadosActivity;

import java.util.List;

import Enums.OrdComunicado;

/**
 * Clase de utilidad para gestionar la persistencia de las preferencias de ordenamiento
 * de comunicados por usuario. Almacena y recupera las preferencias de ordenamiento
 * en archivos individuales por usuario.
 */

public final class PersistenciaOrdenamiento {
    /** Prefijo para los archivos de preferencias de ordenamiento */
    private static final String PREFIJO_ARCHIVO = "prefs_ordenamiento_";
    
    /** Extensión de los archivos de preferencias */
    private static final String EXTENSION_ARCHIVO = ".txt";
    
    /** Separador de campos en el archivo de preferencias */
    private static final String SEPARADOR = ";";

    private PersistenciaOrdenamiento(){}

    /**
     * Genera el nombre del archivo de preferencias para un usuario específico.
     * 
     * @param userId ID del usuario
     * @return Nombre del archivo de preferencias para el usuario
     */
    private static String obtenerNombreArchivo(String userId) {
        return PREFIJO_ARCHIVO + userId + EXTENSION_ARCHIVO;
    }

    /**
     * Guarda las preferencias de ordenamiento de un usuario en un archivo.
     * 
     * @param context Contexto de la aplicación Android
     * @param userId ID del usuario cuyas preferencias se van a guardar
     * @param criterioPrimario Criterio principal de ordenamiento
     * @param estadoPrimario Estado del criterio principal (1 para ascendente, -1 para descendente)
     * @param criterioSecundario Criterio secundario de ordenamiento (puede ser null)
     * @param estadoSecundario Estado del criterio secundario (1 para ascendente, -1 para descendente, -1 si no aplica)
     */
    public static void guardarPreferenciasOrdenamiento(Context context,
                                                     String userId,
                                                     OrdComunicado criterioPrimario, int estadoPrimario,
                                                     OrdComunicado criterioSecundario, int estadoSecundario) {

        String contenido = criterioPrimario.name() + SEPARADOR + estadoPrimario +
                SEPARADOR + (criterioSecundario != null ? criterioSecundario.name() : "null") +
                SEPARADOR + (criterioSecundario != null ? estadoSecundario : "-1");

        try {
            context.openFileOutput(obtenerNombreArchivo(userId), Context.MODE_PRIVATE)
                    .write(contenido.getBytes());
        } catch (Exception e) {
            Log.e("PersistenciaOrdenamiento", "Error al guardar preferencias: " + e.getMessage());
        }
    }

    /**
     * Carga las preferencias de ordenamiento guardadas para un usuario y las aplica a la actividad.
     * 
     * @param context Contexto de la aplicación Android
     * @param userId ID del usuario cuyas preferencias se van a cargar
     * @param actividad Instancia de MisComunicadosActivity donde se aplicarán las preferencias
     */
    public static void cargarPreferenciasOrdenamiento(Context context, String userId, MisComunicadosActivity actividad) {
        try {
            List<String> lineas = ManejadorArchivo.leerArchivo(context, obtenerNombreArchivo(userId));
            if (lineas.isEmpty()) return;

            String[] partes = lineas.get(0).split(SEPARADOR);
            if (partes.length >= 4) {
                OrdComunicado criterioPrimario = OrdComunicado.valueOf(partes[0]);
                int estadoPrimario = Integer.parseInt(partes[1]);

                OrdComunicado criterioSecundario = !"null".equals(partes[2]) ?
                        OrdComunicado.valueOf(partes[2]) : null;
                int estadoSecundario = Integer.parseInt(partes[3]);


                actividad.configurarOrdenamiento(criterioPrimario, estadoPrimario,
                        criterioSecundario, estadoSecundario);
            }
        } catch (Exception e) {
            Log.e("PersistenciaOrdenamiento", "Error al cargar preferencias: " + e.getMessage());
        }
    }
}
