package Interfaces;

import android.content.Context;
import android.content.Intent;

import com.example.par5_proy2p_duenas_romero_ortega.ComunicadoDetailActivity;

import Models.Comunicado;

public interface VerComunicadoDetails {
    default void showComunicadoDetails(Context context, Comunicado comunicado) {
        Intent intent = new Intent(context, ComunicadoDetailActivity.class);
        intent.putExtra("comunicadoID", comunicado.getId());
        context.startActivity(intent);
    }
}
