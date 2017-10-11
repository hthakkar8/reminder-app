package com.example.suyog.locationtracker;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.LocationFence;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;

/**
 * Created by SUYOG on 10/5/2017.
 */

public class AddGeoFence{

    private PendingIntent myPendingIntent;
    private String TAG = "Fence";
    private final static int REQUEST_PERMISSION_RESULT_CODE = 42;


    public void removeLocationFence(final String fenceKey, final Context context,GoogleApiClient googleApiClient) {

        if(googleApiClient == null){
            Log.i("Fence","mGoogleApiClient null");
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(Awareness.API)
                    .build();
            googleApiClient.connect();
        }

        Awareness.FenceApi.updateFences(
                googleApiClient,
                new FenceUpdateRequest.Builder()
                        .removeFence(fenceKey)
                        .build()).setResultCallback(new ResultCallbacks<Status>() {
            @Override
            public void onSuccess(@NonNull Status status) {
                String info = "Fence " + fenceKey + " successfully removed.";
                Log.i(TAG, info);
                Toast.makeText(context, info, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(@NonNull Status status) {
                String info = "Fence " + fenceKey + " could NOT be removed.";
                Log.i(TAG, info);
                Toast.makeText(context, info, Toast.LENGTH_LONG).show();

            }

        });
    }

    public void addLocationFence(Context context, double lng, double lat,String rname,String place,String id) {
        Log.i(TAG, "in addLocationFence()");
        Intent intent = new Intent(context, FenceReceiver.class);
        intent.putExtra("rname",rname);
        intent.putExtra("place",place);
        intent.putExtra("id",id);
        PendingIntent mFencePendingIntent = PendingIntent.getBroadcast(context, 99, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Log.i(TAG, "in addLocationFence()2");


        Log.i(TAG, "Inside Location if");
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "not getting permission");
        }


        else {

            Log.i(TAG,"Got permisssion");

            AwarenessFence locationFence = LocationFence.in(lat, lng, 100, 100);
            // AwarenessFence locationFence = HeadphoneFence.during(HeadphoneState.PLUGGED_IN);
            Awareness.FenceApi.updateFences(
                    LocationService.mGoogleApiClient,
                    new FenceUpdateRequest.Builder()
                            .addFence(ReminderActivity.LOCATION_FENCE_KEY, locationFence, mFencePendingIntent)
                            .build())
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {
                                Log.i(TAG, "Fence was successfully registered.");
                            } else {
                                Log.e(TAG, "Fence could not be registered: " + status);
                            }
                        }
                    });


        }
    }




/*
    private boolean checkLocationPermission(Context context) {
        Log.i(TAG,"check permisiion");
        if( !hasLocationPermission(context) ) {
            Log.e("Tuts+", "Does not have location permission granted");
            //requestLocationPermission();
            return false;
        }

        return true;
    }

    private boolean hasLocationPermission(Context context) {
        Log.i(TAG,"has permisiion");
        return ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION )
                == PackageManager.PERMISSION_GRANTED;
    }


    private void requestLocationPermission() {
        Log.i(TAG,"before req permisiion");
        ActivityCompat.requestPermissions(
                new ReminderActivity(),
                new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION },
                REQUEST_PERMISSION_RESULT_CODE );
        Log.i(TAG,"in requestLocation()");
    }*/

    public Activity getActivity(Context context)
    {
        if (context == null)
        {
            return null;
        }
        else if (context instanceof ContextWrapper)
        {
            if (context instanceof Activity)
            {
                return (Activity) context;
            }
            else
            {
                return getActivity(((ContextWrapper) context).getBaseContext());
            }
        }

        return null;
    }

}
