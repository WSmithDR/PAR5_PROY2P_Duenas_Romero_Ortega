package Models;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import Enums.TipoComunicado;

public abstract class Comunicado implements Serializable {

    private int id;
    private TipoComunicado tipo;
    private String area;
    private String titulo;
    private List<String> audiencia;
    private String descripcion;
    private String nombreArchivoImagen;
    private String fecha;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoComunicado getTipo() {
        return tipo;
    }

    public void setTipo(TipoComunicado tipo) {
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
        return descripcion;
    }

    public void setDecripcion(String decripcion) {
        this.descripcion = decripcion;
    }

    public String getNombreArchivoImagen() {
        return nombreArchivoImagen;
    }

    public void setNombreArchivoImagen(String nombreArchivoImagen) {
        this.nombreArchivoImagen = nombreArchivoImagen;
    }

    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Comunicado(
            int id,
            TipoComunicado tipo,
            String area,
            String titulo,
            List<String> audiencia,
            String decripcion,
            String nombreArchivoImagen,
            String fecha
    ) {
        this.id = id;
        this.tipo = tipo;
        this.area = area;
        this.titulo = titulo;
        this.audiencia = audiencia;
        this.descripcion = decripcion;
        this.nombreArchivoImagen = nombreArchivoImagen;
        this.fecha = fecha;

    }

    public String toCSV(){
        String audienciaStr = String.join(";", this.audiencia);
        return String.format(
                Locale.US,
                "%d,%s,%s,%s,%s,%s,%s,%s",
                id,
                tipo,
                area,
                titulo,
                audienciaStr,
                descripcion,
                nombreArchivoImagen,
                fecha
        );
    }

}
