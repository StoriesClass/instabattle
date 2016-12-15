package me.instabattle.app.services;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.util.Date;

import me.instabattle.app.models.Battle;

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LocationService";

    public static final int REQUEST_LOCATION_PERMISSION = 123;

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 4000;

    private static GoogleApiClient googleApiClient;
    private static LocationRequest locationRequest;

    private static Location currentLocation;
    private static String lastUpdateTime;

    private static boolean requestingUpdates = false;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();

        createLocationRequest();

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public static boolean askLocationPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return false;
        }
        return true;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onStartCommand");
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        googleApiClient.disconnect();
        super.onDestroy();
    }

    private static final LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            currentLocation = location;
            lastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            Log.d(TAG, getCurrentLocation().toString());
        }
    };

    public void startLocationUpdates() {
        if (!requestingUpdates && ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) { // if required by API 23 and higher

            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, listener);
            requestingUpdates = true;

            if (currentLocation == null) {
                currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                lastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            }
        }
    }

    public void stopLocationUpdates() {
        if (requestingUpdates) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, listener);
            requestingUpdates = false;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "Connected to GoogleApiClient");

        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection suspended");
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    public static boolean hasActualLocation() {
        //FIXME check last update
        return currentLocation != null;
    }

    public static LatLng getCurrentLocation() {
        return new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
    }

    public static float getDistanceFromMeTo(LatLng point) {
        float[] res = new float[1];
        Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(),
                                point.latitude, point.longitude, res);
        return res[0];
    }

    public static boolean isTooFarFrom(Battle battle) {
        return getDistanceFromMeTo(battle.getLocation()) > battle.getRadius();
    }
}
