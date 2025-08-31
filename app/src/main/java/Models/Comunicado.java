package Models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Enums.OrdComunicado;

import Enums.TipoComunicado;

public abstract class Comunicado implements Serializable, Comparable<Comunicado> {

    private int id;
    private TipoComunicado tipo;
    private String area;
    private String titulo;
    private List<String> audiencia;
    private String descripcion;
    private String nombreArchivoImagen;
    private String fecha;

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


    @Override
    public int compareTo(Comunicado otro) {
        return this.getTitulo().compareToIgnoreCase(otro.getTitulo());
    }

    public int compareTo(Comunicado otro, OrdComunicado primaryCriteria, boolean primaryAscending, 
                        OrdComunicado secondaryCriteria, boolean secondaryAscending) {
        int primaryResult = compareByCriteria(otro, primaryCriteria, primaryAscending);

        if (primaryResult == 0 && secondaryCriteria != null) {
            return compareByCriteria(otro, secondaryCriteria, secondaryAscending);
        }
        
        return primaryResult;
    }
    
    private int compareByCriteria(Comunicado otro, OrdComunicado criteria, boolean ascending) {
        int result = 0;
        
        switch (criteria) {
            case FECHA:
                result = compararPorFecha(otro);
                break;
            case TITULO:
                result = this.compareTo(otro);
                break;
        }
        
        return ascending ? result : -result;
    }
    
    private int compararPorFecha(Comunicado otro) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date fecha1 = null;
        Date fecha2 = null;

        String fechaStr1 = this.getFecha();
        String fechaStr2 = otro.getFecha();

        try {
            if (fechaStr1 != null && !fechaStr1.isEmpty()) {
                fecha1 = sdf.parse(fechaStr1);
            }
            if (fechaStr2 != null && !fechaStr2.isEmpty()) {
                fecha2 = sdf.parse(fechaStr2);
            }
        } catch (ParseException e) {
            //
        }

        return fecha1.compareTo(fecha2);
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

    public static class EstadoOrdenamiento {
        public OrdComunicado criterio;
        public int estado;
        
        public EstadoOrdenamiento(OrdComunicado criterio, int estado) {
            this.criterio = criterio;
            this.estado = estado;
        }
        
        public boolean estaActivo() {
            return estado > 0;
        }
        
        public boolean esAscendente() {
            return estado == 1;
        }
    }

}
