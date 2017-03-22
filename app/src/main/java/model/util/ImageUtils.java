package model.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
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

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "VEHICLE_" + timeStamp;
        File storageDir= new File(externalPicturesDir);
        File imageFile = new File(storageDir+"/"+fileName+".jpg");
        if(imageFile.exists()) {
            return imageFile;
        }
        else {
            imageFile.createNewFile();
        }
        return imageFile;
    }

    /**
     * ANDROID SPECIFIC !!!
     * @param path - String
     * @return Scaled bitmap from path
     */
    public static Bitmap getScaledBitmapFromPath(String path,int width, int height){
        Bitmap temp = BitmapFactory.decodeFile(path);
        Bitmap picture = null;
        if (width >0  && height > 0) {
            picture = Bitmap.createScaledBitmap(temp, width, height, false);
        }
        else picture = Bitmap.createScaledBitmap(temp, 120, 120, false);
        return picture;
    }

}
