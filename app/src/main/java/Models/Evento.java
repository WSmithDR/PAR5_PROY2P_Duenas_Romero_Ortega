package Models;

import java.util.List;
import java.util.Locale;

import Enums.TipoAudiencia;
import Enums.TipoComunicado;

public final class Evento extends Comunicado {
    private String lugar;

    public Evento(
            int id,
            String userId,
            String area,
            String titulo,
            List<TipoAudiencia> audiencia,
            String decripcion,
            String nombreArchivoImagen,
            String fecha,
            String lugar
    ) {
        super(
                id,
                userId,
                TipoComunicado.EVENTO,
                area,
                titulo,
                audiencia,
                decripcion,
                nombreArchivoImagen,
                fecha
        );
        this.lugar = lugar;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    @Override
    public String toFileFormat(String separator){
        return String.format(
                Locale.US,
                "%s" + separator + "%s",
                super.toFileFormat(separator),
                this.lugar
        );
    }
}
