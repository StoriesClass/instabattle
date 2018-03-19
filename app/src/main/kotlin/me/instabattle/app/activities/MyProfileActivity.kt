package me.instabattle.app.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        viewManager = LinearLayoutManager(this)

        val registrationDate = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(State.currentUser.createdOn)

        currentUserName.text = State.currentUser.username
        currentUserDate.text = "Registration date: $registrationDate"
        userBattleLimit.text = "You can create ${State.currentUser.battleCreationLimit} more battles."

        State.currentUser.getEntriesAndDo(object : Callback<List<Entry>> {
            override fun onResponse(call: Call<List<Entry>>, response: Response<List<Entry>>) {
                info("got user entries")
                viewAdapter = UserEntryListAdapter(this@MyProfileActivity, response.body()!!)

                recyclerView = findViewById<RecyclerView>(R.id.userEntryList).apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = viewAdapter
                }
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
