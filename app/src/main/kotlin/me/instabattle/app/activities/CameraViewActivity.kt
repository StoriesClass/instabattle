package me.instabattle.app.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.otaliastudios.cameraview.AspectRatio
import com.otaliastudios.cameraview.CameraException
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.SizeSelectors
import kotlinx.android.synthetic.main.activity_camera_view.*
import me.instabattle.app.R
import org.jetbrains.anko.error
import org.jetbrains.anko.getStackTraceString
import org.jetbrains.anko.info

class CameraViewActivity: DefaultActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_view)
        setSize()
        camera.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(jpeg: ByteArray?) {
                val returnIntent = Intent()
                returnIntent.putExtra("jpeg", jpeg)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
            override fun onCameraError(err: CameraException) {
                error { err.getStackTraceString() }
            }
        })
        capturePhoto.setOnClickListener {
            info("Photo has been captured")
            camera.capturePicture()
        }
        toggleCamera.setOnClickListener {
            info("Camera has been toggled")
            camera.toggleFacing()
        }
    }

    // FIXME it's not working for some reason :hmm:
    private fun setSize() {
        val width = SizeSelectors.minWidth(1000)
        val height = SizeSelectors.minWidth(2000)
        val dimensions = SizeSelectors.and(width, height) // Matches sizes bigger than 1000x2000.
        val ratio = SizeSelectors.aspectRatio(AspectRatio.of(1, 1), 0f) // Matches 1:1 sizes.

        val result = SizeSelectors.or(
                SizeSelectors.and(ratio, dimensions), // Try to match both constraints
                ratio, // If none is found, at least try to match the aspect ratio
                SizeSelectors.biggest() // If none is found, take the biggest
        )
        camera.setPictureSize(result)
    }

    override fun onResume() {
        super.onResume()
        camera.start()
    }

    override fun onPause() {
        super.onPause()
        camera.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        camera.destroy()
    }
}