package me.instabattle.app.activities

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_my_profile.*
import me.instabattle.app.R
import me.instabattle.app.adapters.UserEntryListAdapter
import me.instabattle.app.models.Entry
import me.instabattle.app.settings.State
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MyProfileActivity : DefaultActivity() {
    private var userEntryListAdapter: UserEntryListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        val registrationDate = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(State.currentUser.createdOn)

        currentUserName.text = State.currentUser.username
        currentUserDate.text = "Registration date: $registrationDate"
        userBattleLimit.text = "You can create ${State.currentUser.battleCreationLimit} more battles."

        State.currentUser.getEntriesAndDo(object : Callback<List<Entry>> {
            override fun onResponse(call: Call<List<Entry>>, response: Response<List<Entry>>) {
                info("got user entries")
                userEntryListAdapter = UserEntryListAdapter(this@MyProfileActivity, response.body()!!)
                userEntryList.adapter = userEntryListAdapter
            }

            override fun onFailure(call: Call<List<Entry>>, t: Throwable) {
                //TODO
                error {"cant get entries: $t"}
            }
        })
    }

    fun logout(v: View) {
        State.token = "" // FIXME check
        //State.currentUser = null

        MapActivity.viewPoint = MapActivity.DEFAULT_VIEW_POINT
        MapActivity.viewZoom = MapActivity.DEFAULT_ZOOM

        startActivity<LoginActivity>()
    }
}
