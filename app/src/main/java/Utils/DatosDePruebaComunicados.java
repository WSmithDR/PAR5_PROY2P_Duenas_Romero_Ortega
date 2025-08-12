package Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import Enums.NivelUrgencia;
import Models.Anuncio;
import Models.Comunicado;
import Models.Evento;

public class DatosDePruebaComunicados {

    public static List<Comunicado> obtenerListaDePrueba() {
        List<Comunicado> comunicadosDePrueba = new ArrayList<>();
        Random random = new Random();

        String[] titulosBaseAnuncio = {"Aviso Importante", "Recordatorio", "Notificación Urgente", "Información General", "Actualización"};
        String[] titulosBaseEvento = {"Reunión Próxima", "Evento Especial", "Seminario Web", "Taller Práctico", "Celebración"};
        String[] areas = {"Rectorado", "Académico", "Bienestar Estudiantil", "Sistemas", "Biblioteca", "Deportes"};
        String[] lugares = {"Auditorio Principal", "Sala de Conferencias A", "Online", "Patio Central", "Laboratorio X"};

        for (int i = 1; i <= 100; i++) {
            int id = 100 + i; // IDs únicos para la prueba
            String area = areas[random.nextInt(areas.length)];
            String descripcion = "Esta es la descripción para el comunicado número " + i + ".";
            String nombreArchivoImagen = null; // Sin imágenes para la prueba por ahora
            String fecha = String.format("%02d/%02d/2025", random.nextInt(28) + 1, random.nextInt(12) + 1);
            List<String> audiencia = new ArrayList<>(Arrays.asList("Todos", "Estudiantes")); // Audiencia de ejemplo

            if (i % 2 == 0) { // Alternar entre Anuncio y Evento
                // Crear Anuncio
                String titulo = titulosBaseAnuncio[random.nextInt(titulosBaseAnuncio.length)] + " #" + i;
                NivelUrgencia urgencia;
                int randUrgencia = random.nextInt(3);
                if (randUrgencia == 0) urgencia = NivelUrgencia.ALTA;
                else if (randUrgencia == 1) urgencia = NivelUrgencia.MEDIA;
                else urgencia = NivelUrgencia.BAJA;

                Anuncio anuncio = new Anuncio(id, area, titulo, audiencia, descripcion, nombreArchivoImagen, urgencia);
                comunicadosDePrueba.add(anuncio);

            } else {
                // Crear Evento
                String titulo = titulosBaseEvento[random.nextInt(titulosBaseEvento.length)] + " #" + i;
                String lugar = lugares[random.nextInt(lugares.length)];
                // Constructor de Evento: Evento(int id, String area, String titulo, List<String> audiencia, String descripcion, String nombreArchivoImagen, String fecha, String lugar)
                Evento evento = new Evento(id, area, titulo, audiencia, descripcion, nombreArchivoImagen, fecha, lugar);
                comunicadosDePrueba.add(evento);
            }
        }
        return comunicadosDePrueba;
    }
}
