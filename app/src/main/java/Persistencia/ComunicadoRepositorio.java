package Persistencia;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import Enums.AreaComunicado;
import Enums.NivelUrgencia;
import Enums.TipoAudiencia;
import Enums.TipoComunicado;
import Models.Anuncio;
import Models.Comunicado;
import Models.Evento;

public final class ComunicadoRepositorio {
    private static final List<Comunicado> comunicados = new ArrayList<>();
    private static final String comunicadostxt = "comunicados.txt";
    private static final String SEPARADOR = "\u001F";

    private ComunicadoRepositorio() {
    }

    public static List<Comunicado> cargarComunicados(Context context) {
        comunicados.clear();
        List<String> lineas = ManejadorArchivo.leerArchivo(context, comunicadostxt);

        if (lineas.isEmpty()) {
            Log.d("ComunicadoRepositorio", "No se encontraron comunicados en el archivo");
            return new ArrayList<>();
        } else {
            for (String linea : lineas) {
                if (linea == null || linea.trim().isEmpty()) {
                    continue;
                }
                String[] parts = linea.split(SEPARADOR);
                for (String part : parts) {
                    Log.d("ComunicadoRepositorio", "Parte: " + part);
                }
                try {
                    if (parts.length < 9) {
                        continue;
                    }
                    int id = Integer.parseInt(parts[0].trim());
                    String userId = parts[1].trim();

                    String tipoComunicadoStr = parts[2].trim().toUpperCase();
                    Log.d("*********************ComunicadoRepositorio", "Tipo de comunicación: " + tipoComunicadoStr);
                    TipoComunicado tipoComunicado;

                    try {
                        // First try to parse directly
                        tipoComunicado = TipoComunicado.valueOf(tipoComunicadoStr);
                    } catch (IllegalArgumentException e) {
                        Log.e("ComunicadoRepositorio", "Tipo de comunicación inválido: " + tipoComunicadoStr
                                + ". Usando ANUNCIO como valor por defecto.");
                        continue;
                    }
                    AreaComunicado area = AreaComunicado.valueOf(parts[3].trim().toUpperCase(Locale.ROOT));
                    String titulo = parts[4].trim();
                    String[] audienciaArray = parts[5].trim().split(";");
                    List<TipoAudiencia> audiencia = new ArrayList<>();

                    for (String audienciaStr : audienciaArray) {
                        if (audienciaStr != null && !audienciaStr.trim().isEmpty()) {
                            try {
                                audiencia.add(TipoAudiencia.valueOf(audienciaStr.trim().toUpperCase()));
                            } catch (IllegalArgumentException e) {
                                Log.e("ComunicadoRepositorio",
                                        "Valor de audiencia desconocido: " + audienciaStr + " en línea: " + linea, e);
                            }
                        }
                    }
                    String decripcion = parts[6].trim();
                    String nombreArchivoImagen = parts[7].trim();
                    String fecha = parts[8].trim();

                    if (tipoComunicado == TipoComunicado.ANUNCIO) {
                        if (parts.length < 10) {
                            System.out.println(
                                    "Línea de ANUNCIO con formato incorrecto (campos insuficientes): " + linea);
                            continue;
                        }
                        String nivelUrgenciaStr = parts[9].trim().toUpperCase();
                        NivelUrgencia nivelUrgencia = NivelUrgencia.valueOf(nivelUrgenciaStr);
                        Anuncio anuncio = new Anuncio(
                                id,
                                userId,
                                area,
                                titulo,
                                audiencia,
                                decripcion,
                                nombreArchivoImagen,
                                nivelUrgencia);
                        comunicados.add(anuncio);
                    } else if (tipoComunicado == TipoComunicado.EVENTO) {
                        if (parts.length < 10) {
                            System.out
                                    .println("Línea de EVENTO con formato incorrecto (campos insuficientes): " + linea);
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
                                lugar);
                        comunicados.add(evento);
                    } else {
                        System.out.println("Tipo de comunicado desconocido: " + tipoComunicado);
                    }
                } catch (NumberFormatException e) {
                    Log.e("ComunicadoRepositorio", "Error al parsear ID en línea: " + linea, e);
                } catch (IllegalArgumentException e) {
                    Log.e("ComunicadoRepositorio", "Error al parsear valor en línea: " + linea, e);
                    Log.e("ComunicadoRepositorio", "Valores esperados - TipoComunicado: " +
                            Arrays.toString(TipoComunicado.values()) +
                            ", NivelUrgencia: " + Arrays.toString(NivelUrgencia.values()));
                    Log.e("ComunicadoRepositorio", "Partes de la línea: " + Arrays.toString(parts));
                } catch (Exception e) {
                    Log.e("ComunicadoRepositorio", "Error inesperado procesando línea: " + linea, e);
                }
            }
        }
        Log.d("ComunicadoRepositorio", "Comunicados cargados: " + comunicados.size());
        return new ArrayList<>(comunicados);
    }

    public static List<Comunicado> cargarComunicados(Context context, String userId) {
        if (comunicados.isEmpty())
            cargarComunicados(context);
        Log.e("cargarComunicados**************:", comunicados.toString());
        return comunicados.stream()
                .filter(c -> c.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public static Comunicado getComunicadoByID(int id) {
        for (Comunicado comunicado : comunicados) {
            if (comunicado.getId() == id) {
                return comunicado;
            }
        }
        return null;
    }

    public static void guardarComunicado(Context context, Comunicado nuevoComunicado) {
        comunicados.add(nuevoComunicado);
        String csvLinea = nuevoComunicado.toFileFormat(SEPARADOR);
        ManejadorArchivo.escribirArchivo(context, comunicadostxt, csvLinea);
    }

    public static int generarNuevoId(Context context) {
        int max = 0;
        if (comunicados.isEmpty())
            cargarComunicados(context);
        for (Comunicado c : comunicados) {
            if (c.getId() > max)
                max = c.getId();
        }
        return max + 1;
    }
}
