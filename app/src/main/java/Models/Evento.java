package Models;

import java.util.List;
import java.util.Locale;

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

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    @Override
    public String toCSV(){
        return String.format(
                Locale.US,
                "%s,%s",
                super.toCSV(),
                this.lugar
        );
    }
}
