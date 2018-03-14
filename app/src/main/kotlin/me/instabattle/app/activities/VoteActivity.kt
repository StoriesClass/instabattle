package me.instabattle.app.activities

import android.graphics.Bitmap
import android.os.Bundle
import android.app.DialogFragment
import android.view.View
import android.widget.ImageView

import me.instabattle.app.R
import me.instabattle.app.managers.BitmapCallback
import me.instabattle.app.managers.EntryManager
import me.instabattle.app.models.Vote
import me.instabattle.app.settings.State
import me.instabattle.app.dialogs.VotingEndDialog
import me.instabattle.app.models.Entry
import org.jetbrains.anko.info
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VoteActivity : DefaultActivity() {
    private var voteEnd: DialogFragment? = null

    private lateinit var firstImage: ImageView
    private lateinit var secondImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vote)

        voteEnd = VotingEndDialog()

        firstImage = findViewById(R.id.firstImage)
        secondImage = findViewById(R.id.secondImage)

        setPhotos()
    }

    fun setPhotos() {
        firstEntry!!.getPhotoAndDo(object : BitmapCallback {
            override fun onResponse(photo: Bitmap) {
                runOnUiThread { firstImage.setImageBitmap(photo) }
            }

            override fun onFailure(e: Exception) {
                toast("Failed to get first photo, try again later.")
                error("can't get firstEntry photo")
                onBackPressed()
            }
        })
        firstImage.invalidate()

        secondEntry!!.getPhotoAndDo(object : BitmapCallback {
            override fun onResponse(photo: Bitmap) {
                runOnUiThread { secondImage.setImageBitmap(photo) }
            }

            override fun onFailure(e: Exception) {
                toast("Failed to get first photo, try again later.")
                error("can't get secondEntry photo")
                onBackPressed()
            }
        })
        secondImage.invalidate()
    }

    fun vote(v: View) {
        val winnerId: Int
        val loserId: Int
        if (v.id == R.id.voteFirstBtn) {
            winnerId = firstEntry!!.id!!
            loserId = secondEntry!!.id!!
        } else {
            winnerId = secondEntry!!.id!!
            loserId = firstEntry!!.id!!
        }

        error {"Vote info: ${State.chosenBattle!!.id} ${State.currentUser.id} $winnerId  $loserId"}

        EntryManager.voteAndDo(State.chosenBattle!!.id, State.currentUser.id, winnerId, loserId, object : Callback<Vote> {
            override fun onResponse(call: Call<Vote>, response: Response<Vote>) {
                toast("Nice vote, ${State.currentUser.username}!")
                onBackPressed()
                info("vote sent")
            }

            override fun onFailure(call: Call<Vote>, t: Throwable) {
                toast("Failed to vote, try again later.")
                error("failed to send vote")
            }
        })
    }

    override fun onBackPressed() = startActivity<BattleActivity>()

    companion object {
        var firstEntry: Entry? = null
        var secondEntry: Entry? = null
    }
}
