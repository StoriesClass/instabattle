package me.instabattle.app.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_battle.*
import me.instabattle.app.R
import me.instabattle.app.adapters.EntryListAdapter
import me.instabattle.app.models.Entry
import me.instabattle.app.services.LocationService
import me.instabattle.app.settings.State
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BattleActivity : DefaultActivity() {
    private lateinit var entryListAdapter: EntryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle)

        battle_title.text = State.chosenBattle!!.name
        battle_description.text = State.chosenBattle!!.description

        State.chosenBattle!!.getEntriesAndDo(object : Callback<List<Entry>> {
            override fun onResponse(call: Call<List<Entry>>, response: Response<List<Entry>>) {
                entryListAdapter = EntryListAdapter(this@BattleActivity, response.body()!!)
                entryList.adapter = entryListAdapter
            }

            override fun onFailure(call: Call<List<Entry>>, t: Throwable) {
                toast("Failed to get battle entries, try again later.")
                error { "cant get entries: $t" }
            }
        })
    }

    override fun onResume() {
        super.onResume()

        if (::entryListAdapter.isInitialized)
            entryListAdapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        val back = Intent(this, gotHereFrom)
        startActivity(back)
    }

    fun vote(v: View) {
        if (State.chosenBattle!!.entriesCount > 1) {
            State.chosenBattle!!.getVoteAndDo(object : Callback<List<Entry>> {
                override fun onResponse(call: Call<List<Entry>>, response: Response<List<Entry>>) {
                    if (response.code() == 400) {
                        toast("You've already voted for all pairs of photos.")
                        return
                    }

                    startActivity<VoteActivity>(
                            "firstEntry" to response.body()!![0],
                            "secondEntry" to response.body()!![1]
                    )

                    info { "got vote" }
                }

                override fun onFailure(call: Call<List<Entry>>, t: Throwable) {
                    toast("Failed to set new vote, try again later.")
                    error { "cant get vote: $t" }
                }
            })
        } else {
            toast("Only one entry in battle, you can't vote.")
        }
    }

    fun participate(v: View) {
        if (!LocationService.hasActualLocation()) {
            toast("There're problems with detecting your location. Try again later.")
        } else if (LocationService.isTooFarFrom(State.chosenBattle)) {
            toast("You're too far away, come closer to battle for participating!")
        } else {
            startActivity<CameraViewActivity>()
        }
    }

    companion object {
        var gotHereFrom: Class<*>? = null
    }
}
