package Persistencia;

import android.content.Context;
import java.io.File; // Required for file existence check
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Enums.NivelUrgencia;
import Enums.TipoComunicado;
import Models.Anuncio;
import Models.Comunicado;
import Models.Evento;

public final class ComunicadoRepositorio {
    private static final List<Comunicado> comunicados = new ArrayList<>();
    private static final String comunicadostxt = "comunicados.txt";
    
    private ComunicadoRepositorio() {
    }

    public static List<Comunicado> cargarComunicados(Context context) {
        List<String> lineas = ManejadorArchivo.leerArchivo(context, comunicadostxt);
        //comunicados.clear();
        if (lineas.isEmpty()) {
            return comunicados;
        }else{
            for (String linea : lineas) {
                if (linea == null || linea.trim().isEmpty()) {
                    continue;
                }
                String[] parts = linea.split(",");
                try{
                    if (parts.length < 9) {
                        continue;
                    }
                    int id = Integer.parseInt(parts[0].trim());
                    String userId = parts[1].trim();
                    TipoComunicado tipoComunicado = TipoComunicado.valueOf(parts[2].trim());
                    String area = parts[3].trim();
                    String titulo = parts[4].trim();
                    String[] audienciaArray = parts[5].trim().split(";");
                    List<String> audiencia = Arrays.asList(audienciaArray);
                    String decripcion = parts[6].trim();
                    String nombreArchivoImagen=parts[7].trim();
                    String fecha=parts[8].trim();

                    if(tipoComunicado.equals(TipoComunicado.ANUNCIO)){
                        if (parts.length < 10) {
                            System.out.println("Línea de ANUNCIO con formato incorrecto (campos insuficientes): " + linea);
                            continue;
                        }
                        NivelUrgencia nivelUrgencia = NivelUrgencia.valueOf(parts[8].trim());
                        Anuncio anuncio = new Anuncio(
                                id,
                                userId,
                                area,
                                titulo,
                                audiencia,
                                decripcion,
                                nombreArchivoImagen,
                                nivelUrgencia
                        );
                        comunicados.add(anuncio);
                    }else if(tipoComunicado.equals(TipoComunicado.EVENTO)){
                        if (parts.length < 10) {
                            System.out.println("Línea de EVENTO con formato incorrecto (campos insuficientes): " + linea);
                            continue;
                        }
                        String lugar = parts[9].trim();
                        Evento evento = new Evento(
                                id,
                                userId,
                                area,
                                titulo,
                                audiencia,
                                decripcion,
                                nombreArchivoImagen,
                                fecha,
                                lugar
                        );
                        comunicados.add(evento);
                    }else{
                        System.out.println("Tipo de comunicado desconocido: " + tipoComunicado);
                    }
                }catch (NumberFormatException e){
                    System.err.println("Error al parsear ID en línea: " + linea);
                }catch (IllegalArgumentException e){
                    System.err.println("Error al parsear Enum (TipoComunicado o NivelUrgencia) en línea: " + linea);
                }catch (Exception e){
                    System.err.println("Error inesperado procesando línea: " + linea);
                }
            }
        }
        return comunicados;
    }

    public static void guardarComunicado(Context context, Comunicado nuevoComunicado) {
        comunicados.add(nuevoComunicado);

        String csvLinea = nuevoComunicado.toCSV();
        String stringParaEscribir = csvLinea;

        try {
            File file = context.getFileStreamPath(comunicadostxt);
            if (file != null && file.exists() && file.length() > 0) {
                stringParaEscribir = "\n" + csvLinea;
            }
        } catch (Exception e) {
            System.err.println("Error checking file '" + comunicadostxt + "' before append: " + e.getMessage());
        }

        ManejadorArchivo.escribirArchivo(context, comunicadostxt, stringParaEscribir);
    }

    public static int generarNuevoId(Context context) {
        int max = 0;
        if(comunicados.isEmpty()) cargarComunicados(context);
        for (Comunicado c : comunicados) {
            if (c.getId() > max) max = c.getId();
        }
        return max + 1;
    }
}
