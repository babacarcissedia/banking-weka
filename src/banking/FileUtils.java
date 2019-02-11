package banking;

import com.sun.istack.internal.Nullable;

import java.io.File;

public class FileUtils {
    public final static String JPEG = "jpeg";
    public final static String JPG = "jpg";
    public final static String GIF = "gif";
    public final static String TIFF = "tiff";
    public final static String TIF = "tif";
    public final static String PNG = "png";
    public final static String CSV = "csv";

    /*
     * Get the extension of a file.
     */
    @Nullable
    public static String getExtension (File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }
}
