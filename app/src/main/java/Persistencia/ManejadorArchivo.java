package Persistencia;

import android.content.Context;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de utilidad para operaciones de lectura/escritura de archivos en Android.
 * Proporciona métodos estáticos para manejar archivos de texto y binarios,
 * tanto en el almacenamiento interno como en la carpeta de assets.
 */

public final class ManejadorArchivo {
    /**
     * Constructor privado para evitar la instanciación de la clase.
     */
    private ManejadorArchivo(){}
    
    /**
     * Lee un archivo de texto desde la carpeta de assets de la aplicación.
     * 
     * @param context Contexto de la aplicación Android
     * @param nombreArchivo Nombre del archivo a leer (incluyendo la ruta relativa en assets)
     * @return Lista de cadenas, donde cada elemento es una línea del archivo
     */
    public static List<String> leerArchivoDeAssets(Context context, String nombreArchivo) {
        List<String> lineas = new ArrayList<>();
        try (
                InputStream is = context.getAssets().open(nombreArchivo);
                BufferedReader br = new BufferedReader(new InputStreamReader(is))
        ){
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de assets [IOException]: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al leer el archivo de assets [Exception]: " + e.getMessage());
        }
        return lineas;
    }

    /**
     * Lee un archivo de texto del almacenamiento interno de la aplicación.
     * 
     * @param context Contexto de la aplicación Android
     * @param nombreArchivo Nombre del archivo a leer (sin ruta, se asume en el directorio de la app)
     * @return Lista de cadenas, donde cada elemento es una línea del archivo
     */
    public static List<String> leerArchivo(Context context, String nombreArchivo) {
        List<String> lineas = new ArrayList<>();
        try (
                FileInputStream fis = context.openFileInput(nombreArchivo);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr)
        ) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Archivo no encontrado en almacenamiento interno: " + nombreArchivo + " [FileNotFoundException]: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de almacenamiento interno [IOException]: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al leer el archivo de almacenamiento interno [Exception]: " + e.getMessage());
        }
        return lineas;
    }

    /**
     * Escribe una cadena de texto en un archivo del almacenamiento interno.
     * Si el archivo ya existe, añade el contenido al final del mismo.
     * 
     * @param context Contexto de la aplicación Android
     * @param nombre Nombre del archivo donde se escribirá el contenido
     * @param contenido Contenido de texto a escribir en el archivo
     */
    public static void escribirArchivo(Context context, String nombre, String contenido) {
        String contenidoConSaltoDeLinea = contenido + "\n";
        try (FileOutputStream fos = context.openFileOutput(nombre, Context.MODE_APPEND)) {
            fos.write(contenidoConSaltoDeLinea.getBytes());
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo [IOException]: " + e.getMessage());
        }catch (Exception e){
            System.err.println("Error al escribir el archivo [Exception]: " + e.getMessage());
        }
    }

    /**
     * Escribe datos binarios en un archivo del almacenamiento interno.
     * Si el archivo ya existe, será sobrescrito.
     * 
     * @param context Contexto de la aplicación Android
     * @param nombre Nombre del archivo donde se escribirán los datos
     * @param bytes Arreglo de bytes con los datos a escribir
     */
    public static void escribirBinario(Context context, String nombre, byte[] bytes) {
        try (FileOutputStream fos = context.openFileOutput(nombre, Context.MODE_PRIVATE)) {
            fos.write(bytes);
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo binario [IOException]: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al escribir el archivo binario [Exception]: " + e.getMessage());
        }
    }
        /**
         * Lee todos los bytes disponibles de un flujo de entrada (InputStream).
         * 
         * @param is Flujo de entrada del cual leer los datos
         * @return Arreglo de bytes leídos del flujo de entrada
         * @throws IOException Si ocurre un error al leer del flujo de entrada
         */
        public static byte[] readBytesFromInputStream(InputStream is) throws IOException {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[4096];
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            return buffer.toByteArray();
        }
    }

