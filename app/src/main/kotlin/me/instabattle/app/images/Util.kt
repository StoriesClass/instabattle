package me.instabattle.app.images

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

import java.io.IOException
import java.net.URL

object Util : AnkoLogger{
    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    fun decodeSampledBitmapFromBytes(bytes: ByteArray, reqWidth: Int, reqHeight: Int, offset: Int = 0): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeByteArray(bytes, offset, bytes.size, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        info { "ByteArray, inSampleSize: ${options.inSampleSize}" }

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeByteArray(bytes, offset, bytes.size, options)
    }

    @Throws(IOException::class)
    fun decodeSampledBitmapFromURL(url: URL, reqWidth: Int, reqHeight: Int): Bitmap {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)
        }
        var `is` = url.openConnection().getInputStream()
        BitmapFactory.decodeStream(`is`, null, options)

        info { "ByteArray, inSampleSize: ${options.inSampleSize}" }

        options.inJustDecodeBounds = false
        `is` = url.openConnection().getInputStream()
        return BitmapFactory.decodeStream(`is`, null, options)!!
    }
}
