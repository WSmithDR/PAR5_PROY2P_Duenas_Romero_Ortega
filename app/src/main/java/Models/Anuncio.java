package Models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Enums.AreaComunicado;
import Enums.NivelUrgencia;
import Enums.TipoAudiencia;
import Enums.TipoComunicado;

public final class Anuncio extends Comunicado {
    /** Nivel de urgencia del anuncio */
    private NivelUrgencia nivelUrgencia;


    public Anuncio(
            int id,
            String userId,
            AreaComunicado area,
            String titulo,
            List<TipoAudiencia> audiencia,
            String descripcion,
            String nombreArchivoImagen,
            NivelUrgencia nivelUrgencia
    ) {
        super(
                id,
                userId,
                TipoComunicado.ANUNCIO,
                area,
                titulo,
                audiencia,
                descripcion,
                nombreArchivoImagen,
                new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date())
        );
        this.nivelUrgencia = nivelUrgencia;
    }

    public NivelUrgencia getNivelUrgencia() {
        return nivelUrgencia;
    }

    public void setNivelUrgencia(NivelUrgencia nivelUrgencia) {
        this.nivelUrgencia = nivelUrgencia;
    }

    /**
     * Convierte el anuncio a un formato de cadena para ser guardado en archivo.
     * Incluye la información del comunicado base más el nivel de urgencia.
     * 
     * @param separator Separador a utilizar entre campos
     * @return Cadena formateada con los datos del anuncio
     */
    @Override
    public String toFileFormat(String separator) {
        return String.format(
                Locale.US,
                "%s" + separator + "%s",
                super.toFileFormat(separator),
                this.nivelUrgencia
        );
    }
}
