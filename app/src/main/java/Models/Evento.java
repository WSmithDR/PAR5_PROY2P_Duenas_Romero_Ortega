package Models;

import java.util.List;

public class Evento extends Comunicado {
    private String lugar;

    public Evento(
            int id,
            String tipo,
            String area,
            String titulo,
            List<String> audiencia,
            String decripcion,
            String nombreArchivoImagen,
            String fecha,
            String lugar
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
        this.lugar = lugar;
    }
}
