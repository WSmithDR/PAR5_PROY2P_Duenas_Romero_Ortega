package Persistencia;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ManejadorArchivo {
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

    public static void escribirBinario(Context context, String nombre, byte[] bytes) {
        try (FileOutputStream fos = context.openFileOutput(nombre, Context.MODE_PRIVATE)) {
            fos.write(bytes);
        }catch(IOException e) {
            System.err.println("Error al escribir el archivo binario [IOException]: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al escribir el archivo binario [Exception]: " + e.getMessage());
        }
    }
}
