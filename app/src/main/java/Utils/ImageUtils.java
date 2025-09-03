package Utils;

import android.content.Context;
import android.net.Uri;

import java.io.File;

/**
 * Clase de utilidad para operaciones relacionadas con el manejo de imágenes.
 * Proporciona métodos estáticos para trabajar con URIs de imágenes en el almacenamiento interno.
 */
public class ImageUtils {
    private ImageUtils(){}

    /**
     * Obtiene la URI de una imagen almacenada en el directorio de archivos internos de la aplicación.
     *
     * @param context Contexto de la aplicación para acceder al directorio de archivos
     * @param nombreArchivo Nombre del archivo de imagen (sin ruta)
     * @return URI que apunta al archivo de imagen, o null si el contexto es nulo
     */
    public static Uri obtenerImagenUri(Context context, String nombreArchivo) {
        File file = new File(context.getFilesDir(), nombreArchivo);
        return Uri.fromFile(file);
    }

}
