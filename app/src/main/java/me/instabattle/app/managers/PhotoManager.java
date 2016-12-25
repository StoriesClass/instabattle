package me.instabattle.app.managers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PhotoManager {
    private static final String TAG = "PhotoManager";
    private static final Cloudinary cloudinary;
    private static final Map<String, String> config = new HashMap<>();

    static {
        config.put("cloud_name", "instabattle");
        config.put("api_key", "277887798658767");
        config.put("api_secret", "V3VsI9jHpatwH1nsVgf6NP11nSg");
        cloudinary = new Cloudinary(config);
    }

    public static void getPhotoAndDo(String name, final BitmapCallback callback) {
        final Exception exception;
        try {
            final URL url = new URL(cloudinary.url().generate(name) + ".jpg");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        callback.onResponse(image);
                        Log.d(TAG, "Got photo from " + url.toString());
                    } catch (IOException e) {
                        // FIXME
                        Log.e(TAG, "cannot establish connection with " + url.toString());
                        callback.onFailure(e);
                        //exception = e;
                    }
                }
            }).start();
            return;
        } catch (MalformedURLException e) {
            Log.e(TAG, "can't create url from name");
            exception = e;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(exception);
            }
        }).start();
    }

    public static void upload(final String name, final byte[] photo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map params = ObjectUtils.asMap("public_id", name);
                    cloudinary.uploader().upload(new ByteArrayInputStream(photo), params);
                } catch (IOException e) {
                    Log.e("Cloudinary fail", "can't upload photo");
                }
            }
        }).start();
    }
}
