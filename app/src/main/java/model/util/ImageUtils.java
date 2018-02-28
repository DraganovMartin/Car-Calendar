package model.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import model.Vehicle.Vehicle;

/**
 * Created by dimcho on 20.03.17.
 */

public class ImageUtils {

    private static final HashMap<Vehicle,Bitmap> imageHolder = new HashMap<>(10);
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
            try {
                picture = rotateImageIfRequired(picture,Uri.fromFile(new File(path)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            picture = Bitmap.createScaledBitmap(temp, 120, 120, false);
            try {
                picture = rotateImageIfRequired(picture,Uri.fromFile(new File(path)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return picture;
    }

    /**
     * ANDROID SPECIFIC !!!
     * @param path - String
     * @return Bitmap from path
     */
    public static Bitmap getBitmapFromPath(String path){
        Bitmap temp = BitmapFactory.decodeFile(path);
        return temp;
    }

    /**
     * ANDROID SPECIFIC !!!
     * @param bm - original bitmap
     * @return Scaled bitmap from path
     */
    public static Bitmap getScaledBitmap(Bitmap bm,int width, int height){
        Bitmap picture = null;
        if (width >0  && height > 0) {
            picture =  Bitmap.createScaledBitmap(bm,width,height,false);
        }
        else picture = Bitmap.createScaledBitmap(bm, 120, 120, false);
        return picture;
    }

    /**
     * ANDROID SPECIFIC !!!
     * @param uri - uri of photo from camera containing meta-data
     * @return orientation of image
     */
    public static int checkRotation(Uri uri) throws IOException{
        ExifInterface exif = new ExifInterface(uri.getPath());
        int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        return rotation;
    }

    /**
     * Android specific - saves scaled memory efficient bitmap image
     * @param path-String
     * @param bitmap - bitmap image
     * @return Path to bitmap scaled to 120x120
     */
    public static String saveBitmapImage(String path,Bitmap bitmap) throws Exception{
        if(path == null){
            throw new Exception("External directory unavailable");
        }
        File file = new File(path);
        if (!file.isDirectory() && file.length() >0){
            file.delete();
            File imageFile = new File(path);
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap,120,120,false);
            OutputStream outStream = new FileOutputStream(imageFile);
            scaled.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
            return imageFile.getAbsolutePath();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "VEHICLE_" + timeStamp;
        File storageDir= new File(path);
        File imageFile = new File(storageDir+"/"+fileName+".jpg");

        Bitmap scaled = Bitmap.createScaledBitmap(bitmap,120,120,false);
        OutputStream outStream = new FileOutputStream(imageFile);
        scaled.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        outStream.flush();
        outStream.close();

        return imageFile.getAbsolutePath();
    }

    /**
     * Maps image to vehicle (stores bitmap for faster access later)
     * @param x - Vehicle object
     * @param bitmap - image for vehicle
     */
    public static void mapImageToVehicle(Vehicle x, Bitmap bitmap){
        imageHolder.put(x,bitmap);
    }

    /**
     *
     * @param x - Vehicle object
     * @return Bitmap image for vehicle from HashMap
     */
    public static Bitmap getImageForVehicle(Vehicle x) {
        return imageHolder.get(x);
    }

    /**
     * Rotate an image if required.
     *
     * @param img           The image bitmap
     * @param selectedImage Image URI
     * @return The resulted Bitmap after manipulation
     */
    public static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

}
