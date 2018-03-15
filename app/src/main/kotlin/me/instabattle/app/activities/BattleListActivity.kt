package me.instabattle.app.activities

import android.os.Bundle
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_battle_list.*
import kotlinx.android.synthetic.main.battle_list_item.*

import me.instabattle.app.managers.BattleManager
import me.instabattle.app.R
import me.instabattle.app.adapters.BattleListAdapter
import me.instabattle.app.models.Battle
import org.jetbrains.anko.contentView
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BattleListActivity : DefaultActivity() {
    private lateinit var battleListAdapter: BattleListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_list)

        BattleManager.getAllAndDo(object : Callback<List<Battle>> {
            override fun onResponse(call: Call<List<Battle>>, response: Response<List<Battle>>) {
                battleListAdapter = BattleListAdapter(this@BattleListActivity, response.body()!!)
                battleList.adapter = battleListAdapter
            }

            override fun onFailure(call: Call<List<Battle>>, t: Throwable) {
                toast("Failed to get battles, try again later.")
                error {"cant get battles:$t"}
            }
        })
    }

    override fun onBackPressed() = startActivity<MenuActivity>()
}
