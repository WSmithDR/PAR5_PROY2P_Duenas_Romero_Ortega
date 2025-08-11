package Models;

import java.util.List;

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
            NivelUrgencia nivelUrgencia
    ) {
        super(id, tipo, area, titulo, audiencia, decripcion, nombreArchivoImagen);
        this.nivelUrgencia = nivelUrgencia;
    }

    public NivelUrgencia getNivelUrgencia() {
        return nivelUrgencia;
    }
    public void setNivelUrgencia(NivelUrgencia nivelUrgencia) {
        this.nivelUrgencia = nivelUrgencia;
    }
}
