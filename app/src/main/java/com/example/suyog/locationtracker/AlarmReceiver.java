package com.example.suyog.locationtracker;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import static java.security.AccessController.getContext;

/**
 * Created by abc on 10/3/2017.
 */

public class AlarmReceiver extends BroadcastReceiver
{
    double latitude,logitude;
    String rname,place,id;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i("Fence","in alarm receiver");
        Log.i("Fence","in alarm receiver2");
        latitude= (double) intent.getExtras().get("lat");
        logitude=(double) intent.getExtras().get("lng");
        rname= (String) intent.getExtras().get("rname");
        place=(String) intent.getExtras().get("place");
        id=(String) intent.getExtras().get("id");
        Log.i("Fence","Alarm Manager : "+latitude+" "+logitude);
        Intent location=new Intent(context,LocationService.class);
        location.putExtra("lng",logitude);
        location.putExtra("lat",latitude);
        location.putExtra("rname",rname);
        location.putExtra("place",place);
        location.putExtra("id",id);
        context.startService(location);
        Log.i("Fence","latitude"+"  "+"logitude");
   }
}
