package Models;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public abstract class Comunicado implements Serializable {
    private int id;
    private String tipo;
    private String area;
    private String titulo;
    private List<String> audiencia;
    private String decripcion;
    private String nombreArchivoImagen;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area;
    }
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<String> getAudiencia() {
        return audiencia;
    }

    public void setAudiencia(List<String> audiencia) {
        this.audiencia = audiencia;
    }

    public String getDecripcion() {
        return decripcion;
    }

    public void setDecripcion(String decripcion) {
        this.decripcion = decripcion;
    }

    public String getNombreArchivoImagen() {
        return nombreArchivoImagen;
    }

    public void setNombreArchivoImagen(String nombreArchivoImagen) {
        this.nombreArchivoImagen = nombreArchivoImagen;
    }

    public Comunicado(
            int id,
            String tipo,
            String area,
            String titulo,
            List<String> audiencia,
            String decripcion,
            String nombreArchivoImagen
    ) {
        this.id = id;
        this.tipo = tipo;
        this.area = area;
        this.titulo = titulo;
        this.audiencia = audiencia;
        this.decripcion = decripcion;
        this.nombreArchivoImagen = nombreArchivoImagen;
    }

    public String toCSV(){
        return String.format(
                Locale.US,
                "%d;%s;%s;%s;%s;%s;%s",
                id, tipo, area, titulo, audiencia, decripcion, nombreArchivoImagen
        );
    }

}
