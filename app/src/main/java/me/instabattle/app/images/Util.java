package me.instabattle.app.images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Util {
    private static final String TAG = "images.Util";
    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private static Bitmap decodeSampledBitmapFromBytes(byte[] bytes, int offset, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, offset, bytes.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        Log.d(TAG, "ByteArray, inSampleSize:" + options.inSampleSize);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bytes, offset, bytes.length, options);
    }

    public static Bitmap decodeSampledBitmapFromBytes(byte[] bytes, int reqWidth, int reqHeight) {
        return decodeSampledBitmapFromBytes(bytes, 0, reqWidth, reqHeight);
    }

    public static Bitmap decodeSampledBitmapFromURL(URL url, int reqWidth, int reqHeight) throws IOException {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream is = url.openConnection().getInputStream();
        BitmapFactory.decodeStream(is, null, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        Log.d(TAG, "Stream, inSampleSize:" + options.inSampleSize);

        options.inJustDecodeBounds = false;
        is = url.openConnection().getInputStream();
        return BitmapFactory.decodeStream(is, null, options);
    }
}
