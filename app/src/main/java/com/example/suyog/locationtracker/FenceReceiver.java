package com.example.suyog.locationtracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
/**
 * Created by SUYOG on 10/5/2017.
 */

public class FenceReceiver extends BroadcastReceiver{

    final  String TAG = "Fence";
    DatabaseReference mReminderRef;
    FirebaseAuth  mAuth;



    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"in receiver");
        FenceState fenceState = FenceState.extract(intent);

        if (TextUtils.equals(fenceState.getFenceKey(), ReminderActivity.LOCATION_FENCE_KEY)) {
            switch(fenceState.getCurrentState()) {
                case FenceState.TRUE:
                    //Log.i(TAG, "Location Entering");
                    Toast.makeText(context,"entering in location",Toast.LENGTH_LONG).show();

                    createNotification(context,intent.getStringExtra("rname"),intent.getStringExtra("place"),"Alert");




                    Log.i("Fence",LocationService.mGoogleApiClient.toString()+"");
                    LocationService.mAddGeoFence.removeLocationFence(ReminderActivity.LOCATION_FENCE_KEY,context);
                    context.stopService(new Intent(context,LocationService.class));
                    Log.i(TAG,"IN ALARM");
                    break;
                case FenceState.FALSE:
                    // Log.i(TAG, "Location false");
                    Toast.makeText(context,"exiting from location",Toast.LENGTH_LONG).show();
                    break;
                case FenceState.UNKNOWN:
                    Log.i(TAG, "unknown state.");
                    break;
            }
        }
    }


    private void createNotification(Context context, String msg, String msgText, String alert)
    {
        PendingIntent pi = PendingIntent.getActivity(context,0,new Intent(context,MainActivity.class),0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(msg)
                .setContentText(msgText)
                .setTicker(alert)
                .setSmallIcon(R.drawable.icon)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setVibrate(new long[]{1000,1000});

        mBuilder.setContentIntent(pi);

        mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);

        NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        nm.notify(1,mBuilder.build());
    }




}
