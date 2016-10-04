package com.teamawesome.client.recieverDependantProbes;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
/*
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.teamawesome.client.MainActivity;
*/

/*
 * Created by mason on 10/2/16.
 */
public class locationFinder extends Service /* implements

        GoogleApiClient.ConnectionCallbacks, OnConnectionFailedListener */{

    LocationManager locationManager;
    //GoogleApiClient googleApi;
    String TAG = "LocationFinder";
    Location mLastLocation;
    double mLatitude;
    double mLongitude;
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent pIntent, int flags, int startId) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "NotifyingDailyService", Toast.LENGTH_LONG).show();
        Log.i("com.example.boot", "NotifyingDailyService");

        return super.onStartCommand(pIntent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
/*

        if (googleApi == null) {
            googleApi = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        Log.d(TAG, "onCreate: API obj created.");
        googleApi.connect();

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                toastLoc(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {


            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
*/
    }

    void toastLoc(Location location) {
        Toast.makeText(locationFinder.this, "" + location.getLatitude(), Toast.LENGTH_SHORT).show();
        ;
    }
/*
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected: CALLED");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            String requests[] = {Manifest.permission.ACCESS_FINE_LOCATION};
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApi);
        if (mLastLocation != null) {
            mLatitude = mLastLocation.getLatitude();
            mLongitude = mLastLocation.getLongitude();
            Toast.makeText(locationFinder.this, "" + mLatitude, Toast.LENGTH_SHORT).show();
            Toast.makeText(locationFinder.this, "" + mLongitude, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

        Log.d(TAG, "onConnectionSuspended: CALLED");
    }

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: CALLED");
    }

    protected void onStart() {
        googleApi.connect();

    }

    protected void onStop() {
        googleApi.disconnect();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        googleApi.disconnect();
    }*/
}
