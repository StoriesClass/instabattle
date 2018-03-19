package me.instabattle.app.managers

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import me.instabattle.app.GlideApp

import java.io.ByteArrayInputStream
import java.io.IOException

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import kotlin.concurrent.thread

object PhotoManager: AnkoLogger {
    private val config = mapOf(
            "cloud_name" to "instabattle",
            "api_key" to "277887798658767",
            "api_secret" to "V3VsI9jHpatwH1nsVgf6NP11nSg"
    )
    private val cloudinary = Cloudinary(config)

    fun getPhotoInto(ctx: Context, name: String, imageView: ImageView) {
        val url = cloudinary.url().generate(name) + ".jpg"
        //val url = URL(cloudinary.url().generate(name) + ".jpg")
        //val image = Util.decodeSampledBitmapFromURL(url, 256, 256)
        GlideApp
                .with(ctx)
                .load(url)
                //.placeholder(R.mipmap.ic_launcher) // can also be a drawable
                //.error(R.mipmap.future_studio_launcher) // will be displayed if the image cannot be loaded
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    fun upload(name: String, photo: ByteArray) {
        thread {
            try {
                val params = ObjectUtils.asMap("public_id", name)
                cloudinary.uploader().upload(ByteArrayInputStream(photo), params)
            } catch (e: IOException) {
                error("Can't upload photo")
            }
        }
    }
}
