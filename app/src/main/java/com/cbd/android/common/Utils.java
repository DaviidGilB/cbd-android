package com.cbd.android.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class Utils {

    private Utils() {

    }

    public static boolean hasToken() {
        return SharedPreferencesManager.getSomeStringValue(Constants.TOKEN) != null;
    }

    public static String getToken() {
        return SharedPreferencesManager.getSomeStringValue(Constants.TOKEN);
    }

    public static void saveToken(String token) {
        SharedPreferencesManager.setSomeStringValue(Constants.TOKEN, token);
    }

    public static void deleteToken() {
        saveToken(null);
    }

    public static String getBase64FromBitmap(Bitmap bmp, int ancho, int quality) {
        String res = "";
        if (bmp != null) {
            if (bmp.getWidth() > ancho) {
                float proporcion = ancho / (float) bmp.getWidth();
                bmp = Bitmap.createScaledBitmap(bmp, ancho, (int) (bmp.getHeight() * proporcion), false);
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            byte[] imagenBytes = baos.toByteArray();
            res = Base64.encodeToString(imagenBytes, Base64.DEFAULT);
        }
        return res;
    }

    public static Bitmap getBitmapFromBase64(String base) {
        byte[] decodedString = Base64.decode(base, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static void rescaleBitmap(Bitmap bmp, int ancho, int quality) {
        if (bmp.getWidth() > ancho) {
            float proporcion = ancho / (float) bmp.getWidth();
            bmp = Bitmap.createScaledBitmap(bmp, ancho, (int) (bmp.getHeight() * proporcion), false);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, quality, baos);
    }

}
