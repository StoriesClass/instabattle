package me.instabattle.app.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.Manifest;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

import me.instabattle.app.models.Battle;
import me.instabattle.app.managers.BattleManager;
import me.instabattle.app.R;
import me.instabattle.app.State;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;

    private HashMap<String, Battle> battleByMarker;

    public static Class<?> gotHereFrom;

    public static LatLng viewPoint = new LatLng(59.930969, 30.352445);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        enableCurrentLocation();

        UiSettings settings = mMap.getUiSettings();
        settings.setZoomControlsEnabled(true);
        settings.setMapToolbarEnabled(false);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(viewPoint, 15));

        battleByMarker = new HashMap<>();

        for (Battle nearBattle : BattleManager.getNearBattles(viewPoint, 1)) {
            Marker newMarker = mMap.addMarker(new MarkerOptions().position(nearBattle.getLocation()).
                    title(nearBattle.getName()).snippet(nearBattle.getEntries(0, 0).size() + " photos"));
            battleByMarker.put(newMarker.getId(), nearBattle);
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                State.chosenBattle = battleByMarker.get(marker.getId());
                BattleActivity.gotHereFrom = MapActivity.class;
                Intent viewBattle = new Intent(MapActivity.this, BattleActivity.class);
                startActivity(viewBattle);
            }
        });
    }


    private void enableCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0:
                enableCurrentLocation();
        }
    }

    public void goCreateBattle(View v) {
        Intent createBattle = new Intent(this, CreateBattleActivity.class);
        startActivity(createBattle);
    }

    @Override
    protected void onResume() {
        googleApiClient.connect();
        super.onResume();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        enableCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //TODO: show radius
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent voting = new Intent(this, gotHereFrom);
        startActivity(voting);
    }
}
