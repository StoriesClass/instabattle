package me.instabattle.app.activities

import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageButton
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraUtils
import com.otaliastudios.cameraview.CameraView
import kotlinx.android.synthetic.main.activity_camera_view.*
import me.instabattle.app.R
import me.instabattle.app.managers.PhotoManager
import me.instabattle.app.models.Entry
import me.instabattle.app.settings.KState
import java.io.ByteArrayOutputStream
import me.instabattle.app.settings.State
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class CameraViewActivity: DefaultActivity() {
    lateinit var cameraView: CameraView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_view)
        cameraView = findViewById(R.id.camera)
        cameraView.addCameraListener(object: CameraListener() {
            override fun onPictureTaken(jpeg: ByteArray?) {
                try {
                    if (!KState.creatingBattle) {
                        State.chosenBattle!!.createEntryAndDo(object : Callback<Entry> {
                            override fun onResponse(call: Call<Entry>, response: Response<Entry>) {
                                PhotoManager.upload(response.body()!!.imageName, jpeg!!)
                            }

                            override fun onFailure(call: Call<Entry>, t: Throwable) {
                                error("failed to add new entry: $t")
                            }
                        })
                    } else {
                        CreateBattleActivity.photoBytes = jpeg
                    }
                    toast("Nice photo, " + State.currentUser.username + "!")
                } catch (e: IOException) {
                    error("failed to load picture from MediaStore") // FIXME no handling
                }

                onBackPressed()
            }
        })
        capturePhoto.setOnClickListener {
            cameraView.capturePicture()

        }
    }

    override fun onResume() {
        super.onResume()
        cameraView.start()
    }

    override fun onPause() {
        super.onPause()
        cameraView.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraView.destroy()
    }
}