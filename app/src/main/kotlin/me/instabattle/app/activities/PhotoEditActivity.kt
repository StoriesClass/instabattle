package me.instabattle.app.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_photo_edit.*
import me.instabattle.app.R
import me.instabattle.app.images.Util
import me.instabattle.app.managers.PhotoManager
import me.instabattle.app.models.Entry
import me.instabattle.app.settings.KState
import me.instabattle.app.settings.State
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotoEditActivity : DefaultActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_edit)

        val battleTitle = intent.getStringExtra("battleTitle")

        editPhotoBattleTitle.text = battleTitle

        val photoBitmap = Util.decodeSampledBitmapFromBytes(photoBytes, 256, 256)
        currentPhoto.setImageBitmap(photoBitmap)
    }

    fun takeNewPhoto(v: View) = startActivity<CameraViewActivity>()

    fun useThisPhoto(v: View) {
        if (!KState.creatingBattle) {
            State.chosenBattle!!.createEntryAndDo(object : Callback<Entry> {
                override fun onResponse(call: Call<Entry>, response: Response<Entry>) {
                    PhotoManager.upload(response.body()!!.imageName, photoBytes!!)
                }

                override fun onFailure(call: Call<Entry>, t: Throwable) {
                    //TODO
                    error {"failed to add new entry: $t"}
                }
            })
            startActivity<BattleActivity>()
        } else {
            CreateBattleActivity.photoBytes = photoBytes
            startActivity<CreateBattleActivity>()
        }
    }

    companion object {
        var photoBytes: ByteArray? = null
    }
}
