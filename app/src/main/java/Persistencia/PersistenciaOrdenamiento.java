package Persistencia;

import android.content.Context;
import android.util.Log;

import com.example.par5_proy2p_duenas_romero_ortega.MisComunicadosActivity;

import java.util.List;

import Enums.OrdComunicado;

public class PersistenciaOrdenamiento {
    private static final String NOMBRE_ARCHIVO = "preferencias_ordenamiento.txt";
    private static final String SEPARADOR = ";";

    private PersistenciaOrdenamiento(){}

    public static void guardarPreferenciasOrdenamiento(Context context,
                                                       OrdComunicado criterioPrimario, int estadoPrimario,
                                                       OrdComunicado criterioSecundario, int estadoSecundario) {

        String contenido = criterioPrimario.name() + SEPARADOR + estadoPrimario +
                SEPARADOR + (criterioSecundario != null ? criterioSecundario.name() : "null") +
                SEPARADOR + (criterioSecundario != null ? estadoSecundario : "-1");

        try {
            context.openFileOutput(NOMBRE_ARCHIVO, Context.MODE_PRIVATE)
                    .write(contenido.getBytes());
        } catch (Exception e) {
            Log.e("PersistenciaOrdenamiento", "Error al guardar preferencias: " + e.getMessage());
        }
    }

    public static void cargarPreferenciasOrdenamiento(Context context, MisComunicadosActivity actividad) {
        try {
            List<String> lineas = ManejadorArchivo.leerArchivo(context, NOMBRE_ARCHIVO);
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
