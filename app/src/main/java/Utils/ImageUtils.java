package Utils;

import android.content.Context;
import android.net.Uri;

import java.io.File;

public class ImageUtils {
    private ImageUtils(){}

    public static Uri obtenerImagenUri(Context context, String nombreArchivo) {
        File file = new File(context.getFilesDir(), nombreArchivo);
        return Uri.fromFile(file);
    }

}
