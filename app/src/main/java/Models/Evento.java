package Models;

import java.util.List;

public class Evento extends Comunicado {
    private String lugar;
    private String fecha;

    public Evento(
            int id,
            String tipo,
            String area,
            String titulo,
            List<String> audiencia,
            String decripcion,
            String nombreArchivoImagen,
            String lugar,
            String fecha
    ) {
        super(
                id,
                tipo,
                area,
                titulo,
                audiencia,
                decripcion,
                nombreArchivoImagen
        );
        this.lugar = lugar;
        this.fecha = fecha;
    }
}
