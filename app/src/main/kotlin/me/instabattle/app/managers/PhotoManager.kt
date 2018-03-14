package me.instabattle.app.managers

import android.util.Log

import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils

import java.io.ByteArrayInputStream
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.util.HashMap

import me.instabattle.app.images.Util

object PhotoManager {
    private val TAG = "PhotoManager"
    private val cloudinary: Cloudinary
    private val config = HashMap<String, String>()

    init {
        config["cloud_name"] = "instabattle"
        config["api_key"] = "277887798658767"
        config["api_secret"] = "V3VsI9jHpatwH1nsVgf6NP11nSg"
        cloudinary = Cloudinary(config)
    }

    fun getPhotoAndDo(name: String, callback: BitmapCallback) {
        val exception: Exception
        try {
            val url = URL(cloudinary.url().generate(name) + ".jpg")
            Thread {
                try {
                    val image = Util.decodeSampledBitmapFromURL(url, 256, 256)
                    callback.onResponse(image)
                    Log.d(TAG, "Got photo from " + url.toString())
                } catch (e: IOException) {
                    Log.e(TAG, "cannot establish connection with " + url.toString())
                    callback.onFailure(e)
                }
            }.start()
            return
        } catch (e: MalformedURLException) {
            Log.e(TAG, "can't create url from name")
            exception = e
        }

        Thread { callback.onFailure(exception) }.start()
    }

    fun upload(name: String, photo: ByteArray) {
        Thread {
            try {
                val params = ObjectUtils.asMap("public_id", name)
                cloudinary.uploader().upload(ByteArrayInputStream(photo), params)
            } catch (e: IOException) {
                Log.e("Cloudinary fail", "can't upload photo")
            }
        }.start()
    }
}
