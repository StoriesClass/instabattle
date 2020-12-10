package me.instabattle.app.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import me.instabattle.app.GlideApp
import me.instabattle.app.R
import me.instabattle.app.databinding.ActivityCreateBattleBinding
import me.instabattle.app.managers.BattleManager
import me.instabattle.app.managers.PhotoManager
import me.instabattle.app.models.Battle
import me.instabattle.app.models.Entry
import me.instabattle.app.services.LocationService
import me.instabattle.app.settings.GlobalState
import org.jetbrains.anko.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class CreateBattleActivity : DefaultActivity() {
    var photoBytes: ByteArray? = null
    private lateinit var binding: ActivityCreateBattleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBattleBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_create_battle)
    }

    public override fun onResume() {
        super.onResume()

        if (photoBytes != null) {
            info("Photo bytes are not null")
        }
    }

    private fun validateBattle(): Boolean {
        if (binding.newBattleTitle.text.length < BATTLE_TITLE_MINIMUM_LENGTH) {
            toast("Battle name should have at least  $BATTLE_TITLE_MINIMUM_LENGTH characters!")
            return false
        }
        if (binding.newBattleRadius.text.isEmpty()) {
            toast("Enter battle radius!")
            return false
        }
        if (photoBytes == null) {
            toast("You should take a first photo in battle!")
            return false
        }
        return true
    }

    fun sendBattle(v: View) {
        if (!validateBattle()) {
            return
        }

        val loc = LocationService.getCurrentLocation()
        BattleManager.createAndDo(GlobalState.currentUser.id,
                binding.newBattleTitle.text.toString(),
                loc.latitude,
                loc.longitude,
                binding.newBattleDescription.text.toString(),
                binding.newBattleRadius.text.toString().toInt(),
                object : Callback<Battle> {
                    override fun onResponse(call: Call<Battle>, response: Response<Battle>) {
                        info {"New battle was sent"}
                        if (response.isSuccessful) {
                            GlobalState.chosenBattle = response.body()
                            addFirstEntry(GlobalState.chosenBattle!!)
                            GlobalState.creatingBattle = false
                            BattleActivity.gotHereFrom = MapActivity::class.java
                            startActivity<BattleActivity>()
                        } else {
                            info {"Couldn't create battle"}
                        }
                    }

                    override fun onFailure(call: Call<Battle>, t: Throwable) {
                        toast("Failed to send battle to server, try again later.")
                        error {"can't send new battle: $t"}
                    }
                })
    }

    private fun addFirstEntry(battle: Battle) {
        battle.createEntryAndDo(object : Callback<Entry> {
            override fun onResponse(call: Call<Entry>, response: Response<Entry>) {
                PhotoManager.upload(response.body()!!.imageName!!, photoBytes!!)
                 info {"new entry was sent"}
            }

            override fun onFailure(call: Call<Entry>, t: Throwable) {
                addFirstEntry(battle)
                error {"can't send first entry: $t"}
            }
        })
    }

    fun takeBattlePhoto(v: View) =
            startActivityForResult(intentFor<CameraViewActivity>(), TAKE_PHOTO_REQUEST)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TAKE_PHOTO_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                setPhoto(data?.getByteArrayExtra("jpeg"))
            } else {
                error { "Take photo request finished with bad result code: $resultCode" }
            }
        }
    }

    private fun setPhoto(jpeg: ByteArray?) {
        if (jpeg == null) {
            error("Received empty jpeg ByteArray")
            return
        }
        try {
            if (!GlobalState.creatingBattle) {
                info("A picture has been taken while NOT creating a battle")
                GlobalState.chosenBattle!!.createEntryAndDo(object : Callback<Entry> {
                    override fun onResponse(call: Call<Entry>, response: Response<Entry>) {
                        PhotoManager.upload(response.body()!!.imageName!!, jpeg)
                    }

                    override fun onFailure(call: Call<Entry>, t: Throwable) {
                        error("failed to add new entry: $t")
                    }
                })
            } else {
                info("A picture has been taken while creating a battle")
                photoBytes = jpeg
                GlideApp.with(this)
                        .load(jpeg)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(binding.newBattlePhoto)
                //newBattlePhoto.setImageBitmap(Util.decodeSampledBitmapFromBytes(jpeg, 256, 256))
            }
            toast("Nice photo, " + GlobalState.currentUser.username + "!")
        } catch (e: IOException) {
            error("failed to load picture from MediaStore") // FIXME no handling
        }
    }

    override fun onBackPressed() {
        GlobalState.creatingBattle = false
        startActivity<MapActivity>()
    }

    companion object {
        private const val BATTLE_TITLE_MINIMUM_LENGTH = 1
        private const val TAKE_PHOTO_REQUEST = 1
    }
}
