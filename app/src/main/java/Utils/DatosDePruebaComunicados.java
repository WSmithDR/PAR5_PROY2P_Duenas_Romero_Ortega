package Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import Enums.AreaComunicado;
import Enums.NivelUrgencia;
import Enums.TipoAudiencia;
import Models.Anuncio;
import Models.Comunicado;
import Models.Evento;
import Persistencia.UsuarioRepositorio;

public class DatosDePruebaComunicados {

    private static final Random rd = new Random();

    private static String generarFechaAleatoria() {
        int dia = 1 + rd.nextInt(28);
        int mes = 1 + rd.nextInt(12);
        return String.format("%02d/%02d/2025", dia, mes);
    }

    public static List<Comunicado> obtenerListaDePrueba(String currentUserId) {
        List<Comunicado> comunicadosDePrueba = new ArrayList<>();
        List<String> userIds = UsuarioRepositorio.obtenerListaIdsUsuarios(currentUserId);
        if (userIds.isEmpty()) {
            throw new IllegalStateException("No se encontraron usuarios en el sistema");
        }

        String[] titulosBaseAnuncio = {"Aviso Importante", "Recordatorio", "Notificación Urgente", "Información General", "Actualización"};
        String[] titulosBaseEvento = {"Reunión Próxima", "Evento Especial", "Seminario Web", "Taller Práctico", "Celebración"};
        AreaComunicado[] areas = AreaComunicado.values();
        String[] lugares = {"Auditorio Principal", "Sala de Conferencias A", "Online", "Patio Central", "Laboratorio X"};

        for (int grupo = 1; grupo <= 10; grupo++) {
            String tituloGrupo = "Grupo " + (char)('A' + grupo - 1);
            int numComunicadosEnGrupo = 2 + rd.nextInt(3);

            for (int i = 0; i < numComunicadosEnGrupo; i++) {
                int id = 100 + (grupo * 10) + i;
                AreaComunicado area = areas[rd.nextInt(areas.length)];
                String fecha = generarFechaAleatoria();

                if (grupo % 2 == 0) {
                    String titulo = titulosBaseAnuncio[rd.nextInt(titulosBaseAnuncio.length)] + " - " + tituloGrupo;
                    NivelUrgencia urgencia = NivelUrgencia.values()[rd.nextInt(NivelUrgencia.values().length)];

                    String userId = userIds.get(rd.nextInt(userIds.size()));
                    Anuncio anuncio = new Anuncio(id, userId, area, titulo,
                            new ArrayList<>(Arrays.asList(
                                TipoAudiencia.ESTUDIANTES,
                                TipoAudiencia.PROFESORES,
                                TipoAudiencia.ADMINISTRATIVO
                            )),
                            "Comunicado " + (i+1) + " de " + numComunicadosEnGrupo + " con mismo título",
                            null, urgencia);
                    anuncio.setFecha(fecha);
                    comunicadosDePrueba.add(anuncio);
                } else {
                    String titulo = titulosBaseEvento[rd.nextInt(titulosBaseEvento.length)] + " - " + tituloGrupo;
                    String lugar = lugares[rd.nextInt(lugares.length)];

                    String userId = userIds.get(rd.nextInt(userIds.size()));
                    Evento evento = new Evento(id, userId, area, titulo,
                            new ArrayList<>(Arrays.asList(
                                TipoAudiencia.ESTUDIANTES,
                                TipoAudiencia.PROFESORES,
                                TipoAudiencia.ADMINISTRATIVO
                            )),
                            "Evento " + (i+1) + " de " + numComunicadosEnGrupo + " con mismo título",
                            null, fecha, lugar);
                    comunicadosDePrueba.add(evento);
                }
            }
        }

        for (int i = 1; i <= 15; i++) {
            AreaComunicado area = areas[rd.nextInt(areas.length)];
            String titulo = (i % 2 == 0 ? "Aviso " : "Evento ") + "Pareja " + (i + 1) / 2;

            String primeraFecha = generarFechaAleatoria();
            if (i % 2 == 0) {
                NivelUrgencia urgencia = NivelUrgencia.values()[rd.nextInt(NivelUrgencia.values().length)];
                String userId = userIds.get(rd.nextInt(userIds.size()));
                Anuncio anuncio = new Anuncio(200 + i, userId, area, titulo,
                        new ArrayList<>(Arrays.asList(TipoAudiencia.ESTUDIANTES)),
                        "Primer comunicado de la pareja",
                        null, urgencia);
                anuncio.setFecha(primeraFecha);
                comunicadosDePrueba.add(anuncio);
            } else {
                String userId = userIds.get(rd.nextInt(userIds.size()));
                Evento evento = new Evento(200 + i, userId, area, titulo,
                        new ArrayList<>(Arrays.asList(TipoAudiencia.PROFESORES)),
                        "Primer evento de la pareja",
                        null, primeraFecha, lugares[rd.nextInt(lugares.length)]);
                comunicadosDePrueba.add(evento);
            }

            String segundaFecha = generarFechaAleatoria();
            while (segundaFecha.equals(primeraFecha)) {
                segundaFecha = generarFechaAleatoria();
            }

            if (i % 2 == 0) {
                NivelUrgencia urgencia = NivelUrgencia.values()[rd.nextInt(NivelUrgencia.values().length)];
                String userId = userIds.get(rd.nextInt(userIds.size()));
                Anuncio anuncio = new Anuncio(300 + i, userId, area, titulo,
                        new ArrayList<>(Arrays.asList(TipoAudiencia.ADMINISTRATIVO)),
                        "Segundo comunicado de la pareja",
                        null, urgencia);
                anuncio.setFecha(segundaFecha);
                comunicadosDePrueba.add(anuncio);
            } else {
                String userId = userIds.get(rd.nextInt(userIds.size()));
                Evento evento = new Evento(300 + i, userId, area, titulo,
                        new ArrayList<>(Arrays.asList(TipoAudiencia.PROFESORES)),
                        "Segundo evento de la pareja",
                        null, segundaFecha, lugares[rd.nextInt(lugares.length)]);
                comunicadosDePrueba.add(evento);
            }
        }

        for (int grupoFecha = 1; grupoFecha <= 5; grupoFecha++) {
            String fechaComun = generarFechaAleatoria();
            int numComunicados = 3 + rd.nextInt(3);

            for (int i = 1; i <= numComunicados; i++) {
                int id = 500 + (grupoFecha * 10) + i;
                AreaComunicado area = areas[rd.nextInt(areas.length)];
                String titulo = "Misma Fecha " + grupoFecha + " - " +
                        (i == 1 ? "Primero" : i == 2 ? "Segundo" : i == 3 ? "Tercero" : "Cuarto") + " Título";

                if (rd.nextBoolean()) {
                    NivelUrgencia urgencia = NivelUrgencia.values()[rd.nextInt(NivelUrgencia.values().length)];
                    String userId = userIds.get(rd.nextInt(userIds.size()));
                    Anuncio anuncio = new Anuncio(id, userId, area, titulo,
                            new ArrayList<>(Arrays.asList(TipoAudiencia.ADMINISTRATIVO)),
                            "Comunicado con fecha " + fechaComun,
                            null, urgencia);
                    anuncio.setFecha(fechaComun);
                    comunicadosDePrueba.add(anuncio);
                } else {
                    String userId = userIds.get(rd.nextInt(userIds.size()));
                    Evento evento = new Evento(id, userId, area, titulo,
                            new ArrayList<>(Arrays.asList(TipoAudiencia.ADMINISTRATIVO)),
                            "Evento con fecha " + fechaComun,
                            null, fechaComun, lugares[rd.nextInt(lugares.length)]);
                    comunicadosDePrueba.add(evento);
                }
            }
        }

        for (int i = 1; i <= 20; i++) {
            int id = 400 + i;
            String titulo = "Comunicado Individual " + i;
            AreaComunicado area = areas[rd.nextInt(areas.length)];
            String fecha = generarFechaAleatoria();

            if (rd.nextBoolean()) {
                NivelUrgencia urgencia = NivelUrgencia.values()[rd.nextInt(NivelUrgencia.values().length)];
                String userId = userIds.get(rd.nextInt(userIds.size()));
                Anuncio anuncio = new Anuncio(id, userId, area, titulo,
                        new ArrayList<>(Arrays.asList(
                            TipoAudiencia.ESTUDIANTES,
                            TipoAudiencia.PROFESORES,
                            TipoAudiencia.ADMINISTRATIVO
                        )),
                        "Este es un comunicado individual de prueba",
                        null, urgencia);
                anuncio.setFecha(fecha);
                comunicadosDePrueba.add(anuncio);
            } else {
                String userId = userIds.get(rd.nextInt(userIds.size()));
                Evento evento = new Evento(id, userId, area, titulo,
                        new ArrayList<>(Arrays.asList(
                            TipoAudiencia.ESTUDIANTES,
                            TipoAudiencia.PROFESORES,
                            TipoAudiencia.ADMINISTRATIVO
                        )),
                        "Este es un evento individual de prueba",
                        null, fecha, lugares[rd.nextInt(lugares.length)]);
                comunicadosDePrueba.add(evento);
            }
        }
        return comunicadosDePrueba;
    }
}
