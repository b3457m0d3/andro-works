package com.example.xxxxxxxxx;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.widget.Toast;

public class ExifUtil {
	
	/* Constant Variables that holds the value for the screen orientation */
	public static final int ORIENTATION_NORMAL = 1;
	public static final int ORIENTATION_FLIP_HORIZONTAL = 2;
	public static final int ORIENTATION_ROTATE_180 = 3;
	public static final int ORIENTATION_FLIP_VERTICAL = 4;
	public static final int ORIENTATION_TRANSPOSE = 5;
	public static final int ORIENTATION_ROTATE_90 = 6;
	public static final int ORIENTATION_TRANSVERSE = 7;
	public static final int ORIENTATION_ROTATE_270 = 8;
    
    public static Bitmap rotateBitmap(String src, Bitmap bitmap) {
        try {
            int orientation = getExifOrientation(src);
            
            if (orientation == ORIENTATION_NORMAL) {
                return bitmap;
            }
            
            Matrix matrix = new Matrix();
            switch (orientation) {
            case ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
            }
            
            try {
                Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();
                return oriented;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 
        
        return bitmap;
    }
    
    /* Function to get the EXIF orientation of the image */
    private static int getExifOrientation(String src) throws IOException {
        int orientation = 1;
        
        try {
            /**
             * if your are targeting only api level >= 5
             * ExifInterface exif = new ExifInterface(src);
             * orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
             */
            if (Build.VERSION.SDK_INT >= 5) {
                Class<?> exifClass = Class.forName("android.media.ExifInterface");
                Constructor<?> exifConstructor = exifClass.getConstructor(new Class[] { String.class });
                Object exifInstance = exifConstructor.newInstance(new Object[] { src });
                Method getAttributeInt = exifClass.getMethod("getAttributeInt", new Class[] { String.class, int.class });
                Field tagOrientationField = exifClass.getField("TAG_ORIENTATION");
                String tagOrientation = (String) tagOrientationField.get(null);
                orientation = (Integer) getAttributeInt.invoke(exifInstance, new Object[] { tagOrientation, 1});
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        
        return orientation;
    }
}
