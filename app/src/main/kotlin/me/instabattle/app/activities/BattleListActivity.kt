package me.instabattle.app.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import me.instabattle.app.R
import me.instabattle.app.adapters.BattleListAdapter
import me.instabattle.app.managers.BattleManager
import me.instabattle.app.models.Battle
import org.jetbrains.anko.error
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BattleListActivity : DefaultActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_list)

        viewManager = LinearLayoutManager(this)

        BattleManager.getAllAndDo(object : Callback<List<Battle>> {
            override fun onResponse(call: Call<List<Battle>>, response: Response<List<Battle>>) {
                viewAdapter = BattleListAdapter(this@BattleListActivity, response.body()!!)

                recyclerView = findViewById<RecyclerView>(R.id.battleList).apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = viewAdapter
                }
            }

            override fun onFailure(call: Call<List<Battle>>, t: Throwable) {
                toast("Failed to get battles, try again later.")
                error {"cant get battles:$t"}
            }
        })
    }

    override fun onResume() {
        super.onResume()

        if (::viewAdapter.isInitialized)
            viewAdapter.notifyDataSetChanged()
    }
}
