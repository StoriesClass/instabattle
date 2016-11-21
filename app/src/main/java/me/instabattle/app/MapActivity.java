package me.instabattle.app;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private Button btnCreateBattle;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        UiSettings settings = mMap.getUiSettings();
        settings.setMyLocationButtonEnabled(true);
        settings.setZoomControlsEnabled(true);

        LatLng spb = new LatLng(59.930969, 30.352445);

        mMap.addMarker(new MarkerOptions().position(spb).title("Kelich"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(spb, 13));

        for (Battle nearBattle : BattleFactory.getNearBattles()) {
            mMap.addMarker(new MarkerOptions().position(nearBattle.getLocation()).title(nearBattle.getName()));
        }
    }

    public void goCreateBattle(View v) {
        Intent createBattle = new Intent(this, CreateBattleActivity.class);
        startActivity(createBattle);
    }
}
