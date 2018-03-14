package me.instabattle.app.activities

import android.os.Bundle
import android.view.View

import me.instabattle.app.R
import me.instabattle.app.services.LocationService
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity

class MenuActivity : DefaultActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        startService(intentFor<LocationService>())
    }

    fun onButtonClick(view: View) {
        when (view.id) {
            R.id.mapBtn -> {
                MapActivity.gotHereFrom = MenuActivity::class.java
                startActivity<MapActivity>()
            }
            R.id.battlesBtn -> startActivity<BattleListActivity>()
            R.id.myProfileBtn -> startActivity<MyProfileActivity>()
        }
    }

    override fun onBackPressed() {}
}
