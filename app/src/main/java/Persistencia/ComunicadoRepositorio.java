package Persistencia;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Enums.NivelUrgencia;
import Enums.TipoComunicado;
import Models.Anuncio;
import Models.Comunicado;
import Models.Evento;

public class ComunicadoRepositorio {
    private static List<Comunicado> comunicados = new ArrayList<>();
    private static final String comunicadostxt = "comunicados.txt";

    public static List<Comunicado> cargarDesdeAssets(Context context) {
        List<String> lineas = ManejadorArchivo.leerArchivo(context, comunicadostxt);
        comunicados.clear();
        if (lineas.isEmpty()) {
            return comunicados;
        }else{
            for (String linea : lineas) {
                if (linea == null || linea.trim().isEmpty()) {
                    continue;
                }
                String[] parts = linea.split(",");
                try{
                    if (parts.length < 8) {
                        continue;
                    }
                    int id = Integer.parseInt(parts[0].trim());
                    TipoComunicado tipoComunicado = TipoComunicado.valueOf(parts[1].trim());
                    String area = parts[2].trim();
                    String titulo = parts[3].trim();
                    String[] audienciaArray = parts[4].trim().split(";");
                    List<String> audiencia = Arrays.asList(audienciaArray);
                    String decripcion = parts[5].trim();
                    String nombreArchivoImagen=parts[6].trim();
                    String fecha=parts[7].trim();

                    if(tipoComunicado.equals(TipoComunicado.ANUNCIO)){
                        if (parts.length < 9) {
                            System.out.println("Línea de ANUNCIO con formato incorrecto (campos insuficientes): " + linea);
                            continue;
                        }
                        NivelUrgencia nivelUrgencia = NivelUrgencia.valueOf(parts[8].trim());
                        Anuncio anuncio = new Anuncio(
                                id,
                                area,
                                titulo,
                                audiencia,
                                decripcion,
                                nombreArchivoImagen,
                                fecha,
                                nivelUrgencia
                        );
                        comunicados.add(anuncio);
                    }else if(tipoComunicado.equals(TipoComunicado.EVENTO)){
                        if (parts.length < 9) {
                            System.out.println("Línea de EVENTO con formato incorrecto (campos insuficientes): " + linea);
                            continue;
                        }
                        String lugar = parts[8].trim();
                        Evento evento = new Evento(
                                id,
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
}
