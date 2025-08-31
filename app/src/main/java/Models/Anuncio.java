package Models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Enums.NivelUrgencia;
import Enums.TipoComunicado;

public class Anuncio extends Comunicado {
    private NivelUrgencia nivelUrgencia;


    public Anuncio(
            int id,
            String userId,
            String area,
            String titulo,
            List<String> audiencia,
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

    @Override
    public String toCSV() {
        return String.format(
                Locale.US,
                "%s,%s",
                super.toCSV(),
                this.nivelUrgencia
        );
    }
}
