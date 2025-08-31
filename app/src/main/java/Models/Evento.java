package Models;

import java.util.List;
import java.util.Locale;

import Enums.TipoComunicado;

public class Evento extends Comunicado {
    private String lugar;

    public Evento(
            int id,
            String userId,
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
    public String toCSV(){
        return String.format(
                Locale.US,
                "%s,%s",
                super.toCSV(),
                this.lugar
        );
    }
}
