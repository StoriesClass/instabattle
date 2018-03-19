package me.instabattle.app.activities

import android.os.Bundle
import com.otaliastudios.cameraview.CameraException
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import kotlinx.android.synthetic.main.activity_camera_view.*
import me.instabattle.app.R
import me.instabattle.app.managers.PhotoManager
import me.instabattle.app.models.Entry
import me.instabattle.app.settings.State
import org.jetbrains.anko.error
import org.jetbrains.anko.getStackTraceString
import org.jetbrains.anko.info
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
                    if (!State.creatingBattle) {
                        info("A picture has been taken while NOT creating a battle")
                        State.chosenBattle!!.createEntryAndDo(object : Callback<Entry> {
                            override fun onResponse(call: Call<Entry>, response: Response<Entry>) {
                                PhotoManager.upload(response.body()!!.imageName!!, jpeg!!)
                            }

                            override fun onFailure(call: Call<Entry>, t: Throwable) {
                                error("failed to add new entry: $t")
                            }
                        })
                    } else {
                        info("A picture has been taken while creating a battle")
                        CreateBattleActivity.photoBytes = jpeg
                    }
                    toast("Nice photo, " + State.currentUser.username + "!")
                } catch (e: IOException) {
                    error("failed to load picture from MediaStore") // FIXME no handling
                }

                onBackPressed()
            }
            override fun onCameraError(err: CameraException) {
                error { err.getStackTraceString() }
            }
        })
        capturePhoto.setOnClickListener {
            info("Photo has been captured")
            cameraView.capturePicture()
        }
        toggleCamera.setOnClickListener {
            info("Camera has been toggled")
            cameraView.toggleFacing()
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