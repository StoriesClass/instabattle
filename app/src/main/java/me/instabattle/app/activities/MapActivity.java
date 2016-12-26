package me.instabattle.app.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

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
import me.instabattle.app.services.LocationService;
import me.instabattle.app.settings.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";

    public static final float DEFAULT_ZOOM = 14;
    public static final LatLng DEFAULT_VIEW_POINT = new LatLng(59.930969, 30.352445);

    private GoogleMap googleMap;

    private HashMap<String, Battle> battleByMarker;
    private HashMap<String, Circle> circleByMarker;

    private Circle visibleCircle = null;

    public static Class<?> gotHereFrom;

    public static LatLng viewPoint = DEFAULT_VIEW_POINT;
    public static float viewZoom = DEFAULT_ZOOM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        battleByMarker = new HashMap<>();
        circleByMarker = new HashMap<>();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "map ready");

        this.googleMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            LocationService.askLocationPermission(this);
        }

        UiSettings settings = googleMap.getUiSettings();
        settings.setZoomControlsEnabled(true);
        settings.setMapToolbarEnabled(false);
        settings.setMyLocationButtonEnabled(true);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(viewPoint, viewZoom));

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                State.chosenBattle = battleByMarker.get(marker.getId());
                BattleActivity.gotHereFrom = MapActivity.class;
                Intent viewBattle = new Intent(MapActivity.this, BattleActivity.class);
                startActivity(viewBattle);
            }
        });

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (visibleCircle != null) {
                    visibleCircle.setVisible(false);
                    visibleCircle = null;
                }
            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
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

        placeMarkers();
    }

    private void placeMarkers() {
        BattleManager.getAllAndDo(new Callback<List<Battle>>() {
            @Override
            public void onResponse(Call<List<Battle>> call, Response<List<Battle>> response) {
                List<Battle> battles = response.body();
                for (Battle nearBattle : battles) {
                    Marker newMarker = MapActivity.this.googleMap.addMarker(new MarkerOptions()
                            .position(nearBattle.getLocation())
                            .title(nearBattle.getName())
                            .snippet(nearBattle.getEntriesCount() + " photos"));
                    battleByMarker.put(newMarker.getId(), nearBattle);

                    Circle newCircle = MapActivity.this.googleMap.addCircle(new CircleOptions()
                            .center(nearBattle.getLocation())
                            .radius(nearBattle.getRadius())
                            .strokeColor(Color.GREEN)
                            .strokeWidth(4)
                            .visible(false));
                    circleByMarker.put(newMarker.getId(), newCircle);
                }
                Log.d(TAG, "placed markers for " + battles.size() + " battles");
            }

            @Override
            public void onFailure(Call<List<Battle>> call, Throwable t) {
                //TODO
                Log.e(TAG, "cant get battles: " + t);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        viewPoint = googleMap.getCameraPosition().target;
        viewZoom = googleMap.getCameraPosition().zoom;
    }

    public void goCreateBattle(View v) {
        State.creatingBattle = true;
        Intent createBattle = new Intent(this, CreateBattleActivity.class);
        startActivity(createBattle);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, gotHereFrom);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == LocationService.REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                //TODO: cant participate message
                Log.d(TAG, "didn't get location permission");
            } else {
                Log.d(TAG, "got location permission");

                try {
                    googleMap.setMyLocationEnabled(true);
                } catch (SecurityException e) {
                    //try catch block is required by API 23 and higher
                    //never get here
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
