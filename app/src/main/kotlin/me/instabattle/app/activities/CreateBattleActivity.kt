package me.instabattle.app.activities

import android.os.Bundle
import android.view.View
import com.evernote.android.state.State
import kotlinx.android.synthetic.main.activity_create_battle.*
import me.instabattle.app.R
import me.instabattle.app.images.Util
import me.instabattle.app.managers.BattleManager
import me.instabattle.app.managers.PhotoManager
import me.instabattle.app.models.Battle
import me.instabattle.app.models.Entry
import me.instabattle.app.services.LocationService
import me.instabattle.app.settings.GlobalState
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateBattleActivity : DefaultActivity() {
    @State
    var battleTitle = ""

    @State
    var battleDescription = ""

    @State
    var battleRadius = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_battle)

    }

    public override fun onResume() {
        super.onResume()

        if (photoBytes != null) {
            info("Photo bytes are not null")
            newBattlePhoto.setImageBitmap(Util.decodeSampledBitmapFromBytes(photoBytes!!, 256, 256))
        }

        newBattleTitle.setText(battleTitle)
        newBattleDescription.setText(battleDescription)
        newBattleRadius.setText(battleRadius)
    }

    private fun validateBattle(): Boolean {
        if (battleTitle.length < BATTLE_TITLE_MINIMUM_LENGTH) {
            toast("Battle name should have at least  $BATTLE_TITLE_MINIMUM_LENGTH characters!")
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
                battleTitle,
                loc.latitude,
                loc.longitude,
                battleDescription,
                Integer.parseInt(battleRadius),
                object : Callback<Battle> {
                    override fun onResponse(call: Call<Battle>, response: Response<Battle>) {
                        info {"New battle was sent"}
                        if (response.isSuccessful) {
                            GlobalState.chosenBattle = response.body()
                            addFirstEntry(GlobalState.chosenBattle!!)
                            GlobalState.creatingBattle = false
                            clearFields()
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

    fun takeBattlePhoto(v: View) {
        saveFields()
        startActivity<CameraViewActivity>()
    }

    private fun saveFields() {
        battleTitle = newBattleTitle.text.toString()
        battleDescription = newBattleDescription.text.toString()
        battleRadius = newBattleRadius.text.toString()
    }

    private fun clearFields() {
        battleTitle = ""
        battleDescription = ""
        battleRadius = ""
    }

    override fun onBackPressed() {
        clearFields()
        GlobalState.creatingBattle = false
        startActivity<MapActivity>()
    }

    companion object {
        private const val BATTLE_TITLE_MINIMUM_LENGTH = 1

        var photoBytes: ByteArray? = null
    }
}
