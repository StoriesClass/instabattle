package me.instabattle.app.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.support.v4.app.FragmentActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import java.util.HashMap

import me.instabattle.app.models.Battle
import me.instabattle.app.managers.BattleManager
import me.instabattle.app.R
import me.instabattle.app.services.LocationService
import me.instabattle.app.settings.State
import me.instabattle.app.utils.Utils
import org.jetbrains.anko.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapActivity : FragmentActivity(), OnMapReadyCallback, AnkoLogger {
    private lateinit var googleMap: GoogleMap

    private lateinit var battleByMarker: HashMap<String, Battle>
    private lateinit var circleByMarker: HashMap<String, Circle>

    private var visibleCircle: Circle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        battleByMarker = HashMap()
        circleByMarker = HashMap()
    }


    override fun onMapReady(map: GoogleMap) {
        info("map is ready")

        googleMap = map

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
        } else {
            LocationService.askLocationPermission(this)
        }

        val settings = googleMap.uiSettings
        settings.isZoomControlsEnabled = true
        settings.isMapToolbarEnabled = false
        settings.isMyLocationButtonEnabled = true

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(viewPoint, viewZoom))

        googleMap.setOnInfoWindowClickListener { marker ->
            State.chosenBattle = battleByMarker[marker.id]
            BattleActivity.gotHereFrom = MapActivity::class.java
            startActivity<BattleActivity>()
        }

        googleMap.setOnMapClickListener { latLng ->
            if (visibleCircle != null) {
                visibleCircle!!.isVisible = false
                visibleCircle = null
            }
        }

        googleMap.setOnMarkerClickListener { marker ->
            if (visibleCircle != null) {
                visibleCircle!!.isVisible = false
            }
            visibleCircle = circleByMarker[marker.id]
            visibleCircle!!.isVisible = true
            false
        }

        placeMarkers()
    }

    private fun placeMarkers() {
        BattleManager.getAllAndDo(object : Callback<List<Battle>> {
            override fun onResponse(call: Call<List<Battle>>, response: Response<List<Battle>>) {
                val battles = response.body()
                for (nearBattle in battles!!) {
                    val newMarker = googleMap.addMarker(MarkerOptions()
                            .position(nearBattle.location)
                            .title(nearBattle.name)
                            .snippet(nearBattle.entriesCount.toString() + " photos"))
                    battleByMarker[newMarker.id] = nearBattle

                    val newCircle = googleMap.addCircle(CircleOptions()
                            .center(nearBattle.location)
                            .radius(nearBattle.radius.toDouble())
                            .strokeColor(Color.GREEN)
                            .strokeWidth(4f)
                            .visible(false))
                    circleByMarker[newMarker.id] = newCircle
                }
                info("placed markers for " + battles.size + " battles")
            }

            override fun onFailure(call: Call<List<Battle>>, t: Throwable) {
                Utils.showToast(this@MapActivity, "Failed to get battles, try to reload map.")
                error("cant get battles: $t")
            }
        })
    }

    public override fun onPause() {
        super.onPause()
        viewPoint = googleMap.cameraPosition.target
        viewZoom = googleMap.cameraPosition.zoom
    }

    fun goCreateBattle(v: View) {
        if (State.currentUser.battleCreationLimit == 0) {
            toast("Sorry, you've spent all of your battle creations for this week.")
            return
        }
        State.creatingBattle = true
        startActivity<CreateBattleActivity>()
    }

    override fun onBackPressed() {
        val intent = Intent(this, gotHereFrom)
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == LocationService.REQUEST_LOCATION_PERMISSION) {
            if (grantResults.size != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                toast("If you decide to participate, grant this permission later in settings.")
                info("didn't get location permission")
            } else {
                info("got location permission")

                try {
                    googleMap.isMyLocationEnabled = true
                } catch (e: SecurityException) {
                    //try catch block is required by API 23 and higher, never get here
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    companion object {
        const val DEFAULT_ZOOM = 14f
        val DEFAULT_VIEW_POINT = LatLng(59.930969, 30.352445)

        var gotHereFrom: Class<*>? = null

        var viewPoint = DEFAULT_VIEW_POINT
        var viewZoom = DEFAULT_ZOOM
    }
}
