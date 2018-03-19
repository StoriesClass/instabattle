package me.instabattle.app.activities

import android.os.Bundle
import android.app.DialogFragment
import android.view.View
import kotlinx.android.synthetic.main.activity_vote.*

import me.instabattle.app.R
import me.instabattle.app.managers.EntryManager
import me.instabattle.app.models.Vote
import me.instabattle.app.settings.State
import me.instabattle.app.managers.PhotoManager
import me.instabattle.app.models.Entry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import org.jetbrains.anko.*

class VoteActivity : DefaultActivity() {
    private lateinit var firstEntry: Entry
    private lateinit var secondEntry: Entry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vote)

        firstEntry = intent.getParcelableExtra<Entry>("firstEntry")
        secondEntry = intent.getParcelableExtra<Entry>("secondEntry")

        setPhotos()
    }

    fun setPhotos() {
        PhotoManager.getPhotoInto(this, firstEntry.imageName!!, firstImage)
        PhotoManager.getPhotoInto(this, secondEntry.imageName!!, secondImage)
    }

    fun vote(v: View) {
        val winnerId: Int
        val loserId: Int
        if (v.id == R.id.voteFirstBtn) {
            winnerId = firstEntry.id!!
            loserId = secondEntry.id!!
        } else {
            winnerId = secondEntry.id!!
            loserId = firstEntry.id!!
        }

        info {"Vote info: ${State.chosenBattle!!.id} ${State.currentUser.id} $winnerId  $loserId"}

        EntryManager.voteAndDo(State.chosenBattle!!.id, State.currentUser.id, winnerId, loserId, object : Callback<Vote> {
            override fun onResponse(call: Call<Vote>, response: Response<Vote>) {
                info("vote sent")
                alert ("Nice vote, ${State.currentUser.username}!") {
                    positiveButton("Vote again") {
                        setPhotos()
                    }
                    negativeButton("To battle") {
                        startActivity<BattleActivity>()
                    }
                }.show()
            }
            override fun onFailure(call: Call<Vote>, t: Throwable) {
                toast("Failed to vote, try again later.")
                error("failed to send vote")
            }
        })
    }
}
