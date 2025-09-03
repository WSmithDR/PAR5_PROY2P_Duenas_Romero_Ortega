package Models;

import java.util.List;
import java.util.Locale;

import Enums.AreaComunicado;
import Enums.TipoAudiencia;
import Enums.TipoComunicado;

public final class Evento extends Comunicado {
    /** Lugar donde se llevará a cabo el evento */
    private String lugar;

    public Evento(
            int id,
            String userId,
            AreaComunicado area,
            String titulo,
            List<TipoAudiencia> audiencia,
            String descripcion,
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
                descripcion,
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

    /**
     * Convierte el evento a un formato de cadena para ser guardado en archivo.
     * Incluye la información del comunicado base más el lugar del evento.
     * 
     * @param separator Separador a utilizar entre campos
     * @return Cadena formateada con los datos del evento
     */
    @Override
    public String toFileFormat(String separator) {
        return String.format(
                Locale.US,
                "%s" + separator + "%s",
                super.toFileFormat(separator),
                this.lugar
        );
    }
}
