package Interfaces;

import android.content.Context;
import android.content.Intent;

import com.example.par5_proy2p_duenas_romero_ortega.ComunicadoDetailActivity;

import Models.Comunicado;

public interface VerComunicadoDetails {
    /**
     * Muestra los detalles de un Comunicado navegando a la pantalla de detalles.
     * Implementación por defecto que inicia la actividad ComunicadoDetailActivity
     * con el ID del comunicado como parámetro.
     *
     * @param context Contexto de la aplicación o actividad desde donde se llama al método
     * @param comunicado Objeto Comunicado cuyos detalles se desean mostrar
     */
    default void showComunicadoDetails(Context context, Comunicado comunicado) {
        Intent intent = new Intent(context, ComunicadoDetailActivity.class);
        intent.putExtra("comunicadoID", comunicado.getId());
        context.startActivity(intent);
    }
}
