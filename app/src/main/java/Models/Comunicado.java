package Models;

import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import Enums.AreaComunicado;
import Enums.OrdComunicado;

import Enums.TipoAudiencia;
import Enums.TipoComunicado;

public abstract class Comunicado implements Serializable, Comparable<Comunicado> {

    private int id;
    private String userId;
    private TipoComunicado tipo;
    private AreaComunicado area;
    private String titulo;
    private List<TipoAudiencia> audiencia;
    private String descripcion;
    private String nombreArchivoImagen;
    private String fecha;

    public Comunicado(
        int id,
        String userId,
        TipoComunicado tipo,
        AreaComunicado area,
        String titulo,
        List<TipoAudiencia> audiencia,
        String decripcion,
        String nombreArchivoImagen,
        String fecha
) {
    this.id = id;
    this.userId = userId;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public TipoComunicado getTipo() {
        return tipo;
    }

    public void setTipo(TipoComunicado tipo) {
        this.tipo = tipo;
    }

    public AreaComunicado getArea() {
        return area;
    }
    public void setArea(AreaComunicado area) {

        this.area = area;
    }
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<TipoAudiencia> getAudiencia() {
        return audiencia;
    }

    public void setAudiencia(List<TipoAudiencia> audiencia) {
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



    // Using the same separator as in ComunicadoRepositorio for consistency
    public String toFileFormat(String separator) {
        String audienciaStr;
        if (this.audiencia != null && !this.audiencia.isEmpty()) {
            audienciaStr = this.audiencia.stream()
                    .map(TipoAudiencia::name)
                    .collect(Collectors.joining(";"));
        } else {
            audienciaStr = "";
        }
        StringBuilder formatBuilder = new StringBuilder();
        formatBuilder.append("%d").append(separator)
                .append("%s").append(separator)
                .append("%s").append(separator)
                .append("%s").append(separator)
                .append("%s").append(separator)
                .append("%s").append(separator)
                .append("%s").append(separator)
                .append("%s").append(separator)
                .append("%s");

        try {
            return String.format(
                    Locale.US,
                    formatBuilder.toString(),
                    id,
                    userId,
                    tipo.name(),
                    area.name(),
                    titulo,
                    audienciaStr,
                    descripcion,
                    nombreArchivoImagen,
                    fecha
            );
        } catch (Exception e) {
            Log.e("Comunicado", "Error al formatear el comunicado: " + e.getMessage());
            throw e;
        }
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
