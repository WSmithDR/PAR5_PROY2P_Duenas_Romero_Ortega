package Models;

import java.util.List;
import java.util.Locale;

import Enums.NivelUrgencia;

public class Anuncio extends Comunicado {
    private NivelUrgencia nivelUrgencia;

    public Anuncio(
            int id,
            String tipo,
            String area,
            String titulo,
            List<String> audiencia,
            String decripcion,
            String nombreArchivoImagen,
            String fecha,
            NivelUrgencia nivelUrgencia
    ) {
        super(
                id,
                tipo,
                area,
                titulo,
                audiencia,
                decripcion,
                nombreArchivoImagen,
                fecha
        );
        this.nivelUrgencia = nivelUrgencia;
    }

    public NivelUrgencia getNivelUrgencia() {
        return nivelUrgencia;
    }
    public void setNivelUrgencia(NivelUrgencia nivelUrgencia) {
        this.nivelUrgencia = nivelUrgencia;
    }

    @Override
    public String toCSV(){
        return String.format(
                Locale.US,
                "%s,%s",
                super.toCSV(),
                this,nivelUrgencia
        );
    }
}
