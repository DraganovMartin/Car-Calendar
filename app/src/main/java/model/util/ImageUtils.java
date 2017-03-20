package model.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dimcho on 20.03.17.
 */

public class ImageUtils {
    public static File createImageFile(String externalPicturesDir) throws Exception {
        if(externalPicturesDir == null){
            throw new Exception("External directory unavailable");
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        String fileName = "CAR_" + timeStamp;
        File storageDir= new File(externalPicturesDir +
                File.pathSeparator + "Carendar" + File.pathSeparator);
        File tempImageFile = File.createTempFile(fileName,".jpg",storageDir);

        return tempImageFile;
    }
}
