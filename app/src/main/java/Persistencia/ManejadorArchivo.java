package Persistencia;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ManejadorArchivo {
    public static List<String> leerArchivo(Context context, String nombreArchivo) {
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
            System.err.println("Error al leer el archivo [IOException]: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al leer el archivo [Exception]: " + e.getMessage());
        }
                return lineas;
    };

    public static void escribirArchivo(Context context, String nombre, String contenido) {
        try (FileOutputStream fos = context.openFileOutput(nombre, Context.MODE_PRIVATE)) {
            fos.write(contenido.getBytes());
        } catch (IOException e) {
                    System.err.println("Error al escribir el archivo [IOException]: " + e.getMessage());
        }catch (Exception e){
            System.err.println("Error al escribir el archivo [Exception]: " + e.getMessage());
        }
    };
    public static void escribirBinario(Context context, String nombre, byte[] bytes) {
        try (FileOutputStream fos = context.openFileOutput(nombre, Context.MODE_PRIVATE)) {
            fos.write(bytes);
        }catch(IOException e) {
            System.err.println("Error al escribir el archivo [IOException]: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al escribir el archivo [Exception]: " + e.getMessage());
        }
    };
}