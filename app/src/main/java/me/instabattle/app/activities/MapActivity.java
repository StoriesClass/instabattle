package me.instabattle.app.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

import me.instabattle.app.models.Battle;
import me.instabattle.app.managers.BattleManager;
import me.instabattle.app.R;
import me.instabattle.app.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener {
    private static String TAG = "MapActivity";

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;

    private HashMap<String, Battle> battleByMarker;
    private HashMap<String, Circle> circleByMarker;

    private Circle visibleCircle = null;

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

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(viewPoint, 1));

        battleByMarker = new HashMap<>();
        circleByMarker = new HashMap<>();

        BattleManager.getAllBattlesAndDo(new Callback<List<Battle>>() {
            @Override
            public void onResponse(Call<List<Battle>> call, Response<List<Battle>> response) {
                for (Battle nearBattle : response.body()) {
                    Marker newMarker = mMap.addMarker(new MarkerOptions()
                            .position(nearBattle.getLocation())
                            .title(nearBattle.getName())
                            .snippet(nearBattle.getEntriesCount() + " photos"));
                    battleByMarker.put(newMarker.getId(), nearBattle);

                    Circle newCircle = mMap.addCircle(new CircleOptions()
                            .center(nearBattle.getLocation())
                            .radius(nearBattle.getRadius())
                            .strokeColor(Color.GREEN)
                            .strokeWidth(4)
                            .visible(false));
                    circleByMarker.put(newMarker.getId(), newCircle);

                    Log.i(TAG, "added battle: " + nearBattle.getName() + " in " + nearBattle.getLocation() + " with radius=" + nearBattle.getRadius());
                }
            }

            @Override
            public void onFailure(Call<List<Battle>> call, Throwable t) {
                //TODO
                Log.e(TAG, "cant get battles");
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                State.chosenBattle = battleByMarker.get(marker.getId());
                BattleActivity.gotHereFrom = MapActivity.class;
                Intent viewBattle = new Intent(MapActivity.this, BattleActivity.class);
                startActivity(viewBattle);
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (visibleCircle != null) {
                    visibleCircle.setVisible(false);
                    visibleCircle = null;
                }
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (visibleCircle != null) {
                    visibleCircle.setVisible(false);
                }
                visibleCircle = circleByMarker.get(marker.getId());
                visibleCircle.setVisible(true);
                return false;
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
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
