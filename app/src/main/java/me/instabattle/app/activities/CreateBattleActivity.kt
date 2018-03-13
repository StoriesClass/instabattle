package me.instabattle.app.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import me.instabattle.app.R
import me.instabattle.app.images.Util
import me.instabattle.app.managers.BattleManager
import me.instabattle.app.managers.PhotoManager
import me.instabattle.app.models.Battle
import me.instabattle.app.models.Entry
import me.instabattle.app.services.LocationService
import me.instabattle.app.settings.State
import org.jetbrains.anko.info
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateBattleActivity : DefaultActivity() {
    private lateinit var newBattleTitle: TextView
    private lateinit var newBattleDescription: TextView
    private lateinit var newBattleRadius: TextView
    private lateinit var newBattlePhoto: ImageView

    private val battleTitle: String
        get() = newBattleTitle.text.toString()

    private val battleDescription: String
        get() = newBattleDescription.text.toString()

    private val battleRadius: String
        get() = newBattleRadius.text.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_battle)

        newBattleTitle = findViewById(R.id.newBattleTitle)
        newBattleDescription = findViewById(R.id.newBattleDescription)
        newBattleRadius = findViewById(R.id.newBattleRadius)
        newBattlePhoto = findViewById(R.id.newBattlePhoto)

        if (photoBytes != null) {
            newBattlePhoto.setImageBitmap(Util.decodeSampledBitmapFromBytes(photoBytes, 256, 256))
        }
    }

    public override fun onResume() {
        super.onResume()

        newBattleTitle.text = savedTitle
        newBattleDescription.text = savedDescription
        newBattleRadius.text = savedRadius
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
        BattleManager.createAndDo(State.currentUser.id,
                battleTitle,
                loc.latitude,
                loc.longitude,
                battleDescription,
                Integer.parseInt(battleRadius),
                object : Callback<Battle> {
                    override fun onResponse(call: Call<Battle>, response: Response<Battle>) {
                        info {"New battle was sent"}
                        if (response.isSuccessful) {
                            State.chosenBattle = response.body()
                            addFirstEntry(State.chosenBattle)
                            State.creatingBattle = false
                            val viewBattle = Intent(this@CreateBattleActivity, BattleActivity::class.java)
                            clearFields()
                            BattleActivity.gotHereFrom = MapActivity::class.java
                            startActivity(viewBattle)
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
                PhotoManager.upload(response.body()!!.imageName, photoBytes)
                 info {"new entry was sent"}
            }

            override fun onFailure(call: Call<Entry>, t: Throwable) {
                addFirstEntry(battle)
                error {"can't send first entry: $t"}
            }
        })
    }

    fun takeBattlePhoto(v: View) {
        ParticipatingActivity.gotHereFrom = CreateBattleActivity::class.java
        val takePhoto = Intent(this, ParticipatingActivity::class.java)

        saveFields()
        startActivity(takePhoto)
    }

    private fun saveFields() {
        savedTitle = battleTitle
        savedDescription = battleDescription
        savedRadius = battleRadius
    }

    private fun clearFields() {
        savedRadius = ""
        savedDescription = ""
        savedTitle = ""
    }

    override fun onBackPressed() {
        clearFields()
        State.creatingBattle = false
        startActivity<MapActivity>()
    }

    companion object {
        private const val BATTLE_TITLE_MINIMUM_LENGTH = 1

        private var savedTitle = ""
        private var savedDescription = ""
        private var savedRadius = ""

        var photoBytes: ByteArray? = null
    }
}